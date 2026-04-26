package com.pao.proiect.cabinet_medical.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reteta {
    private int id;
    private Pacient pacient;
    private Medic medic;
    private Programare programare; 
    private LocalDate dataEmitere;
    
    private List<String> recomandari; // Sfaturi generale
    private Map<Medicament, String> detaliiMedicamente; // Medicament -> Mod administrare: interval, durata, cand se ia, etc

    public Reteta(int id, Pacient pacient, Medic medic, Programare programare, LocalDate dataEmitere) {
        this.id = id;
        this.pacient = pacient;
        this.medic = medic;
        this.programare = programare;
        this.dataEmitere = dataEmitere;
        this.recomandari = new ArrayList<>();
        this.detaliiMedicamente = new HashMap<>();
    }

    // Adaugari
    public void adaugaMedicament(Medicament m, String instructiuni) {
        this.detaliiMedicamente.put(m, instructiuni);
    }

    public void adaugaRecomandare(String sfat) {
        this.recomandari.add(sfat);
    }

    // Gettere; de settere nu ar trebui sa avem nevoie
    public int getId() { return id; }
    public Pacient getPacient() { return pacient; }
    public Medic getMedic() { return medic; }
    public Programare getProgramare() { return programare; }
    public LocalDate getDataEmitere() { return dataEmitere; }
    public List<String> getRecomandari() { return recomandari; }
    public Map<Medicament, String> getDetaliiMedicamente() { return detaliiMedicamente; }

    public void setRecomandari(List<String> recomandari) {
        this.recomandari = recomandari;
    }
    
    public void setDetaliiMedicamente(Map<Medicament, String> detalii) {
        this.detaliiMedicamente = detalii;
    }
}