package com.pao.proiect.cabinet_medical.service;

import com.pao.proiect.cabinet_medical.enums.Specializare;
import com.pao.proiect.cabinet_medical.exceptions.ProgramareInvalidaException;
import com.pao.proiect.cabinet_medical.model.*;
import java.time.*; //
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProgramareService {
    private static ProgramareService instance;
    private List<Programare> programari = new ArrayList<>();

    private ProgramareService() {}

    public static ProgramareService getInstance() {
        if (instance == null) {
            instance = new ProgramareService();
        }
        return instance;
    }

    public boolean esteMedicDisponibil(Medic medic, LocalDateTime startDorit, int durataDorita) {
        // Calculam intervalul dorit 
        LocalDateTime sfarsitDorit = startDorit.plusMinutes(durataDorita);
        
        // Extragem componentele pentru comparația cu programul de lucru
        LocalDate dataDorita = startDorit.toLocalDate();
        LocalTime oraStartDorita = startDorit.toLocalTime();
        LocalTime oraSfarsitDorita = sfarsitDorit.toLocalTime();

        // VALIDARE 1: Programul de lucru (IntervalOrar)
        IntervalOrar tura = medic.getProgramLucru().get(dataDorita);
        if (tura == null) {
            System.out.println("Medicul " + medic.getNume() + " nu lucreaza in data de " + dataDorita);
            return false;
        }

        // Verificam daca intervalul cerut are loc in tura lui
        if (oraStartDorita.isBefore(tura.getStart()) || oraSfarsitDorita.isAfter(tura.getSfarsit())) {
            System.out.println("Medicul este în afara orelor de program (" + tura + ")");
            return false;
        }

        // VALIDARE 2: Suprapunere cu alte programari ale medicului
        for (Programare p : programari) {
            // Verificam intai daca programarea ii apartine acestui medic
            if (p.getMedic().equals(medic)) {
                
                // Calculam intervalul programarii existente
                LocalDateTime inceputExistent = p.getDataOra();
                int durataE = (p.getDurataEstimata() > 0) ? p.getDurataEstimata() : p.getTipConsultatie().getDurataMinute();
                LocalDateTime sfarsitExistent = inceputExistent.plusMinutes(durataE);

                // Verificam daca intervalele se intercaleaza 
                if (startDorit.isBefore(sfarsitExistent) && sfarsitDorit.isAfter(inceputExistent)) {
                    System.out.println("Medicul are deja o alta programare în intervalul " + 
                                    inceputExistent.toLocalTime() + " - " + sfarsitExistent.toLocalTime());
                    return false;
                }
            }
        }

        // Daca a ajuns aici, medicul este liber
        return true;
    }


    public boolean esteSalaDisponibila(Cabinet cabinet, LocalDateTime startDorit, int durataDorita) {
        // 1. Calculam sfarsitul intervalului dorit pentru consultatie
        LocalDateTime sfarsitDorit = startDorit.plusMinutes(durataDorita);

        // VALIDARE 1: Verificam suprapunerea cu Igienizarile
        for (LocalDateTime startIgienizare : cabinet.getIgienizari()) {
            // Igienizarea durează o ora (60 minute)
            LocalDateTime sfarsitIgienizare = startIgienizare.plusHours(1);

            // Verificam intercalarea
            if (startDorit.isBefore(sfarsitIgienizare) && sfarsitDorit.isAfter(startIgienizare)) {
                System.out.println("Sala " + cabinet.getNumarSala() + " este ocupata pentru igienizare (interval: " 
                                + startIgienizare.toLocalTime() + " - " + sfarsitIgienizare.toLocalTime() + ")");
                return false;
            }
        }

        // VALIDARE 2: Verificam suprapunerea cu alte Programari
        for (Programare p : programari) {
            // Filtram doar programarile care au loc in aceasta sala
            if (p.getCabinet().equals(cabinet)) {
                
                // Calculam intervalul programarii existente
                LocalDateTime inceputExistent = p.getDataOra();
                
                // Calculam durata: durataEstimata (dacă e > 0) sau durata default din TipConsultatie
                int durataE = (p.getDurataEstimata() > 0) ? p.getDurataEstimata() : p.getTipConsultatie().getDurataMinute();
                LocalDateTime sfarsitExistent = inceputExistent.plusMinutes(durataE);

                // Verificam intercalarea
                if (startDorit.isBefore(sfarsitExistent) && sfarsitDorit.isAfter(inceputExistent)) {
                    System.out.println("Sala " + cabinet.getNumarSala() + " este deja ocupata de o consultatie.");
                    return false;
                }
            }
        }

        // Daca nu s-a gasit nicio suprapunere, sala este disponibila
        return true;
    }


    private Cabinet gasesteSalaLibera(Specializare spec, LocalDateTime start, int durata) {
        for (Cabinet cab : CabinetService.getInstance().getCabinete()) {
            if (cab.getSpecializare() == spec && esteSalaDisponibila(cab, start, durata)) {
                return cab;
            }
        }
        return null;
    }

    /**
     * Actiunea 1: Gaseste primele 3 variante disponibile pentru o specializare.
     * Cauta incepand de la momentul actual, verificand atat medicul cat si sala.
     * vrem ca programarile sa fie doar la lore frumoase: :00, :30, :45
     */
    public List<String> gasestePrimeleTreiSloturiLibere(Specializare specializare, int durataMinute) {
        List<String> varianteGasite = new ArrayList<>();
        System.out.println("Caut sloturi pentru: " + specializare);
        LocalDateTime momentStart = LocalDateTime.now().withSecond(0).withNano(0);
        
        // Cautam in urmatoarele 10 zile pentru a fi siguri ca gasim variante
        for (int i = 0; i < 10; i++) {
            LocalDate dataCurenta = momentStart.toLocalDate().plusDays(i);
            
            List<Medic> mediciRelevanti = new ArrayList<>();

            for (Medic m : MedicService.getInstance().getMedici()) {
                if (m.getSpecializare() == specializare) {
                    mediciRelevanti.add(m);
                }
            }

            for (Medic medic : mediciRelevanti) {
                IntervalOrar tura = medic.getProgramLucru().get(dataCurenta);
                if (tura == null) continue;

                // Verificam fiecare ora din tura medicului
                for (int oraH = tura.getStart().getHour(); oraH <= tura.getSfarsit().getHour(); oraH++) {
                    
                    // Definim intervalele de inceput programare pe care le vrem
                    int[] minuteFixe = {0, 30, 45};

                    for (int min : minuteFixe) {
                        LocalTime oraVerificata = LocalTime.of(oraH, min);
                        
                        // Validam ca ora sa fie in interiorul turei si in viitor (daca e azi)
                        if (oraVerificata.isBefore(tura.getStart()) || oraVerificata.isAfter(tura.getSfarsit().minusMinutes(durataMinute))) {
                            continue;
                        }
                        if (dataCurenta.equals(LocalDate.now()) && oraVerificata.isBefore(LocalTime.now())) {
                            continue;
                        }

                        LocalDateTime slotDorit = LocalDateTime.of(dataCurenta, oraVerificata);
                        
                        // Verificam disponibilitate Medic si Sala
                        if (esteMedicDisponibil(medic, slotDorit, durataMinute)) {
                            Cabinet salaLibera = gasesteSalaLibera(specializare, slotDorit, durataMinute);
                            
                            if (salaLibera != null) {
                                String varianta = String.format("Data: %s | Ora: %s | Medic: %s | Sala: %d",
                                        dataCurenta, oraVerificata, medic.getNume(), salaLibera.getNumarSala());
                                
                                varianteGasite.add(varianta);
                                //aici daca vrem un alt numar de variante putem modifica
                                if (varianteGasite.size() == 3) return varianteGasite;
                            }
                        }
                    }
                }
            }
        }
        return varianteGasite;
    }



    /*
    Actiunea 2: Creeaza o Programare 
    e actiunea 1.adauga ceruta de la clasele Service
    */
    public void creeazaProgramare(int id, Medic medic, Pacient pacient, TipConsultatie tip, 
        Cabinet cabinet, LocalDateTime dataOra, int durataSpeciala) throws ProgramareInvalidaException {

        int durataFinala;
        if (durataSpeciala > 0) {
        // Daca utilizatorul a trimis o durata specifica (ex: 50 min pentru un caz complex)
        durataFinala = durataSpeciala;
        } else {
        // Altfel, folosim durata standard definita in TipConsultatie
        durataFinala = tip.getDurataMinute();
        }

        //  Validam disponibilitatea Medicului
        if (esteMedicDisponibil(medic, dataOra, durataFinala) == false) {
            throw new ProgramareInvalidaException("Medicul " + medic.getNume() + " nu este disponibil.");
        }

        //  Validam disponibilitatea Salii
        if (esteSalaDisponibila(cabinet, dataOra, durataFinala) == false) {
            throw new ProgramareInvalidaException("Sala " + cabinet.getNumarSala() + " este ocupata.");
        }

        if (cabinet.getSpecializare() != tip.getSpecializare()) {
            throw new ProgramareInvalidaException("Specializarea salii nu coincide cu tipul consultatiei!");
        }

        Programare noua = new Programare(id, medic, pacient, tip, cabinet, dataOra, durataSpeciala);


        this.programari.add(noua);
        System.out.println("Programare adaugata cu succes!");
        }


    //2. sterge
    public boolean stergeProgramare(int id) {
        for (int i = 0; i < programari.size(); i++) {
            if (programari.get(i).getId() == id) {
                programari.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public void adaugaProgramareInLista(Programare p) {
        programari.add(p);
    }
    
    public List<Programare> getProgramari() {
        return programari;
    }

    //3a. cauta dupa id
    public Programare cautaProgramareDupaId(int id) {
        for (Programare p : programari) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }


    //3b. cauta dupa cnp pacient
    public List<Programare> getProgramariPacient(String cnpPacient) {
        List<Programare> rezultate = new ArrayList<>();
        for (Programare p : programari) {
            if (p.getPacient().getCnp().equals(cnpPacient)) {
                rezultate.add(p);
            }
        }
        return rezultate;
    }

    //4. listeaza toate
    public List<Programare> getToateProgramarile() {
        return new ArrayList<>(programari);
    }

    //pentru sortarea impelmentata cu Comparable
    public List<Programare> getProgramariSortateCronologic() {
        List<Programare> copie = new ArrayList<>(programari);
        Collections.sort(copie);
        return copie;
    }

}