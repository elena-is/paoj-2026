package com.pao.laboratory05.angajati;

import java.util.*;

public class AngajatService {
    
    private Angajat[] angajati;

    private AngajatService() {
        this.angajati = new Angajat[0];
    }

    private static class AngajatServiceHolder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return AngajatServiceHolder.INSTANCE;
    }


    public void addAngajat(Angajat a) {
        Angajat[] tmp = new Angajat[this.angajati.length + 1];
        System.arraycopy(this.angajati, 0, tmp, 0, this.angajati.length);
        tmp[tmp.length - 1] = a;
        this.angajati = tmp;
        System.out.println("Angajat adaugat: " + a.getNume());
    }

    public void printAll() {
        for (Angajat a : this.angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        Angajat[] copie = this.angajati.clone();
        Arrays.sort(copie);
        
        System.out.println("Angajati dupa salariu: ");
        System.out.println(Arrays.toString(copie));
    }

    public void findByDepartament(String numeDept) {
        boolean gasit = false;
        
        for (Angajat a : this.angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}