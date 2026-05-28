package com.pao.laboratory13.exercise1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    // Enumerare pentru starile posibile ale sesiunii
    enum State {
        INIT, AUTH, OPEN, CLOSED
    }

    public static void main(String[] args) {
        try {
            run();
        } catch (IOException e) {
            // Mentinem output-ul determinist pentru checker
        }
    }

    private static void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        String firstLine = readNonEmptyLine(br);
        if (firstLine == null) {
            return;
        }

        int q = Integer.parseInt(firstLine);
        ProtocolEngine engine = new ProtocolEngine();

        for (int i = 0; i < q; i++) {
            String line = readNonEmptyLine(br);
            if (line == null) {
                return;
            }

            // Procesam comanda linie cu linie si afisam raspunsul direct
            String response = engine.processCommand(line);
            System.out.println(response);
        }
    }

    private static String readNonEmptyLine(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                return line.trim();
            }
        }
        return null;
    }

    // Motorul de protocol care izoleaza starea si logica de executie
    public static class ProtocolEngine {
        private State currentState = State.INIT;
        private int historyCount = 0;
        private String currentUser = "";

        public String processCommand(String rawLine) {
            // Curatam si impartim linia dupa spatii multiple
            String[] tokens = rawLine.trim().split("\\s+");
            if (tokens.length == 0 || tokens[0].isEmpty()) {
                return "ERR E_PARSE UNKNOWN_COMMAND";
            }

            String cmd = tokens[0].toUpperCase();

            // Regula globala: Daca starea este CLOSED, nicio comanda nu mai este procesata,
            // EXCEPTAND cazul in care comanda in sine este complet necunoscuta (UNKNOWN_COMMAND)
            if (currentState == State.CLOSED && isKnownCommand(cmd)) {
                return "ERR E_STATE CLOSED";
            }

            switch (cmd) {
                case "AUTH":
                    return handleAuth(tokens);
                case "OPEN":
                    return handleOpen(tokens);
                case "SEND":
                    return handleSend(tokens, rawLine);
                case "BROADCAST":
                    return handleBroadcast(tokens, rawLine);
                case "HISTORY":
                    return handleHistory(tokens);
                case "CLOSE":
                    return handleClose(tokens);
                default:
                    return "ERR E_PARSE UNKNOWN_COMMAND";
            }
        }

        private boolean isKnownCommand(String cmd) {
            return cmd.equals("AUTH") || cmd.equals("OPEN") || cmd.equals("SEND") || 
                   cmd.equals("BROADCAST") || cmd.equals("HISTORY") || cmd.equals("CLOSE");
        }

        private String handleAuth(String[] tokens) {
            // Pas 1: Verificare E_PARSE (aritate)
            if (tokens.length < 2) {
                return "ERR E_PARSE AUTH";
            }
            
            // Pas 2: Tranzitie si resetare parametri
            currentUser = tokens[1];
            currentState = State.AUTH;
            historyCount = 0; // Resetare obligatorie la AUTH/re-AUTH
            
            return "OK AUTH user=" + currentUser;
        }

        private String handleOpen(String[] tokens) {
            // Pas 1: Verificare E_PARSE
            if (tokens.length > 1) {
                return "ERR E_PARSE OPEN";
            }

            // Pas 2: Verificare E_STATE
            if (currentState == State.OPEN) {
                return "ERR E_STATE ALREADY_OPEN";
            }
            if (currentState == State.INIT) {
                return "ERR E_STATE NOT_OPEN";
            }

            currentState = State.OPEN;
            return "OK OPEN";
        }

        private String handleSend(String[] tokens, String rawLine) {
            // Pas 1: Verificare E_PARSE
            if (tokens.length < 2) {
                return "ERR E_PARSE SEND";
            }

            // Pas 2: Verificare E_STATE
            if (currentState != State.OPEN) {
                return "ERR E_STATE NOT_OPEN";
            }

            historyCount++;
            return "OK OPEN sent";
        }

        private String handleBroadcast(String[] tokens, String rawLine) {
            // Pas 1: Verificare E_PARSE
            if (tokens.length < 2) {
                return "ERR E_PARSE BROADCAST";
            }

            // Pas 2: Verificare E_STATE
            if (currentState != State.OPEN) {
                return "ERR E_STATE NOT_OPEN";
            }

            historyCount++;
            return "OK OPEN broadcast";
        }

        private String handleHistory(String[] tokens) {
            // Pas 1: Verificare E_PARSE
            if (tokens.length > 1) {
                return "ERR E_PARSE HISTORY";
            }

            // Pas 2: Verificare E_STATE
            if (currentState != State.OPEN) {
                return "ERR E_STATE NOT_OPEN";
            }

            return "OK OPEN history=" + historyCount;
        }

        private String handleClose(String[] tokens) {
            // Pas 1: Verificare E_PARSE
            if (tokens.length > 1) {
                return "ERR E_PARSE CLOSE";
            }

            // Pas 2: Verificare E_STATE
            if (currentState != State.OPEN) {
                return "ERR E_STATE NOT_OPEN";
            }

            currentState = State.CLOSED;
            return "OK CLOSED";
        }
    }
}