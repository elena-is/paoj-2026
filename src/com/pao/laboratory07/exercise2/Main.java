package com.pao.laboratory07.exercise2;

import java.util.*;
import com.pao.laboratory07.exercise1.OrderState;


public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();
        
        int nrStandard = 0, nrDiscounted = 0, nrGift = 0;
        double sumaStandard = 0, sumaDiscounted = 0;

        for (int i = 0; i < n; i++) {
            if (!sc.hasNextLine()) break;
            String line = sc.nextLine().trim();
            String[] tokens = line.split(" ");
            
            if (tokens[0].equals("STANDARD")) {
                comenzi.add(new ComandaStandard(tokens[1], Double.parseDouble(tokens[2])));
                nrStandard++;
                sumaStandard += comenzi.get(comenzi.size() - 1).pretFinal();
            } else if (tokens[0].equals("DISCOUNTED")) {
                comenzi.add(new ComandaRedusa(tokens[1], Double.parseDouble(tokens[2]), Integer.parseInt(tokens[3])));
                nrDiscounted++;
                sumaDiscounted += comenzi.get(comenzi.size() - 1).pretFinal();
            } else if (tokens[0].equals("GIFT")) {
                comenzi.add(new ComandaGratuita(tokens[1]));
                nrGift++;
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }

        System.out.println();
        System.out.println("Statistici:");
        if (nrStandard > 0)
            System.out.printf(Locale.US, "STANDARD: suma = %.2f lei, numar = %d\n", sumaStandard, nrStandard);
        if (nrDiscounted > 0)
            System.out.printf(Locale.US, "DISCOUNTED: suma = %.2f lei, numar = %d\n", sumaDiscounted, nrDiscounted);
        if (nrGift > 0)
            System.out.printf(Locale.US, "GIFT: suma = 0.00 lei, numar = %d\n", nrGift);
            
        System.out.printf(Locale.US, "Total platit: %.2f lei\n", sumaStandard + sumaDiscounted);
    }
}