package org.example;

import java.io.*;
import java.net.Socket;

public class Client {

    public Client()throws IOException {
        Socket socket = new Socket("localhost", 2026);
        String message;

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));


        message = in.readLine();
        System.out.println(message);

        message = kin.readLine();
        out.println(message);

        while (true){
            //Postovani [ime]...
            message = in.readLine();
            System.out.println(message);

            String porukaZaSlanje = kin.readLine();
            out.println(porukaZaSlanje);
            /// Ako je prazna poruka
            if (porukaZaSlanje.trim().isEmpty()) {
                message = in.readLine(); // Čita ono "uneli ste praznu poruku."
                System.out.println(message);
                continue; // Vraća nas na početak while petlje
            }
            /// Citamo MENU
            message = in.readLine();
            System.out.println(message);

            String komanda = kin.readLine();
            out.println(komanda);
            if(komanda.trim().equals("9")){
                break;
            }

        }
        socket.close() ;
        System.out.println("Klijen je diskonektovan");
    }

    public  static void main(String[] args)throws IOException {
        new Client();
    }
}
