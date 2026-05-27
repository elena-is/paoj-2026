package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (scanner.hasNext()) {
            String comanda = scanner.next();

            switch (comanda) {
                case "ENQUEUE": {
                    int id = scanner.nextInt();
                    double suma = Double.parseDouble(scanner.next());
                    String data = scanner.next();
                    TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                    
                    coada.addLast(new Tranzactie(id, suma, data, tip));
                    break;
                }
                case "DEQUEUE": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = coada.removeFirst();
                        System.out.println("Procesat: " + t);
                    }
                    break;
                }
                case "PUSH": {
                    int id = scanner.nextInt();
                    double suma = Double.parseDouble(scanner.next());
                    String data = scanner.next();
                    TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                    
                    coada.addFirst(new Tranzactie(id, suma, data, tip));
                    break;
                }
                case "POP": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = coada.removeFirst();
                        System.out.println("Extras: " + t);
                    }
                    break;
                }
                case "REMOVE_DEBIT": {
                    int contor = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        if (itr.next().getTip() == TipTranzactie.DEBIT) {
                            itr.remove();
                            contor++;
                        }
                    }
                    System.out.println("Eliminat " + contor + " tranzactii DEBIT.");
                    break;
                }
                case "REMOVE_BELOW": {
                    double threshold = Double.parseDouble(scanner.next());
                    int contor = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        if (itr.next().getSuma() < threshold) {
                            itr.remove();
                            contor++;
                        }
                    }
                    // Formatăm și threshold-ul în mesaj cu 2 zecimale, conform exemplului din text
                    System.out.format(Locale.US, "Eliminat %d tranzactii sub %.2f RON.%n", contor, threshold);
                    break;
                }
                case "PRINT": {
                    for (Tranzactie t : coada) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SIZE": {
                    System.out.println("Dimensiune coada: " + coada.size());
                    break;
                }
                default:
                    break;
            }
        }
        scanner.close();
    }
}