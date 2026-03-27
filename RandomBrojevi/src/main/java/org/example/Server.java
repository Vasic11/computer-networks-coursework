package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int noviKlijent;
    private static int konektovaniKlijenti;
    public Server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(2026);

        System.out.println("Server started. In port: "+ serverSocket.getLocalPort());
        noviKlijent=0;
        konektovaniKlijenti=0;
        int redniBroj=0;
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));
        while (true){

            if(noviKlijent!=redniBroj && konektovaniKlijenti==0){
                System.out.println("Da li server zeli da bude aktivan(Da/Ne):");
                String odgovor = kin.readLine();
                redniBroj = noviKlijent;
                if(odgovor.equals("Ne")){
                    break;
                }
            }
            try {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(this,socket, noviKlijent);
                Thread thread = new Thread(serverThread);
                thread.start();

                System.out.println("Konektovao se klijent " + noviKlijent++);
                konektovanKlijent();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        serverSocket.close();
        kin.close();

    }
    public void konektovanKlijent(){
          konektovaniKlijenti++;
    }
    public void diskonetovanKlijent(){
        konektovaniKlijenti--;
    }
    public  void aktivni(){
        System.out.println("Aktivni: " + konektovaniKlijenti);
    }

    public static void main(String[] args)throws IOException {
        new Server();
    }
}
