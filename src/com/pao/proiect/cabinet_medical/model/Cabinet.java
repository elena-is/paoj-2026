package com.pao.proiect.cabinet_medical.model;

import com.pao.proiect.cabinet_medical.enums.Specializare;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cabinet {
    private int id;
    private int numarSala;
    private int etaj;
    private Specializare specializare; 
    private List<Echipament> echipamente;
    private List<LocalDateTime> igienizare;

    public Cabinet(int id, int numarSala, int etaj, Specializare specializare) {
        this.id = id;
        this.numarSala = numarSala;
        this.etaj = etaj;
        this.specializare = specializare;
        
        this.echipamente = new ArrayList<>();
        this.igienizare = new ArrayList<>();
    }

    //Adauga echipament si igienizare
    public void adaugaEchipament(Echipament echipament) {
        this.echipamente.add(echipament);
    }

    public void inregistreazaIgienizare(LocalDateTime dataOra) {
        this.igienizare.add(dataOra);
    }

    // Gettere si Settere
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumarSala() { return numarSala; }
    public void setNumarSala(int numarSala) { this.numarSala = numarSala; }

    public int getEtaj() { return etaj; }
    public void setEtaj(int etaj) { this.etaj = etaj; }

    public Specializare getSpecializare() { return specializare; }
    public void setSpecializare(Specializare specializare) { this.specializare = specializare; }

    public List<Echipament> getEchipamente() { return echipamente; }
    public List<LocalDateTime> getIgienizari() { return igienizare; }

    @Override
    public boolean equals(Object o) {
        
        if (this == o) return true;
        
        if (o == null || getClass() != o.getClass()) return false;
        
        Cabinet cabinet = (Cabinet) o;
        return java.util.Objects.equals(numarSala, cabinet.numarSala);
        }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(numarSala);
    }
    
    
}