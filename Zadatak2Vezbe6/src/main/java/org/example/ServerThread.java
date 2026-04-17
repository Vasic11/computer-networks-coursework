package org.example;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ServerThread implements Runnable{
    Server server;
    Socket socket;
    int ukupanBrojKorisnika;
    private int brojPouksajaZaPin;
    private String pin;
    int brojPoksajaIgre;
    public ServerThread(Server server, Socket socket, int ukupanBrojKorrisnika){
        this.server=server;
        this.socket=socket;
        this.ukupanBrojKorisnika=ukupanBrojKorrisnika;
        this.pin = "123";
    }

    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);

            brojPouksajaZaPin = 4;
            String porukaServera;
            porukaServera = "Unesite PIN:";
            out.println(porukaServera);
            boolean pogodjenPin=false;
            String porukaKorisnika;
            while (true){
                porukaKorisnika=in.readLine();
                brojPouksajaZaPin--;
                if(brojPouksajaZaPin>0){
                    if(porukaKorisnika.equals(pin)){
                        out.println("Pin kod je tacan. Dobordosli u igru palindorma.");
                        pogodjenPin=true;
                        break;

                    }else{
                        out.println("Neispravan PIN, pokusajte ponovo.");
                    }
                }else{
                    out.println("Pristup je odbacen. Pokusajte kasnije.");
                    break;
                }
            }

            if(pogodjenPin==false){
                server.disconnect();
                socket.close();
                return;
            }

            String intM = in.readLine();
            int m = Integer.parseInt(intM);
            int cesarM = cesar_m(m);

            String[][] liste = {
                    {"ana", "kapak", "potop", "radar", "kazak"},
                    {"aluala", "dovod", "savas", "udaradu", "mamam"},
                    {"anona", "abuuba", "azooza", "akoka", "arera"}
            };

            int brojListe = biranjeListe(m);

            System.out.println("Izabrana lista: " + brojListe);
            boolean igra = true;
            brojPoksajaIgre=4;
            while (igra){
                Random random = new Random();
                String rec = liste[brojListe][random.nextInt(5)];
                String skrivenaRec = maskirajRec(rec);
                porukaServera = sifruj(skrivenaRec,cesarM);
                out.println(porukaServera);

                while (true){
                    porukaKorisnika = desifruj(in.readLine(),cesarM);
                    brojPoksajaIgre--;
                    if(brojPoksajaIgre>0){
                        if(porukaKorisnika.equals(rec)){
                            porukaServera = sifruj("Bravo, uspesno ste resili palindrom. Zelite li novi izazov?",cesarM);
                            out.println(porukaServera);
                            break;
                        }else{
                            porukaServera = sifruj("Netacan odgovor, pokusajte ponovo." + brojPoksajaIgre,cesarM);
                            out.println(porukaServera);
                        }
                    }else{
                        porukaServera = sifruj("Nazalost niste uspeli da ppogodite palnirdom. Tacan odgovor je: " + rec + "Da li zelite novu rec?",cesarM);
                        out.println(porukaServera);
                        break;
                    }
                }

                porukaKorisnika = desifruj(in.readLine(),cesarM);
                if(porukaKorisnika.equals("DA")){
                    brojListe = (brojListe+1)%3;
                    brojPoksajaIgre=4;
                }else{
                    porukaServera = sifruj("Hvala na igri! Do sledeceg puta!",cesarM);
                    igra = false;
                    out.println(porukaServera);
                }

            }

            server.disconnect();
            socket.close();
        }catch (Exception e){
            server.disconnect();
        }
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
    private int biranjeListe(int m){
        if(m>=1 || m<=3){
            return  0;
        }else if(m>=4 && m<=6){
            return  1;
        }else{
            return  2;
        }
    }

    private static String maskirajRec(String rec){
        Random random = new Random();
        char[] zamenjenaRec = rec.toCharArray();
        int brojTackica = random.nextInt(rec.length());
        if(brojTackica==0){
            brojTackica=1;
        }
        int brojStavljanja;

        Set<Integer> mestaTackica = new HashSet<>();
        while (brojTackica>mestaTackica.size()){
            brojStavljanja = random.nextInt(rec.length());
            mestaTackica.add(brojStavljanja);


        }
        for(int i : mestaTackica){
            zamenjenaRec[i] = '*';
        }

        return  new String(zamenjenaRec);
    }
}
