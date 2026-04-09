package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int aktivniKlijent;
    private int ukupanBrojKlijenata;

    public Server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(2026);
        System.out.println("Server slusa na portu: " + serverSocket.getLocalPort());
        aktivniKlijent = 0;
        ukupanBrojKlijenata = 0;

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                connectedClient(); // Prvo povećamo broj aktivnih
                ServerThread serverThread = new ServerThread(this, socket, ++ukupanBrojKlijenata);
                Thread thread = new Thread(serverThread);
                thread.start();
                System.out.println("Konektovao se klijent broj: " + ukupanBrojKlijenata + ". Trenutno aktivnih: " + aktivniKlijent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Dodajemo 'synchronized' kako bismo sigurno baratali brojačem iz više niti
    public synchronized void connectedClient() {
        aktivniKlijent++;
    }

    public synchronized void disconnectedClient() {
        aktivniKlijent--;
        System.out.println("Klijent se diskonektovao. Trenutno aktivnih: " + aktivniKlijent);

        // Ako više nema aktivnih klijenata, pitamo admina šta da radimo
        if (aktivniKlijent == 0) {
            // Pravimo posebnu nit za čitanje sa tastature da ne blokiramo glavni ServerSocket
            new Thread(() -> {
                try {
                    BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("\nSvi klijenti su diskonektovani.");
                    System.out.println("Unesite 'želim' ako želite da server ostane aktivan, ili 'ne želim' za gašenje:");

                    String odgovor = kin.readLine();
                    if (odgovor != null && odgovor.equalsIgnoreCase("ne želim")) {
                        System.out.println("Gasim server...");
                        System.exit(0); // Prisilno gasimo čitavu Java aplikaciju
                    } else {
                        System.out.println("Server ostaje aktivan i čeka nove klijente...");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static void main(String[] args) throws Exception {
        new Server();
    }
}