# Sistem de Gestiune Cabinet Medical - Etapa I

Acest proiect reprezintă un sistem de monitorizare și gestiune pentru un cabinet medical specializat în Obstetrică și Medicină Materno-Fetală. Sistemul permite administrarea eficientă a medicilor, pacienților, resurselor logistice (cabinete/echipamente) și a fluxului de programări.

## 1.1 — Acțiuni și Interogări (Servicii)
Sistemul permite realizarea următoarelor operațiuni prin intermediul claselor de tip Service:


1.	Adăugarea unui medic nou (înregistrarea în sistem cu specializare și grad medical).

2.	Adăugarea unui pacient nou (înregistrarea cu detalii despre grupa sanguină și asigurare).

3.	Adăugarea unui cabinet (definirea spațiului fizic, a etajului și a specializării acestuia).

4.	Setarea programului de lucru pentru medici (alocarea unor intervale orare specifice pe zile calendaristice).

5.	Găsirea sloturilor libere (interogare complexă care returnează primele 3 variante disponibile pentru o specializare, verificând disponibilitatea medicului și a sălii).

6.	Crearea unei programări noi (cu validarea automată a disponibilității medicului și a cabinetului).

7.	Instalarea și gestionarea echipamentelor (adăugarea de aparatură medicală în cabinete).

8.	Planificarea igienizărilor (înregistrarea intervalelor de curățenie în cabinete).

9.	Verificarea disponibilității sălii (interogare pentru a vedea dacă sala este liberă de igienizare sau de alte consultații).

10.	Căutarea medicului după ID (regăsirea datelor unui medic pe baza identificatorului unic).

11.	Căutarea medicului după nume (filtrare case-insensitive a personalului medical).

12.	Căutarea cabinetului după numărul sălii (identificarea locației și dotărilor unei săli).
13.	Căutarea pacientului după CNP (identificarea rapidă a fișei pacientului).

14.	Căutarea programării după ID (regăsirea detaliilor unei consultații specifice).

15.	Afișarea istoricului de programări pentru un pacient (filtrarea tuturor consultațiilor după CNP-ul pacientului).

16.	Sortarea cronologică a programărilor (ordonarea consultațiilor în funcție de dată și oră).

17.	Ștergerea/Anularea unui medic (eliminarea din baza de date).

18.	Ștergerea/Anularea unui pacient (eliminarea din sistem).

19.	Ștergerea unui cabinet (retragerea din circuitul medical).

20.	Anularea unei programări (eliberarea slotului orar și a sălii).





## 1.2 — Tipuri de obiecte (Modele)
Proiectul utilizează următoarele obiecte pentru a modela domeniul medical:

1.  **Persoana** – Clasă abstractă de bază pentru datele personale (nume, prenume, CNP, email, telefon).
2.  **Medic** – Extinde `Persoana` și adaugă specializarea, gradul medical și programul de lucru.
3.  **Pacient** – Extinde `Persoana` și conține informații despre grupa sanguină și numărul de asigurare.
4.  **Cabinet** – Reprezintă sala fizică unde au loc consultațiile, identificată prin număr și etaj.
5.  **Echipament** – Dispozitive medicale (ex: Ecograf) asociate cabinetelor.
6.  **Programare** – Obiectul central care leagă un pacient de un medic, un cabinet și un tip de consultație la o anumită dată.
7.  **IntervalOrar** – Definește o perioadă de timp (start-sfârșit) folosită pentru programul medicilor.
8.  **TipConsultatie** – Definește denumirea, prețul și durata standard a unui act medical (ex: Control, Morfologie).
9.  **Factura** – Detalii despre plata consultației.
10. **Retetă** – Document eliberat în urma unei programări, conținând medicamentele prescrise.
11. **Medicament** – Produsele farmaceutice incluse în rețetă.

## Enums
- `Specializare` (ex: Cardiologie, Obstetrică)
- `GrupaSanguina` (ex: A_POZITIV, B_NEGATIV)
- `MetodaPlata` (ex: Card, Cash)
- `StatutPlata` (ex: Achitat, Neachitat)

## Excepții Custom
- `ProgramareInvalidaException` – Aruncată în cazul suprapunerilor de orar.
- `EntitateNegasitaException` – Aruncată la căutarea unor ID-uri sau nume inexistente.