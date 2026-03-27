package com.pao.laboratory05.angajati;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AngajatService service = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajati =====");
            System.out.println("1. Adauga angajat");
            System.out.println("2. Listare dupa salariu");
            System.out.println("3. Cauta dupa departament");
            System.out.println("0. Iesire");
            System.out.print("Optiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine(); 

            if (optiune == 0) {
                System.out.println("La revedere!");
                break;
            } else if (optiune == 1) {
                System.out.print("Nume: ");
                String nume = scanner.nextLine();
                
                System.out.print("Departament (nume): ");
                String deptNume = scanner.nextLine();
                
                System.out.print("Departament (locatie): ");
                String deptLocatie = scanner.nextLine();
                
                System.out.print("Salariu: ");
                double salariu = scanner.nextDouble();
                scanner.nextLine(); 

                Departament dept = new Departament(deptNume, deptLocatie);
                Angajat angajat = new Angajat(nume, dept, salariu);
                service.addAngajat(angajat);
                
            } else if (optiune == 2) {
                service.listBySalary();
            } else if (optiune == 3) {
                System.out.print("Departament: ");
                String deptCautat = scanner.nextLine();
                service.findByDepartament(deptCautat);
            } else {
                System.out.println("Optiune invalida!");
            }
        }
        
        scanner.close();
    }
}