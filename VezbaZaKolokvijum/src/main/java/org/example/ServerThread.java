package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketPermission;
import java.util.Arrays;
import java.util.Random;

public class ServerThread implements Runnable {
    Server server;
    Socket socket;
    int id;
    public ServerThread(Server server, Socket socket, int id){
        this.server = server;
        this.socket = socket;
        this.id = id;
    }
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            String[] opcijeBrojeva = {"1","3","5","7","9"};
            int brojPokusaja = 6;


            out.println("Dobrodošli u igru NumeroLogika! Za početak igre unesite 'start'.");
            boolean start = false;
            String [] niz;
            while (!start){
                String porukaKlijenta = in.readLine();
                if(porukaKlijenta.equals("start")){
                    out.println("Igra je počela! Imate 6 pokušaja da pogodite niz od 4 broja. Dostupni brojevi su samo neparni: 1, 3, 5, 7, 9.");
                    start = true;
                }else{
                    out.println("Niste uneli start.");
                }
            }
            String porukaKlijenta;
            while (start){
                niz = randomNiz(opcijeBrojeva);
                while (true){
                    porukaKlijenta = in.readLine();
                    String samoBrojevi = porukaKlijenta.replaceAll("[^0-9]+", " ").trim();
                    String[] nizKlienta = samoBrojevi.split("\\s+");
                    char[] ch = samoBrojevi.toCharArray();
                    ispisNiza(nizKlienta);
                    brojPokusaja--;
                    if(brojPokusaja>0) {
                        if (nizKlienta.length > 4 || nizKlienta.length < 4) {
                            out.println("Nije dobar unos treba  4 broja. " + brojPokusaja);
                            continue;
                        }else if (imaParne(nizKlienta)) {
                            out.println("Uneli ste paran broj. " + brojPokusaja);
                            continue;
                        }else{
                            if(Arrays.equals(nizKlienta,niz)){//nizKlienta.equals(niz) ||
                                out.println("Cestitamo pogodili ste niz brojeva: " + niz[0] + niz[1] + niz[2] + niz[3] + " Da li zelite novu igru?");
                                break;
                            }else {
                                String[] odgovorServera =odgovor(nizKlienta,niz);
                                out.println("Niste pogodili: " + odgovorServera[0] + odgovorServera[1] + odgovorServera[2] + odgovorServera[3] + " Broj pokusala: " + brojPokusaja);
                            }
                        }
                    }else{
                        out.println("Nemate vise pokusaja. Tacan odgovro je: " + niz[0] + niz[1] + niz[2] + niz[3] + " Da li zelite novu igru?");
                        break;
                    }
                }
                while (true){
                    porukaKlijenta = in.readLine();
                    if(porukaKlijenta.equalsIgnoreCase("da")){
                        System.out.println("Pocinje nova igra...");
                        brojPokusaja = 6;
                        break;
                    }else{
                        System.out.println("Kraj igre.");
                        out.println("Hvala na ucescu.");
                        start=false;
                        break;
                    }
                }
            }






            server.disconnectClient();
            socket.close();
        }catch (Exception e){
            server.disconnectClient();
        }
    }
    private String[] odgovor(String[] niz, String[] odabraniBrojevi){
        String[] noviNiz = new String[niz.length];
        boolean pogodak =false;
        int mesto=0;
        for(int i = 0; i<niz.length;i++){
            if(niz[i].equals(odabraniBrojevi[i])){
                noviNiz[mesto] = "X";
                mesto++;
                pogodak=true;
            }
        }

        if(pogodak){
            for(int i=mesto;i<niz.length;i++){
                if(!niz[i].equals("X")){
                    noviNiz[i] = "O";
                }
            }
        }else{
            for(int i=mesto;i<niz.length;i++){
                noviNiz[i] = "O";
            }
        }
        return noviNiz;


    }
    private boolean imaParne(String[] niz){
        for(int i=0;i<niz.length;i++){
            int br = Integer.parseInt(niz[i]);
            if(br%2==0 || br> 9){
                return true;
            }
        }
        return  false;
    }
    private String[] randomNiz(String[] niz){
        Random random = new Random();
        String[] nizZaPogadjanje = new String[4];
        for(int i=0;i<4;i++){
            String  broj = String.valueOf(random.nextInt(niz.length));
            int br = Integer.parseInt(broj);
            nizZaPogadjanje[i] = niz[br];
        }
        return nizZaPogadjanje;
    }
    private  void ispisNiza(String[] niz){
        for(int i=0;i<4;i++){
            System.out.println(niz[i]);
        }
    }
}
