package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerThread implements Runnable{
    Server server;
    Socket socket;
    String loznka;
    int brojPokusaja;
    int redniBrojKorinsika;
    int pokusajZaPogadjanjeReci;

    public ServerThread(Server server, Socket socket,int redniBrojKorinsika) {
        this.server = server;
        this.socket = socket;
        this.redniBrojKorinsika = redniBrojKorinsika;
        this.loznka = "raf";
        this.brojPokusaja = 3;
    }

    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);

            String porukaServera;
            porukaServera = "Unesite lozinku";
            out.println(porukaServera);
            boolean unetaLozinka = false;
            String porukaKorisnika;
            while (brojPokusaja>0){
                porukaKorisnika = in.readLine();
                brojPokusaja--;
                if(porukaKorisnika.equals(loznka)){
                    porukaServera = "Lozinka je ispravna. Dobrodošli u igru anagrama!";
                    out.println(porukaServera);
                    unetaLozinka = true;
                    break;
                }else if(brojPokusaja>0){
                    porukaServera = "Neispravna lozinka. Pokušajte ponovo!";
                    out.println(porukaServera);
                }else{
                    porukaServera = "Žao mi je, više sreće sledeći put.";
                    out.println(porukaServera);
                }
            }

            if(unetaLozinka == false){
                server.disconnect();
                socket.close();
                return;
            }

            porukaKorisnika = in.readLine();
            String brojN  = porukaKorisnika;
            System.out.println("BrojN"+brojN);
            int n = Integer.parseInt(brojN);
            System.out.println(n);
            ///Broj za koji se siftuje
            int cesarN;
            if(n<2){
                cesarN = n+3;
            }else if(n==3 || n==4){
                cesarN = n;
            }else{
                cesarN = n-2;
            }
            /// Koja se lista bira
            int odabirListe;
            if(n==1 || n==2){
                odabirListe=0;
            }else if(n==3 || n==4){
                odabirListe=1;
            }else{
                odabirListe=2;
            }


            String[][] lista ={
                    {"majstor", "prozor", "trava", "motor", "stolica"},
                    {"fakultet", "ispit", "student", "profesor", "knjiga"},
                    {"banana", "jagoda", "ananas", "malina", "kupina"}
            };
            String[] odabranaLista =lista[odabirListe];
            boolean igra=true;
            while (igra){
                pokusajZaPogadjanjeReci=5;
                Random random = new Random();
                String odabranaRec = odabranaLista[random.nextInt(5)];
                String anagramRec = napraviAnagram(odabranaRec);
                porukaServera = sifruj(anagramRec, cesarN);
                out.println(porukaServera);
                while (true) {
                    porukaKorisnika = desifruj(in.readLine(),cesarN);
                    if (porukaKorisnika.equalsIgnoreCase("Ne želim više da igram!")) {
                        igra = false;
                        break;
                    }
                    pokusajZaPogadjanjeReci--;
                    if (porukaKorisnika.equals(odabranaRec)) {
                        porukaServera = sifruj("Čestitam, uspešno ste pogodili reč! Želite li novu reč?", cesarN);
                        out.println(porukaServera);
                        break;
                    } else {
                        if(pokusajZaPogadjanjeReci>0) {
                            porukaServera = sifruj("Pogrešan odgovor. Pokušajte ponovo! Imate jos: " + pokusajZaPogadjanjeReci + " pokusaja", cesarN);
                            out.println(porukaServera);
                        }else{
                            porukaServera = sifruj("Žao mi je, niste uspeli da pogodite reč. Originalna reč je bila:[" + odabranaRec + "]. Želite li novu reč?", cesarN);
                            out.println(porukaServera);
                            break;
                        }

                    }
                }
                if(!igra)break;
                porukaKorisnika = desifruj(in.readLine(),cesarN);
                if(porukaKorisnika.equalsIgnoreCase("Ne")){
                    porukaServera = sifruj("Vidimo se sledeći put!",cesarN);
                    out.println(porukaServera);
                    igra=false;
                }else if(porukaKorisnika.equalsIgnoreCase("Da")){
                    odabirListe = (odabirListe+1)%3;
                    odabranaLista = lista[odabirListe];
                }


            }


            server.disconnect();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
            server.disconnect();
        }
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

    private String napraviAnagram(String rec){
        char[] slova = rec.toCharArray();
        Random rand = new Random();

        for(int i=slova.length-1;i>0;i--){
            int j = rand.nextInt(i+1);
            char temp = slova[i];
            slova[i] = slova[j];
            slova[j] = temp;
        }
        return new String(slova);

    }
}
