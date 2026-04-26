package com.pao.proiect.cabinet_medical.service;

import com.pao.proiect.cabinet_medical.model.Pacient;
import java.util.ArrayList;
import java.util.List;

public class PacientService {
    private static PacientService instance;
    private List<Pacient> pacienti = new ArrayList<>();

    private PacientService() {}

    public static PacientService getInstance() {
        if (instance == null) {
            instance = new PacientService();
        }
        return instance;
    }

    //1. adauga
    public void adaugaPacient(Pacient p) { pacienti.add(p); }

    //2. sterge
    public boolean stergePacient(int id) {
        for (int i = 0; i < pacienti.size(); i++) {
            if (pacienti.get(i).getId() == id) {
                pacienti.remove(i);
                return true;
            }
        }
        return false;
    }


    //3. cauta dupa cnp
    public Pacient cautaPacientDupaCnp(String cnp) {
        for (Pacient p : pacienti) {
            if (p.getCnp().equals(cnp)) {
                return p;
            }
        }
        return null;
    }

    //4. listeaza toate
    public List<Pacient> getToțiPacienții() {
        return new ArrayList<>(pacienti);
    }

    public List<Pacient> getPacienti() { return pacienti; }
}