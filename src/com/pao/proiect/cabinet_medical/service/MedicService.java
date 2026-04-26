package com.pao.proiect.cabinet_medical.service;

import com.pao.proiect.cabinet_medical.enums.Specializare;
import com.pao.proiect.cabinet_medical.exceptions.EntitateNegasitaException;
import com.pao.proiect.cabinet_medical.model.Medic;
import java.util.ArrayList;
import java.util.List;

public class MedicService {

    private static MedicService instance;
    private List<Medic> medici = new ArrayList<>();
    
    //1. adauga
    public void adaugaMedic(Medic m) {
        this.medici.add(m);
    }

    private MedicService() {}

    public static MedicService getInstance() {
        if (instance == null) {
            instance = new MedicService();
        }
        return instance;
    }

    //2. sterge (dupa id din persoana)
    public boolean stergeMedic(int id) {
        for (int i = 0; i < medici.size(); i++) {
            if (medici.get(i).getId() == id) {
                medici.remove(i);
                return true;
            }
        }
        return false;
    }

    //3a. cauta dupa id
    public Medic cautaMedicDupaId(int id) throws EntitateNegasitaException  {
        for (Medic m : medici) {
            if (m.getId() == id) {
                return m;
            }
        }
        throw new EntitateNegasitaException("Medicul cu ID-ul " + id + " nu a fost gasit in sistem!");
    }

    //3b. cauta dupa nume
    public List<Medic> cautaMediciDupaNume(String numeCautat) throws EntitateNegasitaException {
        List<Medic> rezultate = new ArrayList<>();
        for (Medic m : medici) {
            if (m.getNume().equalsIgnoreCase(numeCautat)) {
                rezultate.add(m);
            }
        }

        if (rezultate.isEmpty()) {
            throw new EntitateNegasitaException("Niciun medic cu numele " + numeCautat + " nu a fost gasit!");
        }
        return rezultate;
    }

    //4. listeaza toate
    public List<Medic> getToțiMedicii() {
        return new ArrayList<>(medici);
    }

    /*Actiunea 3: Returneaza o lista cu toti medicii care au o anumita specializare. */
    public List<Medic> mediciSpecializare(Specializare specializareCautata) {
        // Cream o lista goala in care vom aduna rezultatele
        List<Medic> rezultate = new ArrayList<>();
    
        // Parcurgem toti medicii din clinica
        for (Medic m : medici) {
            // Verificam daca specializarea medicului curent coincide cu cea cautata
            if (m.getSpecializare() == specializareCautata) {
                // Daca da, il adaugam in lista noastra de rezultate
                rezultate.add(m);
            }
        }
    
        // Returnam lista finala (poate fi goala daca nu gasim niciun medic)
        return rezultate;
    }

    public List<Medic> getMedici() {
        return this.medici;
    }
}