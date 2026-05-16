package com.pao.laboratory09.exercise1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);
        
        // Asigura-te ca directorul "output" exista inainte de serializare
        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            String tip = scanner.next();

            Transaction t = new Transaction(id, suma, data, contSursa, contDestinatie, tip);
            transactions.add(t);
        }

        for (Transaction t : transactions) {
            t.setNote("procesat");
        }
        // Serializeaza lista de tranzactii in OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(transactions);
        } catch (IOException e) {
            System.err.println("Eroare la serializare: " + e.getMessage());
            return;
        }

        for (Transaction t : transactions) {
            t.setNote(null);
        }

        // Deserializeaza lista din OUTPUT_FILE cu ObjectInputStream
        List<Transaction> deserializedTransactions;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            @SuppressWarnings("unchecked")
            List<Transaction> temp = (List<Transaction>) ois.readObject();
            deserializedTransactions = temp;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Eroare la deserializare: " + e.getMessage());
            return;
        }

        while (scanner.hasNext()) {
            String command = scanner.next();

            switch (command) {
                case "LIST":
                    for (Transaction t : transactions) {
                        printTransaction(t);
                    }
                    break;

                case "FILTER":
                    String datePattern = scanner.next(); // citeste yyyy-MM
                    boolean foundFilter = false;
                    for (Transaction t : transactions) {
                        if (t.getData() != null && t.getData().startsWith(datePattern)) {
                            printTransaction(t);
                            foundFilter = true;
                        }
                    }
                    if (!foundFilter) {
                        System.out.println("Niciun rezultat.");
                    }
                    break;

                case "NOTE":
                    int searchId = scanner.nextInt(); 
                    boolean foundNote = false;
                    for (Transaction t : transactions) {
                        if (t.getId() == searchId) {
                            System.out.println("NOTE[" + searchId + "]: " + t.getNote());
                            foundNote = true;
                            break;
                        }
                    }
                    if (!foundNote) {
                        System.out.println("NOTE[" + searchId + "]: not found");
                    }
                    break;

            }
        }
        
        scanner.close();
    }

    private static void printTransaction(Transaction t) {
        System.out.printf(Locale.US, "[%d] %s %s: %.2f RON | %s -> %s%n",
                t.getId(), t.getData(), t.getTip(), t.getSuma(), t.getContSursa(), t.getContDestinatie());

    }
}
