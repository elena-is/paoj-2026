package com.pao.proiect.cabinet_medical.model;

import java.time.LocalTime;
//clasa imutabila

public final class IntervalOrar {
    private final LocalTime start;
    private final LocalTime sfarsit;

    public IntervalOrar(LocalTime start, LocalTime sfarsit) {
        this.start = start;
        this.sfarsit = sfarsit;
    }

    public LocalTime getStart() { return start; }
    public LocalTime getSfarsit() { return sfarsit; }
    
    @Override
    public String toString() {
        return start + " - " + sfarsit;
    }

}