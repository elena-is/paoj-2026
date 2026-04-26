package com.pao.proiect.cabinet_medical.model;

import java.time.LocalDate;

public class Echipament {
    private int id;
    private String nume;
    private String codIdentificare;
    private LocalDate termenRevizie;

    public Echipament(int id, String nume, String codIdentificare, LocalDate termenRevizie) {
        this.id = id;
        this.nume = nume;
        this.codIdentificare = codIdentificare;
        this.termenRevizie = termenRevizie;
    }

    //E revizia la zi?
    public boolean esteValid() {
        return LocalDate.now().isBefore(this.termenRevizie);
    }

    // Gettere si Settere
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getCodIdentificare() { return codIdentificare; }
    public void setCodIdentificare(String cod) { this.codIdentificare = cod; }

    public LocalDate getTermenRevizie() { return termenRevizie; }
    public void setTermenRevizie(LocalDate termen) { this.termenRevizie = termen; }

    @Override
    public String toString() {
        return "Echipament: " + nume + " [" + codIdentificare + "] - Revizie pana la: " + termenRevizie;
    }
}