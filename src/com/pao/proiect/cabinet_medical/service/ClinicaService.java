package com.pao.proiect.cabinet_medical.service;

import com.pao.proiect.cabinet_medical.model.*;
import java.util.ArrayList;
import java.util.List;

/*
Actiuni:
1. Actiunea 1: Gaseste primele 3 variante disponibile pentru o specializare.
2.Actiunea 2: Creeaza o Programare 
3. Actiunea 3: Returneaza o lista cu toti medicii care au o anumita specializare.
*/

public class ClinicaService {
    // Listele care simuleaza baza de date a clinicii
    private List<Medic> medici;
    private List<Pacient> pacienti;
    private List<Cabinet> cabinete;
    private List<Programare> programari;

    public ClinicaService() {
        this.medici = new ArrayList<>();
        this.pacienti = new ArrayList<>();
        this.cabinete = new ArrayList<>();
        this.programari = new ArrayList<>();
    }


    public void adaugaMedic(Medic m) { medici.add(m); }
    public void adaugaCabinet(Cabinet c) { cabinete.add(c); }
    public void adaugaPacient(Pacient p) { pacienti.add(p); }



    
}