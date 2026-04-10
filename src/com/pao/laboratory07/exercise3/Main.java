package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.Collectors;

import com.pao.laboratory07.exercise1.OrderState;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        
        if (!sc.hasNextInt()) return;
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] t = sc.nextLine().split(" ");
            switch (t[0]) {
                case "STANDARD" -> comenzi.add(new ComandaStandard(t[1], Double.parseDouble(t[2]), t[3]));
                case "DISCOUNTED" -> comenzi.add(new ComandaRedusa(t[1],  t[4], Double.parseDouble(t[2]),Integer.parseInt(t[3])));
                case "GIFT" -> comenzi.add(new ComandaGratuita(t[1], t[2]));
            }
            System.out.println(comenzi.get(comenzi.size() - 1).descriere());
        }

        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.isEmpty()) continue;
            String[] t = line.split(" ");
            String cmd = t[0];

            switch (cmd) {
                case "STATS" -> {
                    System.out.println("\n--- STATS ---");
                    Map<String, Double> medii = comenzi.stream()
                        .collect(Collectors.groupingBy(
                            c -> c instanceof ComandaStandard ? "STANDARD" : 
                                 c instanceof ComandaRedusa ? "DISCOUNTED" : "GIFT",
                            Collectors.averagingDouble(Comanda::pretFinal)
                        ));
                    medii.forEach((tip, medie) -> System.out.printf("%s: medie = %.2f lei\n", tip, medie));
                }
                case "FILTER" -> {
                    double threshold = Double.parseDouble(t[1]);
                    System.out.printf("\n FILTER (>= %.2f) \n", threshold);
                    comenzi.stream()
                        .filter(c -> c.pretFinal() >= threshold)
                        .forEach(c -> System.out.println(c.descriere()));
                }
                case "SORT" -> {
                    System.out.println("\n SORT (by client, then by pret) ");
                    comenzi.stream()
                        .sorted(Comparator.comparing(Comanda::getClient)
                                          .thenComparing(Comanda::pretFinal))
                        .forEach(c -> System.out.println(c.descriere()));
                }
                case "SPECIAL" -> {
                    System.out.println("\n SPECIAL (discount > 15%) ");
                    comenzi.stream()
                        .filter(c -> c instanceof ComandaRedusa cr && cr.getDiscountProcent() > 15)
                        .forEach(c -> System.out.println(c.descriere()));
                }
                case "QUIT" -> { return; }
            }
        }
    }
}