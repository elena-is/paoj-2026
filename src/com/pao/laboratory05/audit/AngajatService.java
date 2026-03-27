package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {
    
    private Angajat[] angajati;
    private AuditEntry[] auditLog;

    private AngajatService() {
        this.angajati = new Angajat[0];
        this.auditLog = new AuditEntry[0];
    }

    private static class AngajatServiceHolder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return AngajatServiceHolder.INSTANCE;
    }

    private void logAction(String action, String target) {
        String timestamp = LocalDateTime.now().toString();
        AuditEntry entry = new AuditEntry(action, target, timestamp);

        AuditEntry[] tmp = new AuditEntry[this.auditLog.length + 1];
        System.arraycopy(this.auditLog, 0, tmp, 0, this.auditLog.length);
        tmp[tmp.length - 1] = entry;
        this.auditLog = tmp;
    }


    public void addAngajat(com.pao.laboratory05.audit.Angajat angajat) {
        Angajat[] tmp = new Angajat[this.angajati.length + 1];
        System.arraycopy(this.angajati, 0, tmp, 0, this.angajati.length);
        tmp[tmp.length - 1] = angajat;
        this.angajati = tmp;
        
        System.out.println("Angajat adaugat: " + angajat.getNume());
        
        logAction("ADD", angajat.getNume());
    }

    public void findByDepartament(String numeDept) {
        logAction("FIND_BY_DEPT", numeDept);
        
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

    public void printAuditLog() {
        if (auditLog.length == 0) {
            System.out.println("Jurnalul este gol.");
        } else {
            for (AuditEntry entry : auditLog) {
                System.out.println(entry);
            }
        }
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


}