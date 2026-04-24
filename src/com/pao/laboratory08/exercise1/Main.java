package com.pao.laboratory08.exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește comanda din stdin: PRINT, SHALLOW <nume> sau DEEP <nume>
        // 3. Execută comanda:
        //    - PRINT → afișează toți studenții
        //    - SHALLOW <nume> → shallow clone + modifică orașul clonei la "MODIFICAT" + afișează
        //    - DEEP <nume> → deep clone + modifică orașul clonei la "MODIFICAT" + afișează

        //System.out.println("TODO: implementează exercițiul 1");
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;

        String comanda = scanner.nextLine();
        List<Student> listaStudenti = citesteStudentiDinFisier();

        String[] parti = comanda.split(" ", 2);
        String tipComanda = parti[0];

        if (comanda.equals("PRINT")) {
            for (Student s : listaStudenti) {
                System.out.println(s);
            }
        } else if (tipComanda.equals("SHALLOW")) {
            String numeCautat = parti[1];
            
            for (Student s : listaStudenti) {
                if (s.getNume().equals(numeCautat)) {
                    Student clona = (Student) s.clone();
                    clona.getAdresa().setOras("MODIFICAT");
                    
                    System.out.println("Original: " + s);
                    System.out.println("Clona: " + clona);
                    break;
                }
            }
        } else if (tipComanda.equals("DEEP")) {
            String numeCautat = parti[1];
            for (Student s : listaStudenti) {
                if (s.getNume().equals(numeCautat)) {   
                    Student clona = (Student) s.deepClone();
                    clona.getAdresa().setOras("MODIFICAT");
                    
                    System.out.println("Original: " + s);
                    System.out.println("Clona: " + clona);
                    break;
                }
            }
        }
            
    }

    public static List<Student> citesteStudentiDinFisier() {
        List<Student> studenti = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                if (linie.trim().isEmpty()) continue;
                
                String[] date = linie.split(",");
                if (date.length == 4) {
                    String nume = date[0].trim();
                    int varsta = Integer.parseInt(date[1].trim());
                    String oras = date[2].trim();
                    String strada = date[3].trim();
                    
                    Adresa adresa = new Adresa(oras, strada);
                    studenti.add(new Student(nume, varsta, adresa));
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fisierului: " + e.getMessage());
        }
    return studenti;
    }
}