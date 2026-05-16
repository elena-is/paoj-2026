package com.pao.laboratory09.exercise1;

import java.io.Serializable;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private double suma;
    private String data;
    private String contSursa;
    private String contDestinatie;
    private String tip;
    private String note;

    public Transaction(int id, double suma, String data, String contSursa, String contDestinatie, String tip) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.contSursa = contSursa;
        this.contDestinatie = contDestinatie;
        this.tip = tip;
    }

    public int getId() { return id; }
    public double getSuma() { return suma; }
    public String getData() { return data; }
    public String getContSursa() { return contSursa; }
    public String getContDestinatie() { return contDestinatie; }
    public String getTip() { return tip; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}