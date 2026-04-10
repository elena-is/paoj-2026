package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected String client;
    protected OrderState stare;

    public Comanda(String nume, String client) {
        this.nume = nume;
        this.client = client;
        this.stare = OrderState.PLACED;
    }

    public abstract double pretFinal();
    public abstract String descriere();

    public String getClient() {
        return this.client;
    }
} 

final class ComandaStandard extends Comanda {
    private double pret;

    public ComandaStandard(String nume, double pret, String client) {
        super(nume, client);
        this.pret = pret;
    }

    @Override
    public double pretFinal() {
        return pret;
    }

    @Override
    public String descriere() {
        return String.format("STANDARD: %s, pret: %.2f lei [%s]", nume, pretFinal(), stare);
    }
}

final class ComandaRedusa extends Comanda {
    private double pret;
    private int discountProcent;

    public ComandaRedusa(String nume, String client, double pret, int discountProcent) {
        super(nume, client);
        this.pret = pret;
        this.discountProcent = discountProcent;
    }

    public int getDiscountProcent() {
        return this.discountProcent;
    }

    @Override
    public double pretFinal() {
        return pret * (1 - discountProcent / 100.0);
    }

    @Override
    public String descriere() {
        return String.format("DISCOUNTED: %s, pret: %.2f lei (-%d%%) [%s]", nume, pretFinal(), discountProcent, stare);
    }
}

final class ComandaGratuita extends Comanda {
    public ComandaGratuita(String nume, String client) {
        super(nume, client);
    }

    @Override
    public double pretFinal() {
        return 0.0;
    }

    @Override
    public String descriere() {
        return String.format("GIFT: %s, gratuit [%s]", nume, stare);
    }
}