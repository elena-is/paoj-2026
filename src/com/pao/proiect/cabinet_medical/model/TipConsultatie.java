package com.pao.proiect.cabinet_medical.model;

import com.pao.proiect.cabinet_medical.enums.Specializare;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TipConsultatie {
    private int id;
    private Specializare specializare;
    private String nume;
    private double pret;
    private int durataMinute;

    // Dictionar - Echipament: lista de sali unde se gaseste
    private Map<Echipament, List<Cabinet>> echipamenteNecesare;

    public TipConsultatie(int id, Specializare specializare, String nume, double pret, int durataMinute) {
        this.id = id;
        this.specializare = specializare;
        this.nume = nume;
        this.pret = pret;
        this.durataMinute = durataMinute;
        this.echipamenteNecesare= new HashMap<>();
    }

    //Adauga echipament necesar si in ce sali il gasim
    public void adaugaLogistica(Echipament echipament, List<Cabinet> saliDisponibile) {
        this.echipamenteNecesare.put(echipament, saliDisponibile);
    }

    // Gettere si Settere
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Specializare getSpecializare() { return specializare; }
    public void setSpecializare(Specializare s) { this.specializare = s; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public double getPret() { return pret; }
    public void setPret(double pret) { this.pret = pret; }

    public int getDurataMinute() { return durataMinute; }
    public void setDurataMinute(int durata) { this.durataMinute = durata; }

    public Map<Echipament, List<Cabinet>> getEchipamenteNecesare() {
        return echipamenteNecesare;
    }
}