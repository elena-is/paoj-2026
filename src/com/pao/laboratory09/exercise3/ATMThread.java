package com.pao.laboratory09.exercise3;

import java.util.Random;

public class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii banda;
    private static int idSecvential = 1;

    public ATMThread(int atmId, CoadaTranzactii banda) {
        this.atmId = atmId;
        this.banda = banda;
    }

    private static synchronized int genereazaId() {
        return idSecvential++;
    }

    @Override
    public void run() {
        Random random = new Random();
        try {
            for (int i = 0; i < 4; i++) {
                int id = genereazaId();
                double suma = 100 + random.nextDouble() * 900;
                Tranzactie t = new Tranzactie(id, suma, "2026-05-16");

                banda.adauga(t, atmId);
                System.out.printf("[ATM-%d] trimite: Tranzactie #%d %.2f RON%n", atmId, id, suma);
                
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("[ATM-" + atmId + "] a fost intrerupt.");
        }
    }
}