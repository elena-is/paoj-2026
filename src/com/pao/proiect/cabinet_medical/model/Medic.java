package com.pao.proiect.cabinet_medical.model;

import com.pao.proiect.cabinet_medical.enums.Specializare;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Medic extends Persoana {
    private Specializare specializare;
    private String gradMedic; // Rezident, Specialist, Primar
    
    // Dictionar: Cheia este data, Valoarea este intervalul orar
    private Map<LocalDate, IntervalOrar> programLucru;

    public Medic(int id, String nume, String prenume, String cnp, String email, String telefon, 
                 Specializare specializare, String gradMedic) {
        super(id, nume, prenume, cnp, email, telefon);
        this.specializare = specializare;
        this.gradMedic = gradMedic;
        this.programLucru = new HashMap<>();
    }

    @Override
    public String getTipPersoana() {
        return "Medic - Specializarea: " + this.specializare;
    }

    // Adauga o zi de lucru
    public void adaugaProgram(LocalDate data, IntervalOrar interval) {
        this.programLucru.put(data, interval);
    }

    public Map<LocalDate, IntervalOrar> getProgramLucru() {
        return programLucru;
    }
    
    public Specializare getSpecializare() {
        return specializare;
    }

    public void setSpecializare(Specializare specializare) {
        this.specializare = specializare;
    }

    public String getGradMedic() {
        return gradMedic;
    }

    public void setGradMedic(String gradMedic) {
        this.gradMedic = gradMedic;
    }

    public void setProgramLucru(Map<LocalDate, IntervalOrar> programLucru) {
        this.programLucru = programLucru;
    }


    //Sterge programul intr-o zi
    public void stergeProgramZi(LocalDate data) {
        this.programLucru.remove(data);
    }

}