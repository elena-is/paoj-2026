package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public class Main {

    public static void main(String[] args) {
        // Generam date de test
        List<Transaction> transactions = List.of(
            new Transaction(1, new BigDecimal("5000.00"), LocalDate.of(2026, 5, 1), "RO", "WEB"),
            new Transaction(2, new BigDecimal("120.50"),  LocalDate.of(2026, 5, 1), "RU", "ATM"),
            new Transaction(3, new BigDecimal("5000.00"), LocalDate.of(2026, 5, 2), "NG", "APP"), // Suma egala cu tx 1
            new Transaction(4, new BigDecimal("75.00"),   LocalDate.of(2026, 5, 3), "RO", "POS"),
            new Transaction(5, new BigDecimal("1200.00"), LocalDate.of(2026, 5, 4), "RO", "CRYPTO"),
            new Transaction(6, new BigDecimal("300.00"),  LocalDate.of(2026, 5, 5), "NL", "WEB")
        );

        System.out.println("--- Se genereaza snapshot-ul analitic (Top 3) ---");
        // Colectam stream-ul in snapshot-ul nostru imutabil
        Snapshot snap = transactions.stream().collect(CustomCollectors.toSnapshot(3));
        System.out.println("Snapshot creat cu succes!\n");

        // Interogarea 1: Afisarea top-ului tranzactiilor stocate in snapshot
        System.out.println("INTEROGAREA 1: Top 3 Tranzactii dupa suma (desc) si ID (cresc)");
        snap.getTopTransactions().forEach(tx -> 
            System.out.printf("  [ID: %d] Suma: %s | Tara: %s | Canal: %s%n", 
                    tx.getId(), tx.getAmount(), tx.getCountry(), tx.getChannel())
        );
        System.out.println();

        // Interogarea 2: Volumul de tranzactii pe tari ordonate desc dupa numar
        System.out.println("INTEROGAREA 2: Clasament Tari dupa numarul de tranzactii: ");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.printf("  Tara: %s -> %d tranzactii%n", e.getKey(), e.getValue()));
        System.out.println();

        // Interogarea 3: Distributia tranzactiilor pe canale si impactul financiar total
        System.out.println("INTEROGAREA 3: Statistici Generale: ");
        System.out.println("  Canale active detectate in snapshot:");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> System.out.printf("    - %s: %d utilizari%n", e.getKey(), e.getValue()));
        System.out.printf("  Suma totala rulata in acest snapshot: %s RON%n", snap.getTotalAmount());
        
        try {
            snap.getTopTransactions().clear();
        } catch (UnsupportedOperationException e) {
            System.out.println("Snapshotul e imutabil😛");
        }
    }

    // 1) Modelul de date imutabil Transaction
    public static final class Transaction {
        private final int id;
        private final BigDecimal amount;
        private final LocalDate date;
        private final String country;
        private final String channel;

        public Transaction(int id, BigDecimal amount, LocalDate date, String country, String channel) {
            this.id = id;
            this.amount = amount;
            this.date = date;
            this.country = country;
            this.channel = channel;
        }

        public int getId() { return id; }
        public BigDecimal getAmount() { return amount; }
        public LocalDate getDate() { return date; }
        public String getCountry() { return country; }
        public String getChannel() { return channel; }
    }

    // 2) Snapshot-ul Analitic Imutabil 
    public static final class Snapshot {
        private final Map<String, Long> countByCountry;
        private final Map<String, Long> countByChannel;
        private final BigDecimal totalAmount;
        private final List<Transaction> topTransactions;

        public Snapshot(Map<String, Long> byCountry, Map<String, Long> byChannel, BigDecimal total, List<Transaction> top) {
            // Definim copii ne-modificabile
            this.countByCountry = Collections.unmodifiableMap(new HashMap<>(byCountry));
            this.countByChannel = Collections.unmodifiableMap(new HashMap<>(byChannel));
            this.totalAmount = total;
            this.topTransactions = List.copyOf(top); 
        }

        public Map<String, Long> getCountByCountry() { return countByCountry; }
        public Map<String, Long> getCountByChannel() { return countByChannel; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public List<Transaction> getTopTransactions() { return topTransactions; }
    }

    // 3) Implementarea Colectorului Custom 
    public static class CustomCollectors {
        
        public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
            
            // Containerul local mutabil folosit in timpul acumularii stream-ului
            class Aggregator {
                final Map<String, Long> byCountry = new HashMap<>();
                final Map<String, Long> byChannel = new HashMap<>();
                BigDecimal total = BigDecimal.ZERO;
                final List<Transaction> allTxs = new ArrayList<>();

                void accumulate(Transaction tx) {
                    byCountry.put(tx.getCountry(), byCountry.getOrDefault(tx.getCountry(), 0L) + 1);
                    byChannel.put(tx.getChannel(), byChannel.getOrDefault(tx.getChannel(), 0L) + 1);
                    total = total.add(tx.getAmount());
                    allTxs.add(tx);
                }

                Aggregator combine(Aggregator other) {
                    other.byCountry.forEach((k, v) -> this.byCountry.put(k, this.byCountry.getOrDefault(k, 0L) + v));
                    other.byChannel.forEach((k, v) -> this.byChannel.put(k, this.byChannel.getOrDefault(k, 0L) + v));
                    this.total = this.total.add(other.total);
                    this.allTxs.addAll(other.allTxs);
                    return this;
                }

                Snapshot finish() {
                    // Calculam top-ul determinist in finisher inainte de a sigila datele in Snapshot
                    List<Transaction> top = allTxs.stream()
                            .sorted((tx1, tx2) -> {
                                int comp = tx2.getAmount().compareTo(tx1.getAmount()); 
                                if (comp != 0) return comp;
                                return Integer.compare(tx1.getId(), tx2.getId()); // ID cresc la sume egale
                            })
                            .limit(topN)
                            .toList();

                    return new Snapshot(byCountry, byChannel, total, top);
                }
            }

            return Collector.of(
                    Aggregator::new,          // 1. Supplier: creeaza containerul mutabil
                    Aggregator::accumulate,   // 2. Accumulator: adauga fiecare element in container
                    Aggregator::combine,      // 3. Combiner: uneste doua containere (pentru executie in paralel)
                    Aggregator::finish        // 4. Finisher: transforma containerul mutabil in Snapshot-ul imutabil final
            );
        }
    }
}