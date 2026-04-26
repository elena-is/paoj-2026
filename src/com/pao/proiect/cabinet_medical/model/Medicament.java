package com.pao.proiect.cabinet_medical.model;

import java.util.ArrayList;
import java.util.List;

public class Medicament {
    private int id;
    private String nume;
    private String cod;
    private double pret;
    private int stocDisponibil;
    private boolean necesitaReteta;
    private List<String> substanteActive;

    public Medicament(int id, String nume, String cod, double pret, int stocDisponibil, boolean necesitaReteta) {
        this.id = id;
        this.nume = nume;
        this.cod = cod;
        this.pret = pret;
        this.stocDisponibil = stocDisponibil;
        this.necesitaReteta = necesitaReteta;
        this.substanteActive = new ArrayList<>();
    }

    // Stoc
    public void actualizeazaStoc(int cantitate) {
        this.stocDisponibil += cantitate;
    }

    public boolean areStocSuficient(int necesar) {
        return this.stocDisponibil >= necesar;
    }

    // Adaugarea unei substante active
    public void adaugaSubstantaActive(String substanta) {
        this.substanteActive.add(substanta);
    }

    // Gettere si Settere
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getCod() { return cod; }
    public void setCod(String cod) { this.cod = cod; }

    public double getPret() { return pret; }
    public void setPret(double pret) { this.pret = pret; }

    public int getStocDisponibil() { return stocDisponibil; }
    public void setStocDisponibil(int stoc) { this.stocDisponibil = stoc; }

    public boolean isNecesitaReteta() { return necesitaReteta; }
    public void setNecesitaReteta(boolean necesita) { this.necesitaReteta = necesita; }

    public List<String> getSubstanteActive() { return substanteActive; }
    public void setSubstanteActive(List<String> substante) { this.substanteActive = substante; }

    @Override
    public String toString() {
        return "Medicament: " + nume + " | Cod: " + cod + " | Stoc: " + stocDisponibil + 
               " | Reteta: " + (necesitaReteta ? "DA" : "NU");
    }
}