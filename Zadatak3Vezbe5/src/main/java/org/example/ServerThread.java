package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerThread implements Runnable {
    private Server server;
    private Socket socket;
    private int ukupanBrojKlijenta;

    public ServerThread(Server server, Socket socket, int ukupanBrojKlijent) {
        this.server = server;
        this.socket = socket;
        this.ukupanBrojKlijenta = ukupanBrojKlijent;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            Random random = new Random();

            // 1. Čitamo n koje je klijent poslao
            String nStr = in.readLine();
            if (nStr == null) return;
            int n = Integer.parseInt(nStr);
            int cesar_n = n < 3 ? n + 2 : n == 3 ? n - 2 : n + n;

            // 2. Čitamo inicijalnu poruku (Želim da igram!, itd.)
            String odgovor = in.readLine();
            String desifrovanaPoruka = decode(odgovor, cesar_n);

            int wordList_n = n < 3 ? 0 : n == 3 ? 1 : 2;
            String[][] wordLists = {
                    { "jabuka", "breskva", "mango", "limun", "kajsija" },
                    { "krastavac", "paradajz", "kupus", "rotkvica", "batat" },
                    { "toto", "milka", "plazma", "kinder", "snikers" }
            };

            String[] wordList = wordLists[wordList_n];
            boolean run = true;

            while (run) {
                String word = wordList[random.nextInt(5)];
                String guess = "_".repeat(word.length());

                // Šaljemo početno stanje
                out.println(encode("Novi izgled za dalje pogađanje: [" + guess + "]", cesar_n));

                // Petlja za pogađanje OVE reči
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        run = false;
                        break;
                    }

                    desifrovanaPoruka = decode(line, cesar_n);

                    // Komanda za izlaz
                    if (desifrovanaPoruka.equalsIgnoreCase("Ne želim više da igram!")) {
                        out.println(encode("Vidimo se sledeći put!", cesar_n));
                        run = false;
                        break;
                    }

                    if (desifrovanaPoruka.length() == 1) {// Tehnika za spajanje stare reci i nove reci nakon dodatog pogodjenog slova
                        char ch = desifrovanaPoruka.charAt(0);
                        List<Integer> positio = findPositions(word, ch);
                        String oldGuess = guess;

                        char[] guessArr = guess.toCharArray();
                        for (Integer pos : positio) {
                            guessArr[pos] = ch;
                        }
                        guess = new String(guessArr);

                        if (guess.equals(oldGuess)) {
                            // Promašaj - Igra se nastavlja
                            out.println(encode("Žao mi je, uneto slovo ne postoji u reči. Pokušajte ponovo!", cesar_n));
                        } else if (!guess.contains("_")) {
                            // Pobeda - izlazak iz petlje za pogađanje
                            out.println(encode("Čestitam, reč je: [" + guess + "]. Da li želite novu reč za pogađanje? (DA/NE)", cesar_n));
                            break;
                        } else {
                            // Pogođeno slovo
                            out.println(encode("Uspešno pogođeno slovo " + ch + " i novi izgled za dalje pogađanje: [" + guess + "]", cesar_n));
                        }
                    } else {
                        out.println(encode("Unesite samo jedno slovo ili komandu za prekid.", cesar_n));
                    }
                }

                // Odgovor DA/NE za sledeću partiju
                if (run) {
                    String line = in.readLine();
                    if (line != null) {
                        desifrovanaPoruka = decode(line, cesar_n);
                        if (desifrovanaPoruka.equalsIgnoreCase("NE")) {
                            out.println(encode("Vidimo se sledeći put!", cesar_n));
                            run = false; // Gasi se klijent
                        }
                    } else {
                        run = false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Klijent " + ukupanBrojKlijenta + " je prekinuo vezu.");
        } finally {
            server.disconnectedClient();
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Integer> findPositions(String word, char message) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == message) {
                positions.add(i);
            }
        }
        return positions;
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
}