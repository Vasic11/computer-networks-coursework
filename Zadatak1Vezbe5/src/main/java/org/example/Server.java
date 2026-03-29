package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.spi.AbstractResourceBundleProvider;

public class Server {
    private  static int brojKlijenta;
    private static int aktivniKlijent;



    public Server()throws IOException {
        ServerSocket serverSocket = new ServerSocket(75%24+2000);
        serverSocket.setSoTimeout(1000);
        System.out.println("Server started... Port: "+serverSocket.getLocalPort());
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));
        brojKlijenta = 0;
        aktivniKlijent = 0;
        while (true) {

            if(brojKlijenta!=0 && aktivniKlijent==0){
                System.out.println("Da li server zeli da bude aktivan?");
                String odgovor = kin.readLine();
                if(odgovor.contains("Ne")){
                    break;
                }
            }

        try {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(this, socket, ++brojKlijenta);
            Thread thread = new Thread(serverThread);
            thread.start();

            System.out.println("Konekrovao se klijent sa rednim brojem: " + brojKlijenta);
            newClient();
        }catch (IOException e){

        }

        }
        serverSocket.close();
        kin.close();



    }

    public void newClient(){
        aktivniKlijent++;
    }
    public void disconnect(){
        aktivniKlijent--;
    }
    static void main(String[] args)throws Exception {
        new Server();
    }
}
