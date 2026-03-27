package com.pao.laboratory05.audit;

public class Angajat implements Comparable<Angajat>{

    String nume;
    Departament departament;
    double salariu;

    public Angajat(String nume, Departament departament, double salariu) {
        this.nume = nume;
        this.departament = departament;
        this.salariu = salariu;
    }

    public String getNume() {
        return this.nume;
    }

    public Departament getDepartament() {
        return this.departament;
    }

    public double getSalariu() {
        return this.salariu;
    }

    @Override
    public String toString() {
        return "Angajat{nume='" + this.nume + "', departament=" + departament.toString() + ", salariu= " + this.salariu + "}";
    }

    @Override
    public int compareTo(Angajat other) {
        return Double.compare(other.getSalariu(), this.salariu);
    }
}
