package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Tranzactie> listaTranzactii = new ArrayList<>();

        if (!scanner.hasNextInt()) {
            scanner.close();
            return;
        }

        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = Double.parseDouble(scanner.next());
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            listaTranzactii.add(new Tranzactie(id, suma, data, tip));
        }

        // Citire comenzi până la EOF
        while (scanner.hasNext()) {
            String comanda = scanner.next();

            switch (comanda) {
                case "UNIQUE_IDS": {
                    Set<Integer> uniqueIds = new LinkedHashSet<>();
                    for (Tranzactie t : listaTranzactii) {
                        uniqueIds.add(t.getId());
                    }
                    System.out.println("IDs unice (" + uniqueIds.size() + "): " + uniqueIds);
                    break;
                }
                case "MONTHLY_REPORT": {
                    Map<String, double[]> raport = new TreeMap<>();

                    for (Tranzactie t : listaTranzactii) {
                        if (t.getData() != null && t.getData().length() >= 7) {
                            String luna = t.getData().substring(0, 7); // yyyy-MM
                            raport.putIfAbsent(luna, new double[2]);
                            
                            if (t.getTip() == TipTranzactie.CREDIT) {
                                raport.get(luna)[0] += t.getSuma();
                            } else if (t.getTip() == TipTranzactie.DEBIT) {
                                raport.get(luna)[1] += t.getSuma();
                            }
                        }
                    }

                    for (Map.Entry<String, double[]> entry : raport.entrySet()) {
                        System.out.format(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
                    }
                    break;
                }
                case "TOP": {
                    int topN = scanner.nextInt();
                    System.out.println("Top " + topN + ":");
                    
                    
                    List<Tranzactie> copie = new ArrayList<>(listaTranzactii);
                    copie.sort((t1, t2) -> Double.compare(t2.getSuma(), t1.getSuma()));

                    
                    int limita = Math.min(topN, copie.size());
                    List<Tranzactie> subLista = copie.subList(0, limita);
                    for (Tranzactie t : subLista) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SORT_ASC": {
                    listaTranzactii.sort(Comparator.comparingDouble(Tranzactie::getSuma));
                    
                    for (Tranzactie t : listaTranzactii) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SORT_DESC": {
                    listaTranzactii.sort((t1, t2) -> Double.compare(t2.getSuma(), t1.getSuma()));
                    for (Tranzactie t : listaTranzactii) {
                        System.out.println(t);
                    }
                    break;
                }
                case "REVERSE": {
                    Collections.reverse(listaTranzactii);
                    for (Tranzactie t : listaTranzactii) {
                        System.out.println(t);
                    }
                    break;
                }
                case "MIN_MAX": {
                    if (!listaTranzactii.isEmpty()) {
                        Tranzactie min = Collections.min(listaTranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                        Tranzactie max = Collections.max(listaTranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                        System.out.println("MIN: " + min);
                        System.out.println("MAX: " + max);
                    }
                    break;
                }
                case "CME_DEMO": {
                    try {
                        
                        List<Tranzactie> demoList = new ArrayList<>(listaTranzactii);
                        for (Tranzactie t : demoList) {
                            demoList.remove(t); 
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
                }
                default:
                    break;
            }
        }
        scanner.close();
    }
}