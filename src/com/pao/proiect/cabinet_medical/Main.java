package com.pao.proiect.cabinet_medical;

import com.pao.proiect.cabinet_medical.enums.*;
import com.pao.proiect.cabinet_medical.exceptions.*;
import com.pao.proiect.cabinet_medical.model.*;
import com.pao.proiect.cabinet_medical.service.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Initializare servicii (Singleton)
        MedicService medicService = MedicService.getInstance();
        PacientService pacientService = PacientService.getInstance();
        CabinetService cabinetService = CabinetService.getInstance();
        ProgramareService programareService = ProgramareService.getInstance();

        System.out.println(" START DEMONSTRATIE SISTEM MEDICAL: \n");

        // 2. Adaugare Medici (
        Medic m1 = new Medic(1, "Popescu", "Ion", "1800101123456", "ion@med.ro", "0722111222", 
                             Specializare.MEDICINA_MATERNO_FETALA, "Primar");
        Medic m2 = new Medic(2, "Ionescu", "Maria", "2850202654321", "maria@med.ro", "0733444555", 
                             Specializare.OBSTETRICA, "Specialist");
        
        medicService.adaugaMedic(m1);
        medicService.adaugaMedic(m2);

        // 3. Setare program de lucru 
        LocalDate azi = LocalDate.now();
        LocalDate maine = azi.plusDays(1);
        m1.adaugaProgram(azi, new IntervalOrar(LocalTime.of(8, 0), LocalTime.of(14, 0)));
        m2.adaugaProgram(azi, new IntervalOrar(LocalTime.of(8, 0), LocalTime.of(20, 0)));
        m2.adaugaProgram(maine, new IntervalOrar(LocalTime.of(9, 0), LocalTime.of(17, 0)));

        // 4. Adaugare Cabinete
        Cabinet cab1 = new Cabinet(1, 101, 1, Specializare.OBSTETRICA);
        Cabinet cab2 = new Cabinet(2, 202, 2, Specializare.MEDICINA_MATERNO_FETALA);

        cabinetService.adaugaCabinet(cab1);
        cabinetService.adaugaCabinet(cab2);

        // 5. Adaugare Pacienti
        Pacient p1 = new Pacient(
            1, 
            "Georgescu", 
            "Dan", 
            "1900505998877", 
            "dan@gmail.com", 
            "0744000111", 
            GrupaSanguina.A_POZITIV, 
            "ASIG12345678"
        );
        pacientService.adaugaPacient(p1);

        // 6. Actiune: Gasire sloturi libere 
        System.out.println("Sloturi libere disponibile azi:");
        List<String> sloturi = programareService.gasestePrimeleTreiSloturiLibere(Specializare.OBSTETRICA, 30);
        for (String s : sloturi) {
            System.out.println(s);
        }

        //definire tipuri de consultatii
        TipConsultatie consObstetrica = new TipConsultatie(1, Specializare.OBSTETRICA, "Control Periodic Obstetrica", 200.0, 30);
        TipConsultatie consMorfologie = new TipConsultatie(2, Specializare.MEDICINA_MATERNO_FETALA, "Morfologie Fetala Trim. II", 500.0, 60);

        Echipament ecograf = new Echipament(1, "Ecograf 4D", "ECO-9988", LocalDate.of(2027, 1, 1));
        cab1.getEchipamente().add(ecograf);
        System.out.println("Echipamentul " + ecograf.getNume() + " a fost instalat in Cabinetul " + cab1.getNumarSala());

        List<Cabinet> saliDotate = new ArrayList<>();
        saliDotate.add(cab1);
        consObstetrica.getEchipamenteNecesare().put(ecograf, saliDotate);
        cab1.getIgienizari().add(LocalDateTime.of(azi, LocalTime.of(13, 0)));
        System.out.println("Echipamente si plan de igienizare actualizate.");

        // 7. Actiune: Creare Programare si tratare exceptie custom
        System.out.println("\n Creare Programare: \n");
        try {
            // Programare Reusita (la ora 14:30, cand medicul m2 incepe si sala e libera)
            LocalDateTime oraOk = LocalDateTime.of(azi, LocalTime.of(14, 30));
            programareService.creeazaProgramare(1, m2, p1, consObstetrica, cab1, oraOk, 0);
            System.out.println(" Prima programare creata cu succes.");

            // Incercare programare esuata - Suprapunere cu igienizarea (ora 13:00)
            System.out.println("\nIncercare programare in timpul igienizarii (ora 13:00):");
            LocalDateTime oraIgienizare = LocalDateTime.of(azi, LocalTime.of(13, 0));
            programareService.creeazaProgramare(2, m2, p1, consObstetrica, cab1, oraIgienizare, 0);

        } catch (ProgramareInvalidaException e) {
            System.out.println("Eroare:  " + e.getMessage());
        }

        System.out.println("\n Alte operatii Service ");
        try {
            // Cautare Medic in MedicService
            Medic gasit = medicService.cautaMedicDupaId(2);
            System.out.println("Medic gasit: " + gasit.getNume() + " (" + gasit.getGradMedic() + ")");

            // Listare Cabinete dupa specializare 
            System.out.println("Cabinete de Obstetrica:");
            List<Cabinet> toateCabinetele = cabinetService.getToateCabinetele();
            for (Cabinet c : toateCabinetele) {
                if (c.getSpecializare() == Specializare.OBSTETRICA) {
                    System.out.println("- Sala " + c.getNumarSala() + ", Etaj " + c.getEtaj());
                }
            }

        } catch (EntitateNegasitaException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n Testare metode suplimentare CabinetService: ");

        // 1. Cautare cabinet dupa numarul salii (Metoda 3 din service)
        int numarCautat = 101;
        Cabinet gasitCab = cabinetService.cautaCabinetDupaNumar(numarCautat);
        if (gasitCab != null) {
            System.out.println("Cabinetul " + numarCautat + " a fost gasit la etajul " + gasitCab.getEtaj());
        } else {
            System.out.println("Cabinetul " + numarCautat + " nu exista.");
        }

        // 2. Stergere cabinet (Metoda 2 din service)
        int salaDeSters = 202;
        boolean aFostSters = cabinetService.stergeCabinet(salaDeSters);
        System.out.println("Stergere cabinet " + salaDeSters + ": " + (aFostSters ? "Succes" : "Esec"));

        // 3. Verificare lista dupa stergere (Metoda 4 - getToateCabinetele)
        System.out.println("Numar total cabinete ramase: " + cabinetService.getToateCabinetele().size());


        // 8. Actiune: Sortare programari (Comparable)
        System.out.println("\n Lista programari sortate cronologic: ");
        programareService.getProgramariSortateCronologic().forEach(System.out::println);

        System.out.println("\n Testare Operatii MedicService: \n  ");
        try {
            // 1. Cautare Medic dupa ID (Metoda 3a)
            Medic mId = medicService.cautaMedicDupaId(2);
            System.out.println(" Medic gasit dupa ID: " + mId.getNume() + " " + mId.getPrenume());

            // 2. Cautare Medici dupa Nume (Metoda 3b)
            
            System.out.println("Cautare medici cu numele 'Popescu':");
            List<Medic> rezultateNume = medicService.cautaMediciDupaNume("Popescu");
            for (Medic m : rezultateNume) {
                System.out.println("  Gasit: " + m.getPrenume() + " " + m.getNume() + " (" + m.getSpecializare() + ")");
            }

            // 3. Filtrare dupa Specializare (Actiunea 3)
            System.out.println("Filtrare medici pentru specializarea OBSTETRICA:");
            List<Medic> mediciSpec = medicService.mediciSpecializare(Specializare.OBSTETRICA);
            for (Medic m : mediciSpec) {
                System.out.println("  Dr. " + m.getNume() + " - " + m.getGradMedic());
            }

            // 4. Listare toti medicii (Metoda 4)
            System.out.println("Total medici inregistrati: " + medicService.getToțiMedicii().size());

            // 5. Stergere Medic (Metoda 2)
            // Stergem m1 (Popescu Ion) pentru test
            boolean sters = medicService.stergeMedic(1);
            System.out.println("Medicul cu ID 1 a fost sters? " + (sters ? "DA" : "NU"));
            System.out.println("Medici ramasi dupa stergere: " + medicService.getToțiMedicii().size());

        } catch (EntitateNegasitaException e) {
            System.out.println("Exceptie: " + e.getMessage());
        }


        System.out.println("\n Testare Operatii PacientService: \n");

        // 1. Cautare Pacient dupa CNP (Metoda 3)
        String cnpCautat = "1900505998877";
        Pacient pGasit = pacientService.cautaPacientDupaCnp(cnpCautat);
        if (pGasit != null) {
            System.out.println(" Pacient gasit: " + pGasit.getNume() + " " + pGasit.getPrenume());
        } else {
            System.out.println("Pacientul cu CNP-ul " + cnpCautat + " nu exista.");
        }

        // 2. Listare toti pacientii (Metoda 4)
        System.out.println("In sistem sunt inregistrati: " + pacientService.getToțiPacienții().size() + " pacienti.");

        // 3. Adaugare pacient nou pentru test stergere
        Pacient p2 = new Pacient(2, "Vasile", "Andrei", "1234567890123", "andrei@mail.com", "0722000000", GrupaSanguina.B_NEGATIV, "ASIG999");
        pacientService.adaugaPacient(p2);
        System.out.println("Pacienti dupa adaugare p2: " + pacientService.getPacienti().size());

        // 4. Stergere Pacient (Metoda 2)
        boolean pacientSters = pacientService.stergePacient(2);
        System.out.println("Pacientul cu ID 2 a fost sters? " + (pacientSters ? "DA" : "NU"));
        System.out.println("Pacienti ramasi final: " + pacientService.getPacienti().size());


        System.out.println("\n Testare ultimele metode ProgramareService: \n");

        // 1. Cauta programare dupa ID (Metoda 3a)
        Programare pg = programareService.cautaProgramareDupaId(1);
        if (pg != null) {
            System.out.println("Programare gasita dupa ID 1: Pacient " + pg.getPacient().getNume());
        }

        // 2. Cauta programarile unui pacient dupa CNP (Metoda 3b)
        System.out.println("Programari pentru pacientul " + p1.getNume() + ":");
        List<Programare> progsPacient = programareService.getProgramariPacient(p1.getCnp());
        for (Programare p : progsPacient) {
            System.out.println(" - " + p.getTipConsultatie().getNume() + " la data " + p.getDataOra());
        }

        // 3. Stergere programare (Metoda 2)
        boolean stersP = programareService.stergeProgramare(1);
        System.out.println("Programarea cu ID 1 a fost stearsa? " + (stersP ? "DA" : "NU"));
        
        System.out.println("\n Gata😛 Toate serviciile au fost testate complet! ");

    }
}