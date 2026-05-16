package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        // Asigura-te ca directorul "output" exista
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();

        // Citeste si scrie inregistrarile in OUTPUT_FILE cu DataOutputStream
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            ByteBuffer buffer = ByteBuffer.allocate(RECORD_SIZE);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            for (int i = 0; i < n; i++) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                String tipStr = scanner.next();

                // Determina byte-ul pentru tip (0=CREDIT, 1=DEBIT)
                byte tipByte = (byte) ("CREDIT".equalsIgnoreCase(tipStr) ? 0 : 1);
                // Status initial implicit: 0=PENDING
                byte statusByte = 0;

                // Curata buffer-ul pentru noua inregistrare (umple cu 0)
                buffer.clear();
                Arrays.fill(buffer.array(), (byte) 0);

                // bytes 0-3: id
                buffer.putInt(id);
                // bytes 4-11: suma
                buffer.putDouble(suma);

                // bytes 12-21: data (10 chars ASCII, paddat cu spatii la dreapta)
                byte[] dataBytes = data.getBytes(StandardCharsets.US_ASCII);
                byte[] paddedData = new byte[10];
                Arrays.fill(paddedData, (byte) ' '); 
                System.arraycopy(dataBytes, 0, paddedData, 0, Math.min(dataBytes.length, 10));
                buffer.put(paddedData);

                // byte 22: tip
                buffer.put(tipByte);
                // byte 23: status
                buffer.put(statusByte);

                // bytes 24-31 ramun automat 0 datorita Arrays.fill de mai sus (padding)

                // Scrie cei 32 de bytes pe disc
                dos.write(buffer.array());
            }
        } catch (IOException e) {
            System.err.println("Eroare la scriere binar: " + e.getMessage());
            return;
        }

        //  Proceseaza comenzile din stdin pana la EOF cu RandomAccessFile
        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            ByteBuffer buffer = ByteBuffer.allocate(RECORD_SIZE);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            while (scanner.hasNext()) {
                String command = scanner.next();

                switch (command) {
                    case "READ":
                        int readIdx = scanner.nextInt();
                        long readPos = (long) readIdx * RECORD_SIZE;
                        
                        if (readPos < raf.length()) {
                            raf.seek(readPos);
                            raf.readFully(buffer.array());
                            buffer.clear();
                            printRecord(readIdx, buffer);
                        }
                        break;

                    case "UPDATE":
                        int updateIdx = scanner.nextInt();
                        String statusStr = scanner.next();
                        
                        byte newStatusByte = 0; // PENDING
                        if ("PROCESSED".equalsIgnoreCase(statusStr)) {
                            newStatusByte = 1;
                        } else if ("REJECTED".equalsIgnoreCase(statusStr)) {
                            newStatusByte = 2;
                        }

                        long updatePos = (long) updateIdx * RECORD_SIZE + 23;
                        if (updatePos < raf.length()) {
                            raf.seek(updatePos);
                            raf.writeByte(newStatusByte);
                            System.out.println("Updated [" + updateIdx + "]: " + statusStr.toUpperCase());
                        }
                        break;

                    case "PRINT_ALL":
                        long totalRecords = raf.length() / RECORD_SIZE;
                        raf.seek(0);
                        for (int i = 0; i < totalRecords; i++) {
                            raf.readFully(buffer.array());
                            buffer.clear();
                            printRecord(i, buffer);
                        }
                        break;

                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la RandomAccessFile: " + e.getMessage());
        }

        scanner.close();
    }

    // Metoda helper pentru citirea unui buffer binar de 32 bytes si afisarea lui
    private static void printRecord(int idx, ByteBuffer buffer) {
        int id = buffer.getInt(0);
        double suma = buffer.getDouble(4);

        // Extrage string-ul pentru data (10 bytes) si curata spatiile de la padding
        byte[] dataBytes = new byte[10];
        buffer.position(12);
        buffer.get(dataBytes);
        String data = new String(dataBytes, StandardCharsets.US_ASCII).trim();

        byte tipByte = buffer.get(22);
        byte statusByte = buffer.get(23);

        String tip = (tipByte == 0) ? "CREDIT" : "DEBIT";
        
        String status = "PENDING";
        if (statusByte == 1) {
            status = "PROCESSED";
        } else if (statusByte == 2) {
            status = "REJECTED";
        }

        // Format linie output cerut:
        // [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>
        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                idx, id, data, tip, suma, status);
        }


    
}
