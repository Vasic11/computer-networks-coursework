package org.example;

import java.io.*;
import java.net.Socket;

public class Client {
    public Client()throws IOException {
        Socket socket = new Socket("localhost",2026);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));

        String porukaServera;
        String porukaKlijenta;

        porukaServera = in.readLine();
        System.out.println(porukaServera);

        while (true){
            porukaKlijenta = kin.readLine();

            if(porukaKlijenta.equals("3") ||  porukaKlijenta.equals("5") || porukaKlijenta.equals("6")){
                out.println(porukaKlijenta);
                break;
            }
            else{
                System.out.println("Ta duzina reci nepostoji. Unesite ponovo:");
            }
        }

        boolean igra;
        porukaServera = in.readLine();
        System.out.println(porukaServera);
        while (true){

            porukaKlijenta = kin.readLine();
            out.println(porukaKlijenta);

            porukaServera = in.readLine();
            System.out.println(porukaServera);

            if(porukaServera.contains("Nemate") || porukaKlijenta.contains("Ne zelim")){
                break;
            }
            if(porukaServera.contains("Uspesno")){
                //porukaKlijenta = kin.readLine();
                //out.println(porukaKlijenta);
                if(porukaKlijenta.equals("NE")){
                    break;
                }
            }

        }

        kin.close();
        socket.close();
    }


    static void main(String[] args)throws IOException {
        new Client();
    }
}
