package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Client {
    public Client() throws IOException {
        Socket socket = new Socket("localhost", 2026);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));
        Random random = new Random();

        // 1. Klijent generiše broj n (između 1 i 5)
        int broj = random.nextInt(5) + 1;
        System.out.println("Generisan broj n: " + broj);

        // 2. Šaljemo broj n serveru (nešifrovano da bi server znao ključ)
        out.println(broj);

        String odgovor;
        int cesarN;
        if (broj < 3) {
            odgovor = "Želim da igram!";
            cesarN = broj + 2;
        } else if (broj == 3) { // Ispravljen uslov za n=3
            odgovor = "Igrao bih!";
            cesarN = broj - 2;
        } else { // n > 3
            odgovor = "Daj mi reč!";
            cesarN = broj + broj;
        }

        // 3. Šaljemo prvu šifrovanu poruku
        String encodeMessage = encode(odgovor, cesarN);
        out.println(encodeMessage);

        // Glavna petlja za komunikaciju
        while (true) {
            // Čitamo poruku od servera
            String line = in.readLine();
            if (line == null) break;

            String desifrovanaPoruka = decode(line, cesarN);
            System.out.println("Server: " + desifrovanaPoruka);

            // Ako se server oprostio, prekidamo klijent
            if (desifrovanaPoruka.contains("Vidimo se sledeći put")) {
                break;
            }

            // Unos sa konzole
            System.out.print("Vaš unos: ");
            String korisnikovUnos = kin.readLine();

            // Šaljemo šifrovan odgovor serveru
            encodeMessage = encode(korisnikovUnos, cesarN);
            out.println(encodeMessage);

            // Ako je klijent ukucao komandu za kraj, gasimo petlju
            if (korisnikovUnos.equalsIgnoreCase("Ne želim više da igram!")) {
                break;
            }
        }
        socket.close();
        System.out.println("Klijent ugašen.");
    }

    private static String decode(String message, int n) {
        StringBuilder sb = new StringBuilder();
        for (char ch : message.toCharArray()) {
            sb.append((char) (ch - n));
        }
        return sb.toString();
    }

    private static String encode(String message, int n) {
        StringBuilder sb = new StringBuilder();
        for (char ch : message.toCharArray()) {
            sb.append((char) (ch + n));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }
}