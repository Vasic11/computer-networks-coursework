package org.example;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Client {
    public Client()throws IOException {
        Socket socket = new Socket("localhost",2026);
        BufferedReader in =new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));


        String porukaKorisnika;
        String porukaServera;
        boolean pogodjenPin=false;
        porukaServera=in.readLine();
        System.out.println(porukaServera);

        while (true){
            porukaKorisnika = kin.readLine();
            out.println(porukaKorisnika);

            porukaServera=in.readLine();
            System.out.println(porukaServera);
            if(porukaServera.contains("tacan")){
                pogodjenPin=true;
                break;
            }else if(porukaServera.contains("odbacen")){
                break;
            }
        }
        if(pogodjenPin==false){
            kin.close();
            socket.close();
            return;
        }
        Random random = new Random();
        int m = random.nextInt(8)+1;
        int cesarM = cesar_m(m);
        out.println(m);
        boolean igra=true;
        while(igra){
            porukaServera=desifruj(in.readLine(),cesarM);
            System.out.println(porukaServera);

            while (true){
                porukaKorisnika = sifruj(kin.readLine(),cesarM);
                out.println(porukaKorisnika);

                porukaServera=desifruj(in.readLine(),cesarM);
                System.out.println(porukaServera);

                if(porukaServera.contains("Bravo") || porukaServera.contains("Nazalost")){
                    break;
                }
            }

            porukaKorisnika = kin.readLine();
            String odgovor = porukaKorisnika;
            porukaKorisnika = sifruj(porukaKorisnika,cesarM);
            out.println(porukaKorisnika);
            if(odgovor.equals("NE")){
                igra=false;
                break;
            }


        }
        socket.close();
        kin.close();


    }

    private static int cesar_m(int m){
        int cesarM;
        if(m<=2){
            cesarM = m+4;
        }else if(m>=6){
            cesarM = m-3;
        }else{
            cesarM = m+1;
        }
        return cesarM;
    }



    private String sifruj(String rec, int cesarM){
        StringBuilder stringBuilder = new StringBuilder();
        for(char ch : rec.toCharArray()){
            stringBuilder.append((char) (ch+cesarM));
        }
        return stringBuilder.toString();
    }
    private String desifruj(String rec, int cesarM){
        StringBuilder stringBuilder = new StringBuilder();
        for(char ch : rec.toCharArray()){
            stringBuilder.append((char) (ch-cesarM));
        }
        return stringBuilder.toString();
    }

    static void main(String[] args)throws Exception {
        new Client();
    }
}
