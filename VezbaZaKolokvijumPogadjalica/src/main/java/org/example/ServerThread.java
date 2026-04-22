package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ServerThread implements Runnable{
     Server server;
     Socket socket;
     int id;
    private String sifra;
    public ServerThread(Server server, Socket socket, int id){
        this.server = server;
        this.socket = socket;
        this.id = id;
        this.sifra = "raf";
    }


    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);

            String porukaServera;
            String porukaKorisnika;
            int brojPokusajaZaLozinku = 3;
            boolean lozinka = false;
            out.println("Unesite lozinku:");

            while (true){
                porukaKorisnika = in.readLine();
                brojPokusajaZaLozinku--;
                if(brojPokusajaZaLozinku>0) {
                    if (porukaKorisnika.equals(sifra)) {
                        lozinka = true;
                        out.println("Lozinka je ispravna. Dobrodošli u igru anagrama!");
                        break;
                    } else {
                        out.println("Neispravna lozinka. Pokušajte ponovo!");
                    }
                }else{
                    out.println("Žao mi je, više sreće sledeći put.");
                    break;
                }
            }

            if(lozinka==false){
                server.disconnectClient();
                socket.close();
                return;
            }

            porukaKorisnika = in.readLine();
            int n = desifrujN(porukaKorisnika);
            int sifrujN = brojZaSifrovanje(n);
            //System.out.println("Server N: " + n);
            String[][] lista = {
                    {"majstor", "prozor", "trava", "motor", "stolica"},
                    {"fakultet", "ispit", "student", "profesor", "knjiga"},
                    {"banana", "jagoda", "ananas", "malina", "kupina"}
            };

            int odabranaLista = odaberiListu(n);
            boolean igra = true;
            int brojZaIgru=5;
            while (igra){
                Random random = new Random();
                int cnt = random.nextInt(5);
                String rec = lista[odabranaLista][cnt];
                String  obrnutaRec  = anagram(rec);
                porukaServera = sifruj(obrnutaRec,sifrujN);
                out.println(porukaServera);
                while(true){
                    porukaKorisnika = desifruj(in.readLine(),sifrujN);
                    brojZaIgru--;
                    if(brojZaIgru>0) {
                        if (porukaKorisnika.equals(rec)) {
                            porukaServera = sifruj("Čestitam, uspešno ste pogodili reč! Želite li novu reč?", sifrujN);
                            out.println(porukaServera);
                            break;
                        }else{
                            porukaServera = sifruj("Pogrešan odgovor. Pokušajte ponovo!" + brojZaIgru,sifrujN);
                            out.println(porukaServera);
                        }
                    }else{
                        porukaServera = sifruj("Žao mi je, niste uspeli da pogodite reč. Originalna reč je bila: ["+rec+"]. Želite li novu reč?", sifrujN);
                        out.println(porukaServera);
                        break;
                    }
                }
                while(true){
                    porukaKorisnika = desifruj(in.readLine(),sifrujN);
                    if(porukaKorisnika.equals("da")){
                        brojZaIgru=5;
                        break;
                    }else if(porukaKorisnika.equals("ne")){
                        igra = false;
                        porukaServera = sifruj("Vidimo se sledeći put!",sifrujN);
                        out.println(porukaServera);
                        break;
                    }else{
                        porukaServera = sifruj("Unesite da ili ne.",sifrujN);
                        out.println(porukaServera);
                    }
                }
            }





            server.disconnectClient();
            socket.close();
        }catch (Exception e){
            server.disconnectClient();
        }
    }
    private String anagram(String rec){
        char[] res = rec.toCharArray();
        Random random = new Random();
        for(int i = rec.length()-1; i > 0; i--){
            int j=random.nextInt(i+1);
            char temp = res[i];
            res[i] = res[j];
            res[j]=temp;
        }
        return  new String(res);
    }
    private int odaberiListu(int n){
        if (n == 1 || n == 2) {
            return 0;
        }else if (n == 3 || n == 4) {
            return 1;
        }else{
            return 2;
        }
    }
    private int desifrujN(String poruka){
        for(int i=1;i<=6;i++){
            int n = i;
            int sifrovanBroj = brojZaSifrovanje(n);
            String strN = Integer.toString(n);
            String pokusaj = sifruj(strN,sifrovanBroj);
            if(pokusaj.equals(poruka)){
                return n;
            }
        }
        return -1;
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
}
