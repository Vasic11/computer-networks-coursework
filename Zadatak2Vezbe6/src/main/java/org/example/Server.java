package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.spi.AbstractResourceBundleProvider;

public class Server {
    private static int aktivniKorisnici;
    private static int ukupanBrojKorisnika;
    public Server()throws Exception{
        ServerSocket serverSocket = new ServerSocket(2026);
        System.out.println("Server je pokrenut...");
        aktivniKorisnici = 0;
        ukupanBrojKorisnika = 0;
        serverSocket.setSoTimeout(1000);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (aktivniKorisnici == 0 && ukupanBrojKorisnika != 0) {
                System.out.println("Da li zelite da server ostane aktivan? (Da/Ne)");
                String odgovor = kin.readLine();
                if (odgovor.equalsIgnoreCase("ne")) {
                    break;
                }
            }
            try {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(this,socket,++ukupanBrojKorisnika);
                Thread thread = new Thread(serverThread);
                thread.start();
                connect();
                System.out.println("Dodat novi korisnik: " + ukupanBrojKorisnika);



            }catch (Exception e){

            }
        }
        kin.close();
        serverSocket.close();

    }
    public void connect(){
        aktivniKorisnici++;
    }
    public void disconnect(){
        aktivniKorisnici--;
    }
    static void main(String[] args)throws Exception {
        new Server();
    }

}
