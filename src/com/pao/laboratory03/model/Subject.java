package com.pao.laboratory03.model;

// * 1. model/Subject.java — ENUM
// *    - Constante: PAOJ, BD, SO, RC (sau alte materii)
// *    - Câmpuri: String fullName, int credits
// *    - Constructor privat, getteri
// *    - toString() → "PAOJ (Programare Avansată pe Obiecte, 6 credite)"

public enum Subject {

    PAOJ("Programare Avansata pe Obiecte", 6),
    BD("Baze de Date", 5),
    SO("Sisteme de Operare", 5),
    RC("Retele de Calculatoare", 4);

    private final String fullName;
    private final int credits;

    private Subject(String fullName, int credits) {
        this.fullName = fullName;
        this.credits = credits;
    }

    public String getFullName() {
        return fullName;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d credite)", name(), fullName, credits);
    }
}