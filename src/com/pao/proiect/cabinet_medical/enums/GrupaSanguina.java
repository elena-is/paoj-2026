package com.pao.proiect.cabinet_medical.enums;

public enum GrupaSanguina {
    A_POZITIV("A+"), A_NEGATIV("A-"),
    B_POZITIV("B+"), B_NEGATIV("B-"),
    AB_POZITIV("AB+"), AB_NEGATIV("AB-"),
    O_POZITIV("0+"), O_NEGATIV("0-");

    private final String simbol;

    GrupaSanguina(String simbol) {
        this.simbol = simbol;
    }

    public String getSimbol() {
        return simbol;
    }
}