package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int brojKlijenata;
    private static int aktivniKlijenti;
    public Server()throws Exception {
        ServerSocket serverSocket = new ServerSocket(2026);
        System.out.println("Server je pokrenut...");
        brojKlijenata = 0;
        aktivniKlijenti = 0;
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));
        serverSocket.setSoTimeout(1000);
        while (true){

            if(brojKlijenata!=0 && aktivniKlijenti==0){
                System.out.println("Nema vise aktivnih klijenata. Da li zelite da ugasite server? (Da/Ne)");
                String odgovor = kin.readLine();
                if (odgovor.equals("Da")) {
                    break;
                }
            }

            try{
                Socket socket =  serverSocket.accept();
                ServerThread serverThread = new ServerThread(this,socket,++brojKlijenata);
                Thread thread = new Thread(serverThread);
                thread.start();
                connect();
                System.out.println("Konektovao se klijent: " + brojKlijenata);

            }catch(Exception e){

            }

        }
        kin.close();
        serverSocket.close();
    }

    public void connect(){
        aktivniKlijenti++;
    }
    public void disconnect(){
        aktivniKlijenti--;
    }

    static void main(String[] args)throws Exception {
        new Server();
    }
}
