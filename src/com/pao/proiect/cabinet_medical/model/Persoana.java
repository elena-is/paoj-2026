package com.pao.proiect.cabinet_medical.model;

//clasa abstracta pt Medic si Pacient
public abstract class Persoana {
    protected final int id; 
    protected String nume;
    protected String prenume;
    protected String cnp;
    protected String email;
    protected String telefon;

    public Persoana(int id, String nume, String prenume, String cnp, String email, String telefon) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.cnp = cnp;
        this.email = email;
        this.telefon = telefon;
    }

    public abstract String getTipPersoana();

    public int getId() { return id; }

    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getCnp() { return cnp; }
    public void setCnp(String cnp) { this.cnp = cnp; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    
    @Override
    public String toString() {
        return "ID: " + id + " | Nume: " + nume + " | CNP: " + cnp;
    }

    @Override
public boolean equals(Object o) {
    // 1. Verificam daca este exact aceeasi instanta in memorie
    if (this == o) return true;
    
    // 2. Verificam daca obiectul primit este null sau are alta clasa
    if (o == null || getClass() != o.getClass()) return false;
    
    // 3. Convertim obiectul la tipul Persoana pentru a-i accesa atributele
    Persoana persoana = (Persoana) o;
    
    // 4. Comparam atributul unic (CNP-ul)
    // Folosim Objects.equals pentru a evita erori daca cnp-ul este null
    return java.util.Objects.equals(cnp, persoana.cnp);
    }

    @Override
    public int hashCode() {
        // Generam un cod numeric bazat pe CNP
        return java.util.Objects.hash(cnp);
    }
}