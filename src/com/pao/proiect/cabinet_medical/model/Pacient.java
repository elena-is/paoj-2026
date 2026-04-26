package com.pao.proiect.cabinet_medical.model;

import com.pao.proiect.cabinet_medical.enums.GrupaSanguina;
import java.util.ArrayList;
import java.util.List;

public class Pacient extends Persoana {
    private List<String> alergii;
    private List<String> boliCronice;
    private GrupaSanguina grupaSanguina;
    private String numarAsigurare;

    public Pacient(int id, String nume, String prenume, String cnp, String email, String telefon, 
                   GrupaSanguina grupaSanguina, String numarAsigurare) {
        super(id, nume, prenume, cnp, email, telefon);
        this.grupaSanguina = grupaSanguina;
        this.numarAsigurare = numarAsigurare;
        this.alergii = new ArrayList<>();
        this.boliCronice = new ArrayList<>();
    }

    @Override
    public String getTipPersoana() {
        return "Pacient - CNP: " + this.cnp;
    }

    //Adauga alergii si boli cronice
    public void adaugaAlergie(String alergie) {
        this.alergii.add(alergie);
    }

    public void adaugaBoalaCronica(String boala) {
        this.boliCronice.add(boala);
    }

    public List<String> getAlergii() { return alergii; }
    public void setAlergii(List<String> alergii) { this.alergii = alergii; }

    public List<String> getBoliCronice() { return boliCronice; }
    public void setBoliCronice(List<String> boliCronice) { this.boliCronice = boliCronice; }

    public GrupaSanguina getGrupaSanguina() { return grupaSanguina; }
    public void setGrupaSanguina(GrupaSanguina grupaSanguina) { this.grupaSanguina = grupaSanguina; }

    public String getNumarAsigurare() { return numarAsigurare; }
    public void setNumarAsigurare(String numarAsigurare) { this.numarAsigurare = numarAsigurare; }
}