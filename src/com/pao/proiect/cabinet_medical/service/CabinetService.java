package com.pao.proiect.cabinet_medical.service;

import com.pao.proiect.cabinet_medical.model.Cabinet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CabinetService {
    private static CabinetService instance;
    private List<Cabinet> cabinete = new ArrayList<>();

    private CabinetService() {}

    public static CabinetService getInstance() {
        if (instance == null) {
            instance = new CabinetService();
        }
        return instance;
    }

    //1. ADAUGA
    public void adaugaCabinet(Cabinet c) { cabinete.add(c); }
    public List<Cabinet> getCabinete() { return cabinete; }

    public boolean esteSalaLiberaDeIgienizare(Cabinet cab, LocalDateTime start, LocalDateTime sfarsit) {
        for (LocalDateTime startIgienizare : cab.getIgienizari()) {
            LocalDateTime sfarsitIgienizare = startIgienizare.plusHours(1);
            if (start.isBefore(sfarsitIgienizare) && sfarsit.isAfter(startIgienizare)) {
                return false;
            }
        }
        return true;
    }

    //2. STERGE
    public boolean stergeCabinet(int numarSala) {
        for (int i = 0; i < cabinete.size(); i++) {
            if (cabinete.get(i).getNumarSala() == numarSala) {
                cabinete.remove(i);
                return true;
            }
        }
        return false; // daca nu am gasit
    }


    //3. cauta dupa numar sala
    public Cabinet cautaCabinetDupaNumar(int numarSala) {
        for (Cabinet c : cabinete) {
            if (c.getNumarSala() == numarSala) {
                return c;
            }
        }
        return null; // Nu exista
    }

    //4. listeaza toate
    public List<Cabinet> getToateCabinetele() {
        return new ArrayList<>(cabinete);
    }
}