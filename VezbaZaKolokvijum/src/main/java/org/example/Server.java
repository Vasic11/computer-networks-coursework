package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int aktivniKlijenti;
    private static int id;
    public Server()throws IOException {
        ServerSocket serverSocket = new ServerSocket(2026);
        System.out.println("Server started...");
        serverSocket.setSoTimeout(1000);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));
        aktivniKlijenti=0;
        id=0;
        while (true) {
            if(aktivniKlijenti==0 && id!=0){
                System.out.println("Da li da ugasimo Server?");
                String odgovor = kin.readLine();
                if(odgovor.equalsIgnoreCase("da")){
                    break;
                }
            }
            try{
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(this, socket, ++id);
                Thread thread = new Thread(serverThread);
                thread.start();
                connectClient();
                System.out.println("Konekotvao se klijent: "+ id);
            }catch (Exception e){

            }
        }
        kin.close();
        serverSocket.close();
    }

    public void connectClient(){
        aktivniKlijenti++;
    }
    public void disconnectClient(){
        aktivniKlijenti--;
    }
    static void main(String[] args)throws Exception {
        new Server();
    }
}
