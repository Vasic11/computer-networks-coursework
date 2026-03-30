package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int brojKlikenta;
    private static int aktivniKlijenti;
    public Server()throws Exception{
        ServerSocket serverSocket = new ServerSocket(75%24+2000);
        serverSocket.setSoTimeout(1000);
        System.out.println("Server started... Port:"+serverSocket.getLocalPort());
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));
        brojKlikenta=0;
        aktivniKlijenti=0;

        while(true) {
            if(aktivniKlijenti==0 && brojKlikenta!=0){
                System.out.println("Da li zelite da server ostane aktivan:");
                String poruka = kin.readLine();
                if(poruka.equals("ne zelim")){
                    break;
                }
            }
            try {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(this,socket,++brojKlikenta);
                Thread thread = new Thread(serverThread);
                thread.start();

                System.out.println("Konektovao se klijent");
                newClient();
            } catch (Exception e) {

            }
        }
        serverSocket.close();
        kin.close();
    }

    public void newClient(){
        aktivniKlijenti++;
    }
    public void disconnect(){
        aktivniKlijenti--;
    }
    static void main(String[] args)throws Exception {
        new Server();
    }
}
