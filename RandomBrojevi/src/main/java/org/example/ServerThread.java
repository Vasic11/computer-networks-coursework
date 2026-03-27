package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ServerThread implements Runnable {
    Socket socket;
    Server server;
    int pokusaj=1;
    int noviKlijent;
    private String sifra;
    int m,n;



    public ServerThread(Server server,Socket socket, int noviKlijent) {
        this.server=server;
        this.socket = socket;
        this.noviKlijent = noviKlijent;
        this.sifra = "raf";
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            boolean pogodak = false;
            while (pokusaj<3) {
                out.println("Redni broj: "+noviKlijent+ " unesite lozinku: ");
                String line = in.readLine();
                if (line.equals(sifra)) {
                    out.println("Uspesno ste ulogovali.");
                    pogodak = true;
                    break;
                } else {
                    out.println("Niste dobro uneli lozinku, pokusajte ponovo.");
                    pokusaj++;
                    if(pokusaj==3) {
                        out.println("Iskoristili ste sve pokusaje.");
                        break;
                    }
                }
            }
            if(pogodak) {
                Random random = new Random();
                m = random.nextInt(101);
                n = random.nextInt(101);
                System.out.println("Broj m: " + m + " Broj n: " + n);
                if (n > m) {
                    int zbir = n + m;
                    if ((65 <= zbir && zbir <= 90) || (97 <= zbir && zbir <= 122)) {
                        char ch = (char) zbir;
                        out.println("Vrednost iz zbira za ASCII: " + ch);
                    } else {
                        out.println("[S]:Zbir brojeva n+m je: " + zbir);
                    }
                } else if (n < m) {
                    int razlika = m - n;
                    if ((65 <= razlika && razlika <= 90) || (97 <= razlika && razlika <= 122)) {
                        char ch = (char) razlika;
                        out.println("Vrednost iz razlike za ASCII: " + ch);
                    } else {
                        out.println("[S]:Razlika brojeva m-n je: " + razlika);
                    }
                } else {
                    out.println("Brojvevi n i m su jednaki. Napisite poruku: ");
                    while (true) {
                        String line = in.readLine();
                        if (line.equals("ne zelim vise da igram")) {
                            break;
                        }
                        if (noviKlijent % 2 == 0) {
                            out.println(line.toLowerCase());
                        } else {
                            out.println(line.toUpperCase());
                        }
                    }
                }
            }
            server.diskonetovanKlijent();
            server.aktivni();
            socket.close();

        }catch (Exception e){
            e.printStackTrace();
            //diskonetovanKlijent();
        }
    }
}
