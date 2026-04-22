package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Client {
    public Client()throws Exception{
        Socket socket = new Socket("localhost",2026);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));

        String porukaServera = in.readLine();
        System.out.println(porukaServera);
        boolean start = false;
        while (!start){
            String porukaKlijenta = kin.readLine();
            out.println(porukaKlijenta);

            porukaServera = in.readLine();
            System.out.println(porukaServera);
            if(porukaServera.contains("počela!")){
                start = true;
            }
        }
        String porukaKlijenta;
        while (start){
            while (true){
                porukaKlijenta = kin.readLine();
                out.println(porukaKlijenta);

                porukaServera = in.readLine();
                System.out.println(porukaServera);

                if(porukaServera.contains("Cestitamo") || porukaServera.contains("Nemate")){
                    break;
                }
            }
            while (true){
                porukaKlijenta = kin.readLine();
                out.println(porukaKlijenta);
                if(porukaKlijenta.equals("da")){
                    break;
                }else{
                    porukaServera = in.readLine();
                    System.out.println(porukaServera);
                    start=false;
                    break;
                }
            }
        }

        kin.close();
        socket.close();
    }



    static void main(String[] args)throws Exception {
        new Client();
    }
}
