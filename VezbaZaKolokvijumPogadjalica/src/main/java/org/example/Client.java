package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Client {


    public Client()throws IOException {
        Socket socket = new Socket("localhost",2026);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));


        String porukaServera;
        String porukaKorisnika;
        porukaServera = in.readLine();
        boolean lozinka = false;
        System.out.println(porukaServera);

        while (true){
            porukaKorisnika = kin.readLine();
            out.println(porukaKorisnika);

            porukaServera = in.readLine();
            System.out.println(porukaServera);
            if(porukaServera.contains("Dobrodošli")){
                lozinka = true;
                break;
            }
            if(porukaServera.contains("Žao")){
                break;
            }
        }
        if(lozinka==false){
            kin.close();
            socket.close();
            return;
        }

        Random random = new Random();
        int n = random.nextInt(6)+1;
        //System.out.println("Klijent N: " + n);
        int sifrovanBroj = brojZaSifrovanje(n);
        String strN = Integer.toString(n);
        porukaKorisnika = sifruj(strN,sifrovanBroj);
        out.println(porukaKorisnika);
        boolean igra = true;
        //porukaServera = desifruj(in.readLine(),sifrovanBroj);
        //System.out.println(porukaServera);
        while (igra){
            porukaServera = desifruj(in.readLine(),sifrovanBroj);
            System.out.println(porukaServera);

            while (true){
                porukaKorisnika = sifruj(kin.readLine(),sifrovanBroj);
                out.println(porukaKorisnika);

                porukaServera = desifruj(in.readLine(),sifrovanBroj);
                System.out.println(porukaServera);
                if(porukaServera.contains("Čestitam") || porukaServera.contains("Žao")){
                    break;
                }
            }
            while (true){
                porukaKorisnika = sifruj(kin.readLine(),sifrovanBroj);
                out.println(porukaKorisnika);
                porukaServera = desifruj(in.readLine(),sifrovanBroj);
                System.out.println(porukaServera);
                if(porukaServera.contains("Vidimo")){
                    igra = false;
                    break;
                }
                if(porukaKorisnika.equals("da")){
                    break;
                }
            }

        }

        kin.close();
        socket.close();
    }


    private static int brojZaSifrovanje(int n){
        if(n<=2){
            return n+3;
        }else if(n>4){
            return n-2;
        }else{
            return n;
        }
    }

    private static String sifruj(String rec, int brojZaSifrovanje){
        StringBuilder sb=  new StringBuilder();
        for(char ch:rec.toCharArray()){
            sb.append((char)(ch+brojZaSifrovanje));
        }
        return sb.toString();
    }
    private static String desifruj(String rec, int brojZaSifrovanje){
        StringBuilder sb=  new StringBuilder();
        for(char ch:rec.toCharArray()){
            sb.append((char)(ch-brojZaSifrovanje));
        }
        return sb.toString();
    }

    static void main(String[] args)throws IOException {
        new Client();
    }


}
