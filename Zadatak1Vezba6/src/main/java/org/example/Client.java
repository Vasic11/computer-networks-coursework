package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Client {

    public Client()throws IOException{
        Socket socket = new Socket("localhost",2026);

        BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));

        String porukaServera;
        porukaServera = in.readLine();
        System.out.println(porukaServera);
        String porukaKorisnika;
        boolean ispravnaLozinka = false;
        while (true){
            porukaKorisnika = kin.readLine();
            out.println(porukaKorisnika);

            porukaServera = in.readLine();
            System.out.println(porukaServera);
            if(porukaServera.equals("Lozinka je ispravna. Dobrodošli u igru anagrama!")){
                ispravnaLozinka = true;
                break;
            }
            if(porukaServera.equals("Žao mi je, više sreće sledeći put.")){
                break;
            }
        }
        System.out.println("Izasli smo iz lozinke");
        if(ispravnaLozinka == false){
            kin.close();
            socket.close();
            return;
        }

        Random random = new Random();
        int n =random.nextInt(6)+1;
        int cesarN;
        if(n<2){
            cesarN = n+3;
        }else if(n==3 || n==4){
            cesarN = n;
        }else{
            cesarN = n-2;
        }


        porukaKorisnika = Integer.toString(n);
        out.println(porukaKorisnika);
        boolean igra= true;

        while (igra){
            porukaServera = desifruj(in.readLine(),cesarN);
            System.out.println(porukaServera);
            while (true) {
                porukaKorisnika = sifruj(kin.readLine(), cesarN);
                out.println(porukaKorisnika);
                if (porukaKorisnika.equalsIgnoreCase("Ne želim više da igram!")) {
                    igra = false;
                    break;
                }
                porukaServera = desifruj(in.readLine(), cesarN);
                System.out.println(porukaServera);

                if(porukaServera.contains("Čestitam") || porukaServera.contains("Žao mi je")){
                    break;
                }
            }
            if(!igra){break;}
            porukaKorisnika = kin.readLine();
            out.println(sifruj(porukaKorisnika,cesarN));
            if(porukaKorisnika.equalsIgnoreCase("Ne")){
                igra = false;
                porukaServera = desifruj(in.readLine(),cesarN);
                System.out.println(porukaServera);
            }

        }

        kin.close();
        socket.close();
    }


    private String sifruj(String rec, int n){
        StringBuilder sb = new StringBuilder();
        for(char ch : rec.toCharArray()){
            sb.append((char) (ch+n));
        }
        return sb.toString();
    }
    private String desifruj(String rec, int n){
        StringBuilder sb = new StringBuilder();
        for(char ch : rec.toCharArray()){
            sb.append((char) (ch-n));
        }
        return sb.toString();
    }

    static void main(String[] args)throws IOException {
        new Client();
    }

}
