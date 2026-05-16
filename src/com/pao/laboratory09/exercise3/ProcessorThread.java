package com.pao.laboratory09.exercise3;

public class ProcessorThread implements Runnable {
    private final CoadaTranzactii banda;
    public volatile boolean activ = true;

    public ProcessorThread(CoadaTranzactii banda) {
        this.banda = banda;
    }

    @Override
    public void run() {
        try {
            while (activ || !banda.isEmpty()) {
                try {
                    Tranzactie t = null;
                    
                    synchronized (banda) {
                        if (banda.isEmpty() && !activ) {
                            break;
                        }
                        if (banda.isEmpty()) {
                            banda.wait(100);
                            if (banda.isEmpty()) continue;
                        }
                        t = banda.extrage();
                    }

                    if (t != null) {
                        System.out.printf("[Processor] Factura #%d - %.2f RON | %s%n", 
                                t.getId(), t.getSuma(), t.getData());
                        Thread.sleep(80);
                    }
                } catch (InterruptedException e) {
                    if (!activ && banda.isEmpty()) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[Processor] Eroare: " + e.getMessage());
        }
    }
}