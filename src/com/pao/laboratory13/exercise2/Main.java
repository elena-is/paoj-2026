package com.pao.laboratory13.exercise2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final int PORT = 9000;
    // CountDownLatch porneste de la 2 deoarece avem exact 2 clienti de asteptat
    private static final CountDownLatch clientsLatch = new CountDownLatch(2);
    private static final AtomicInteger clientCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("[SYSTEM] Pornire demo socket multi-client...\n");

        // 1. Pornim Serverul pe un Thread separat
        Thread serverThread = new Thread(() -> startServer());
        serverThread.start();

        // Oferim serverului un scurt moment sa deschida portul
        Thread.sleep(500);

        // 2. Pornim Clientul 1 (Alice) pe Thread separat
        Thread client1Thread = new Thread(() -> startClientAlice());
        
        // 3. Pornim Clientul 2 (Bob) pe Thread separat
        Thread client2Thread = new Thread(() -> startClientBob());

        // Pornire simultana a clientilor pentru a demonstra concurenta
        client1Thread.start();
        client2Thread.start();

        // Asteptam ca ambii clienti sa isi termine complet executia si deconectarea
        clientsLatch.await();

        // Asteptam ca si thread-ul serverului sa se opreasca complet in mod controlat
        serverThread.join();
        System.out.println("\n[SYSTEM] Demo terminat cu succes.");
    }


    private static void startServer() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Listening on port " + PORT);
            
            // Setam un timeout pe accept pentru a putea verifica daca toti clientii au terminat
            serverSocket.setSoTimeout(1000);

            // Serverul ruleaza atata timp cat clientii inca mai au treaba
            while (clientsLatch.getCount() > 0) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    int clientId = clientCounter.incrementAndGet();
                    
                    // Alocam un thread din pool pentru noul client conectat
                    threadPool.submit(new ClientSessionHandler(clientSocket, clientId));
                } catch (SocketTimeoutException e) {
                    // Timeout-ul de accept a expirat. Intram in urmatoarea iteratie si verificam Latch-ul
                }
            }
            
            System.out.println("[SERVER] Toti clientii s-au deconectat. Se incepe oprirea controlata...");
        } catch (IOException e) {
            System.err.println("[SERVER] Eroare server: " + e.getMessage());
        } finally {
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
            }
            System.out.println("[SERVER] Shutdown complet.");
        }
    }

    // Handler pentru gestionarea unei sesiuni individuale de client
    private static class ClientSessionHandler implements Runnable {
        private final Socket socket;
        private final int clientId;
        // Fiecare sesiune isi instantiaza propriul motor izolat din Exercitiul 1
        private final ProtocolEngine engine;

        public ClientSessionHandler(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
            this.engine = new ProtocolEngine();
        }

        @Override
        public void run() {
            System.out.println("[SERVER-THREAD] Client-" + clientId + " s-a conectat.");
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
            ) {
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    // Procesam comanda prin motorul de protocol
                    String response = engine.processCommand(inputLine);
                    
                    // Trimitem raspunsul inapoi la client
                    writer.println(response);
                    
                    // Logam evenimentul in consola serverului
                    System.out.println("[CLIENT-" + clientId + "] >> " + inputLine + "  =>  " + response);
                    
                    // Daca starea a devenit CLOSED, serverul opreste citirea pentru acest client
                    if (response.equals("OK CLOSED")) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("[SERVER-THREAD] Conexiune pierduta cu Client-" + clientId);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    // Ignorat la inchidere
                }
                System.out.println("[SERVER-THREAD] Client-" + clientId + " s-a deconectat.");
                // Notificam Latch-ul global ca acest client a terminat sesiunea
                clientsLatch.countDown();
            }
        }
    }


    
    private static void startClientAlice() {
        List<String> commands = List.of("AUTH alice", "OPEN", "SEND salut de la alice", "CLOSE");
        executeClientScenario("Alice", commands);
    }

    private static void startClientBob() {
        List<String> commands = List.of("AUTH bob", "OPEN", "BROADCAST anunt public de la bob", "HISTORY", "CLOSE");
        executeClientScenario("Bob", commands);
    }

    private static void executeClientScenario(String clientName, List<String> commands) {
        try (
            Socket socket = new Socket("localhost", PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Pauza mica random la pornire pentru a simula un comportament asincron real in retea
            Thread.sleep((long) (Math.random() * 300));

            for (String cmd : commands) {
                out.println(cmd);
                String response = in.readLine();
                // Clientii nu printeaza logs, lasa serverul sa centralizeze log-ul cerut in enunt
                Thread.sleep(400); // Timp de gandire intre comenzi
            }
        } catch (Exception e) {
            System.err.println("[" + clientName + "] Eroare conexiune: " + e.getMessage());
        }
    }

    enum State { INIT, AUTH, OPEN, CLOSED }

    public static class ProtocolEngine {
        private State currentState = State.INIT;
        private int historyCount = 0;
        private String currentUser = "";

        public String processCommand(String rawLine) {
            String[] tokens = rawLine.trim().split("\\s+");
            if (tokens.length == 0 || tokens[0].isEmpty()) {
                return "ERR E_PARSE UNKNOWN_COMMAND";
            }

            String cmd = tokens[0].toUpperCase();

            if (currentState == State.CLOSED && isKnownCommand(cmd)) {
                return "ERR E_STATE CLOSED";
            }

            switch (cmd) {
                case "AUTH": return handleAuth(tokens);
                case "OPEN": return handleOpen(tokens);
                case "SEND": return handleSend(tokens);
                case "BROADCAST": return handleBroadcast(tokens);
                case "HISTORY": return handleHistory(tokens);
                case "CLOSE": return handleClose(tokens);
                default: return "ERR E_PARSE UNKNOWN_COMMAND";
            }
        }

        private boolean isKnownCommand(String cmd) {
            return cmd.equals("AUTH") || cmd.equals("OPEN") || cmd.equals("SEND") || 
                   cmd.equals("BROADCAST") || cmd.equals("HISTORY") || cmd.equals("CLOSE");
        }

        private String handleAuth(String[] tokens) {
            if (tokens.length < 2) return "ERR E_PARSE AUTH";
            currentUser = tokens[1];
            currentState = State.AUTH;
            historyCount = 0;
            return "OK AUTH user=" + currentUser;
        }

        private String handleOpen(String[] tokens) {
            if (tokens.length > 1) return "ERR E_PARSE OPEN";
            if (currentState == State.OPEN) return "ERR E_STATE ALREADY_OPEN";
            if (currentState == State.INIT) return "ERR E_STATE NOT_OPEN";
            currentState = State.OPEN;
            return "OK OPEN";
        }

        private String handleSend(String[] tokens) {
            if (tokens.length < 2) return "ERR E_PARSE SEND";
            if (currentState != State.OPEN) return "ERR E_STATE NOT_OPEN";
            historyCount++;
            return "OK OPEN sent";
        }

        private String handleBroadcast(String[] tokens) {
            if (tokens.length < 2) return "ERR E_PARSE BROADCAST";
            if (currentState != State.OPEN) return "ERR E_STATE NOT_OPEN";
            historyCount++;
            return "OK OPEN broadcast";
        }

        private String handleHistory(String[] tokens) {
            if (tokens.length > 1) return "ERR E_PARSE HISTORY";
            if (currentState != State.OPEN) return "ERR E_STATE NOT_OPEN";
            return "OK OPEN history=" + historyCount;
        }

        private String handleClose(String[] tokens) {
            if (tokens.length > 1) return "ERR E_PARSE CLOSE";
            if (currentState != State.OPEN) return "ERR E_STATE NOT_OPEN";
            currentState = State.CLOSED;
            return "OK CLOSED";
        }
    }
}