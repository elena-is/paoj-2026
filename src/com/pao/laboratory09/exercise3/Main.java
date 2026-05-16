package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) {
        CoadaTranzactii banda = new CoadaTranzactii();

        // 1. Creeaza 3 instante ATMThread si un ProcessorThread
        ATMThread atm1 = new ATMThread(1, banda);
        ATMThread atm2 = new ATMThread(2, banda);
        ATMThread atm3 = new ATMThread(3, banda);

        ProcessorThread processorRunnable = new ProcessorThread(banda);
        Thread processorThread = new Thread(processorRunnable);

        // 2 & 3. Porneste toti producatorii si consumatorul
        System.out.println("--- Se porneste procesatorul asincron ---");
        atm1.start();
        atm2.start();
        atm3.start();
        processorThread.start();

        try {
            // 4. Fa join() pe toti cei 3 producatori
            atm1.join();
            atm2.join();
            atm3.join();
            System.out.println("--- Toate ATM-urile au terminat de trimis! ---");

            // 5. Seteaza activ = false si notifica banda pentru oprirea consumatorului
            processorRunnable.activ = false;
            synchronized (banda) {
                banda.notifyAll();
            }

            // 6. Fa join() pe firul consumatorului
            processorThread.join();

            // 7. Afiseaza mesajul final
            System.out.println("Toate tranzactiile procesate. Total: 12");

        } catch (InterruptedException e) {
            System.err.println("Firul principal a fost intrerupt: " + e.getMessage());
        }
    }
}