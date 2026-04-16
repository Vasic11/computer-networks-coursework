package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.spi.AbstractResourceBundleProvider;

public class Server {
        private static int redniBrojKorinsika;
        private static int aktivniKorinsici;


        public Server()throws IOException {
            ServerSocket serverSocket = new ServerSocket(2026);
            System.out.println("Server started...");
            serverSocket.setSoTimeout(1000);
            BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));
            redniBrojKorinsika = 0;
            aktivniKorinsici = 0;
            while (true) {
                if (redniBrojKorinsika != 0 && aktivniKorinsici == 0) {
                    System.out.println("Da li zelite da server ostane aktivan? (Da/Ne)");
                    String odgovor = kin.readLine();
                    if (odgovor.equals("Ne")) {
                        break;
                    }
                }

                try{
                    Socket socket = serverSocket.accept();
                    ServerThread serverThread = new ServerThread(this,socket,++redniBrojKorinsika);
                    Thread thread = new Thread(serverThread);
                    thread.start();
                    connect();
                    System.out.println("Konektovao se klijent: " + redniBrojKorinsika);


                }catch(Exception e){

                }

            }
        }


        public void connect(){
            aktivniKorinsici++;
        }
        public void disconnect(){
            aktivniKorinsici--;
        }

    static void main(String[] args)throws IOException {
        new Server();
    }
}
