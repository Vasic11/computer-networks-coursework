package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerThread implements Runnable{
    private Server server;
    private Socket socket;
    private int brojKlijenata;
    public ServerThread(Server server, Socket socket, int brojKlijenata) {
        this.server = server;
        this.socket = socket;
        this.brojKlijenata = brojKlijenata;
    }

    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);

            String porukaServer;
            String porukaKlijenta;

            porukaServer = "Dobrodosli na server! Unesite koju duzinu reci zalite da pogadjate:";
            out.println(porukaServer);

            String listaReci[] = {"vuk", "pas", "rak", "tigar", "macka", "ptica", "zirafa", "hijena", "kamila"};

            porukaKlijenta = in.readLine();
            int duzinaReci = Integer.parseInt(porukaKlijenta);
            int brojPokusajaZaPogadjanjeReci = brojPokusaja(duzinaReci);


            String recZaPogadjanje;
            String recZaPogadjanjeCrtice;
            boolean igra=true;
            while (igra){
                while (true){
                    Random random = new Random();
                    recZaPogadjanje = listaReci[random.nextInt(9)];
                    recZaPogadjanjeCrtice = "_".repeat(recZaPogadjanje.length());

                    if(recZaPogadjanje.length()==duzinaReci){
                        System.out.println(recZaPogadjanje);
                        out.println(recZaPogadjanjeCrtice);
                        break;
                    };
                }

                while(true) {
                    //out.println(recZaPogadjanjeCrtice);

                    porukaKlijenta = in.readLine();
                    if (porukaKlijenta.length() == 1) {
                        if (recZaPogadjanje.contains(porukaKlijenta)) {
                            char ch = porukaKlijenta.charAt(0);
                            List<Integer> mestaZaPopunjavanje = popuni(recZaPogadjanje, ch);
                            char[] oldRec = recZaPogadjanjeCrtice.toCharArray();
                            for (int i : mestaZaPopunjavanje) {
                                oldRec[i] = ch;
                            }
                            recZaPogadjanjeCrtice = new String(oldRec);
                            if (!recZaPogadjanjeCrtice.contains("_")) {
                                out.println("Uspesno ste pogodili rec:" + recZaPogadjanje + " Da li zelite novu rec?");
                                break;
                            }
                            out.println("Uspesno pogodjeno slovo: " + ch + " novi izgled za dalje pogadjanje:[" + recZaPogadjanjeCrtice + "]");
                        } else {
                            brojPokusajaZaPogadjanjeReci--;
                            if (brojPokusajaZaPogadjanjeReci == 0) {
                                out.println("Nemate vise pokusaja.");
                                break;
                            }else {
                                out.println("Uneto slovo ne postoji, pokuajte ponovo.");
                            }
                        }
                    } else {
                        if (porukaKlijenta.contains("Ne zelim")) {
                            igra = false;
                            break;
                        }

                    }
                }
                    if(igra!=false){
                        porukaKlijenta = in.readLine();
                        if(porukaKlijenta.equals("DA")){
                            out.println("Unesite duzinu reci za pogadjanje:");
                            porukaKlijenta = in.readLine();
                            duzinaReci = Integer.parseInt(porukaKlijenta);
                            brojPokusajaZaPogadjanjeReci = brojPokusaja(duzinaReci);
                        }else{
                            igra=false;
                            break;
                        }


                    }


                }



            server.disconnect();
            socket.close();
        }catch (Exception e){
            server.disconnect();
        }
    }
    private List<Integer>popuni(String rec, char slovo){
        List<Integer>mesta = new ArrayList<>();
        for(int i=0; i<rec.length(); i++){
            if(rec.charAt(i)==slovo){
                mesta.add(i);
            }
        }
        return mesta;
    }

    private int brojPokusaja(int n){
        if(n==3){
            return 6;
        }else if(n==5){
            return 10;
        }else{
            return 12;
        }
    }

}
