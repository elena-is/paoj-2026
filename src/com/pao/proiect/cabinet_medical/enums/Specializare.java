package com.pao.proiect.cabinet_medical.enums;

public enum Specializare {
    OBSTETRICA("Obstetrica"),
    MEDICINA_MATERNO_FETALA("Medicina Materno-Fetala");

    private final String numeAfisat;
    //ca sa putem afisa si numele mai dragut
    Specializare(String numeAfisat) {
        this.numeAfisat = numeAfisat;
    }

    public String getNumeAfisat() {
        return numeAfisat;
    }
}