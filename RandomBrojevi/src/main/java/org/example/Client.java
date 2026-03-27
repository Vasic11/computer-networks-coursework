package org.example;

import java.io.*;
import java.net.Socket;

public class Client {


    public Client()throws IOException {
        Socket socket = new Socket("localhost", 2026);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));

        String message;
        boolean sifra = false;
        while (!sifra) {
            //System.out.println("Unesite lozinku:");
            message = in.readLine();
            System.out.println(message);
            String unos = kin.readLine();
            out.println(unos);
            message = in.readLine();
            if(message.equals("Uspesno ste ulogovali.")){
                sifra = true;
            }
            if(message.contains("Iskoristili"))break;
        }

        if(sifra){
            message = in.readLine();
            System.out.println(message);
            if(message.contains("jednaki")){
                while (true){
                    System.out.println("Unesite poruku za server: ");
                    message = kin.readLine();
                    out.println(message);
                    if(message.equals("ne zelim vise da igram"))break;
                    message = in.readLine();
                    System.out.println(message);
                }
            }
        }
        socket.close();
    }

    static void main(String[] args)throws IOException {
        new Client();
    }
}
