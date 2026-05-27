package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //tranzactii random din 3 luni
        List<Tranzactie> tranzactii = Arrays.asList(
            new Tranzactie(1, 1500.00, "2026-01-15", TipTranzactie.CREDIT, "RO01AAA"),
            new Tranzactie(2, 750.50,  "2026-01-22", TipTranzactie.DEBIT,  "RO02BBB"),
            new Tranzactie(3, 200.00,  "2026-02-05", TipTranzactie.CREDIT, "RO01AAA"),
            new Tranzactie(4, 1200.00, "2026-02-18", TipTranzactie.DEBIT,  "RO03CCC"),
            new Tranzactie(5, 3000.00, "2026-03-10", TipTranzactie.CREDIT, "RO02BBB"),
            new Tranzactie(6, 450.00,  "2026-01-05", TipTranzactie.DEBIT,  "RO01AAA"),
            new Tranzactie(7, 89.90,   "2026-02-20", TipTranzactie.DEBIT,  "RO04DDD"),
            new Tranzactie(8, 620.00,  "2026-03-14", TipTranzactie.CREDIT, "RO03CCC"),
            new Tranzactie(9, 150.00,  "2026-03-25", TipTranzactie.DEBIT,  "RO01AAA"),
            new Tranzactie(10, 95.00,  "2026-02-28", TipTranzactie.CREDIT, "RO02BBB")
        );

        System.out.println(" API:   \n");

        // 1. filter(tip == CREDIT)
        System.out.println("1. Lista tuturor tranzactiilor CREDIT:");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);
        System.out.println();


        // 2. mapToDouble(suma).sum()
        System.out.println("2. Total procesat:");
        double totalSuma = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.format(Locale.US, "Total procesat: %.2f RON%n%n", totalSuma);


        // 3. Collectors.groupingBy(luna, summingDouble(suma))
        System.out.println("3. Volumul total de tranzactii per luna (Sortat cronologic):");
        Map<String, Double> totalPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        Tranzactie::getLuna,
                        TreeMap::new, // TreeMap asigură sortarea cheilor (yyyy-MM)
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));
        totalPerLuna.forEach((luna, suma) -> 
                System.out.format(Locale.US, "%s: %.2f RON%n", luna, suma));
        System.out.println();


        // 4. sorted(comparingDouble.reversed()).limit(3)
        System.out.println("4. Top 3 tranzactii cu valorile cele mai mari:");
        System.out.println("Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);
        System.out.println();


        // 5. map(contSursa).distinct().collect(toList())
        System.out.println("5. Identificare conturi sursa unice:");
        List<String> conturiUnice = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiUnice);
        System.out.println();


        // 6. mapToDouble(suma).average()
        System.out.println("6. Valoarea medie a unei tranzacții:");
        double medieSuma = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.format(Locale.US, "Suma medie: %.2f RON%n%n", medieSuma);


        // 7. Collectors.groupingBy(luna) cu format extras extins
        System.out.println("7. Generare Extrase de cont lunare:");
        Map<String, List<Tranzactie>> grupatePeLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(Tranzactie::getLuna, TreeMap::new, Collectors.toList()));

        grupatePeLuna.forEach((luna, listaDinLuna) -> {
            int nrTranzactii = listaDinLuna.size();
            double totalLuna = listaDinLuna.stream()
                    .mapToDouble(Tranzactie::getSuma)
                    .sum();
            System.out.format(Locale.US, "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n", 
                    luna, nrTranzactii, totalLuna);
        });
    }
}