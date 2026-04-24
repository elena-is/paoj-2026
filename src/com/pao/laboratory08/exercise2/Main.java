package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Student;
import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește pragul de vârstă din stdin cu Scanner
        // 3. Filtrează studenții cu varsta >= prag
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        // 5. Afișează sumarul la consolă
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextInt()) return;
        int pragVarsta = scanner.nextInt();

        List<Student> totiStudentii = com.pao.laboratory08.exercise1.Main.citesteStudentiDinFisier();

        List<Student> studentiFiltrati = new ArrayList<>();
        for (Student s : totiStudentii) {
            if (s.getVarsta() >= pragVarsta) {
                studentiFiltrati.add(s);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rezultate.txt"))) {
            System.out.println("Filtru: varsta >= " + pragVarsta);
            System.out.println("Rezultate: " + studentiFiltrati.size() + " studenti");
            System.out.println();

            for (Student s : studentiFiltrati) {
                String linie = s.toString();

                writer.write(linie);
                writer.newLine();
                
                System.out.println(linie);
            }

            System.out.println();
            System.out.println("Scris in: rezultate.txt");

        } catch (IOException e) {
            System.err.println("Eroare la scrierea fisierului: " + e.getMessage());
        }

        System.out.println("TODO: implementează exercițiul 2");
    }
}

