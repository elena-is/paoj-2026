package com.pao.proiect.cabinet_medical.model;

import com.pao.proiect.cabinet_medical.enums.MetodaPlata;
import com.pao.proiect.cabinet_medical.enums.StatutPlata;
import java.time.LocalDateTime;

public class Factura {
    private int id;
    private String serieNumar; 
    private Programare programare;
    private Pacient pacient;
    private LocalDateTime dataOra;
    
    private double valoareTotala;           
    private double procentAcoperitAsigurare; // Ex: 80.0 pentru 80%
    private double sumaDecontataAsigurare;   // Calculat: valoareTotala * (procent/100)
    private double sumaDiferentaPacient;     // Calculat: valoareTotala - sumaDecontata
    
    private MetodaPlata metodaPlata;
    private StatutPlata statutPlata;

    public Factura(int id, String serieNumar, Programare programare, 
                   double procentAsigurare, MetodaPlata metodaPlata) {
        this.id = id;
        this.serieNumar = serieNumar;
        this.programare = programare;
        this.pacient = programare.getPacient(); // Il luam direct din programare
        this.dataOra = LocalDateTime.now();
        this.metodaPlata = metodaPlata;
        this.statutPlata = StatutPlata.NEACHITAT;
        
        this.valoareTotala = programare.getTipConsultatie().getPret();
        this.procentAcoperitAsigurare = procentAsigurare;
        this.sumaDecontataAsigurare = (this.valoareTotala * procentAsigurare) / 100;
        this.sumaDiferentaPacient = this.valoareTotala - this.sumaDecontataAsigurare;
    }

    
    public void achitaFactura() {
        this.statutPlata = StatutPlata.ACHITAT;
    }

    //Recalculam sumele daca s-a schimbat ceva!!
    private void recalculareSume() {
        this.sumaDecontataAsigurare = (this.valoareTotala * this.procentAcoperitAsigurare) / 100;
        this.sumaDiferentaPacient = this.valoareTotala - this.sumaDecontataAsigurare;
    }

    //Gettere si Settere
    public int getId() { return id; }
    public String getSerieNumar() { return serieNumar; }
    public Programare getProgramare() { return programare; }
    public Pacient getPacient() { return pacient; }
    public LocalDateTime getDataOra() { return dataOra; }
    public double getValoareTotala() { return valoareTotala; }
    public double getProcentAcoperitAsigurare() { return procentAcoperitAsigurare; }
    public double getSumaDecontataAsigurare() { return sumaDecontataAsigurare; }
    public double getSumaDiferentaPacient() { return sumaDiferentaPacient; }
    public MetodaPlata getMetodaPlata() { return metodaPlata; }
    public StatutPlata getStatutPlata() { return statutPlata; }


    public void setId(int id) { this.id = id; }
    
    public void setSerieNumar(String serieNumar) { this.serieNumar = serieNumar; }
    
    public void setProgramare(Programare programare) { 
        this.programare = programare; 
        this.pacient = programare.getPacient(); // Daca schimbam programarea, se schimba si pacientul
        recalculareSume();
    }

    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public void setValoareTotala(double valoareTotala) { 
        this.valoareTotala = valoareTotala; 
        recalculareSume(); // Recalculam sumele la schimbarea pretului
    }

    public void setProcentAcoperitAsigurare(double procent) { 
        this.procentAcoperitAsigurare = procent; 
        recalculareSume(); // Recalculam sumele la schimbarea procentului
    }

    public void setMetodaPlata(MetodaPlata metodaPlata) { this.metodaPlata = metodaPlata; }

    public void setStatutPlata(StatutPlata statutPlata) { this.statutPlata = statutPlata; }

    @Override
    public String toString() {
        return "Factura: " + serieNumar + " | Total: " + valoareTotala + 
               " | De plata pacient: " + sumaDiferentaPacient + " | Status: " + statutPlata;
    }
}