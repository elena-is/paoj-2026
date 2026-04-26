package com.pao.proiect.cabinet_medical.model;

import java.time.LocalDateTime;

public class Programare implements Comparable<Programare>{
    private int id; 
    private Medic medic;
    private Pacient pacient;
    private TipConsultatie tipConsultatie;
    private Cabinet cabinet; 
    private LocalDateTime dataOra; 
    private int durataEstimata; // Ora la care se estimeaza ca se termina
    //Daca e = 0 se considera ora din TipConsultatie default, daca nu inseamna ca e o consultatie speciala 
    //si are o durata diferita

    public Programare(int id, Medic medic, Pacient pacient, TipConsultatie tipConsultatie, 
                      Cabinet cabinet, LocalDateTime dataOra, int durataEstimata) {
        this.id = id;
        this.medic = medic;
        this.pacient = pacient;
        this.tipConsultatie = tipConsultatie;
        this.cabinet = cabinet;
        this.dataOra = dataOra;
        this.durataEstimata = durataEstimata;
    }

    @Override
    public int compareTo(Programare other) {
        // Sortam cronologic folosind data si ora
        return this.dataOra.compareTo(other.getDataOra());
    }

    //Gettere si Settere

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Medic getMedic() { return medic; }
    public void setMedic(Medic medic) { this.medic = medic; }

    public Pacient getPacient() { return pacient; }
    public void setPacient(Pacient pacient) { this.pacient = pacient; }

    public TipConsultatie getTipConsultatie() { return tipConsultatie; }
    public void setTipConsultatie(TipConsultatie tip) { this.tipConsultatie = tip; }

    public Cabinet getCabinet() { return cabinet; }
    public void setCabinet(Cabinet cabinet) { this.cabinet = cabinet; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public int getDurataEstimata() { return durataEstimata; }
    public void setDurataEstimata(int ora) { this.durataEstimata = ora; }

    @Override
    public String toString() {
        return "Programare: " + id + " | " + dataOra + " | Pacient: " + pacient.getNume() + 
               " | Medic: " + medic.getNume() + " | Sala: " + cabinet.getNumarSala();
    }


}