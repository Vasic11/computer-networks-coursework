package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ServerThread implements Runnable{
    Socket socket;
    Server server;
    int brojKlijenta;
    String lozinka;
    int brojPokusaja,n,m;
    String odgovor;


    public ServerThread(Server server, Socket socket, int brojKlijenta){
        this.server = server;
        this.socket = socket;
        this.brojKlijenta = brojKlijenta;
        lozinka = "raf";
    }

    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            brojPokusaja = 0;
            boolean sifra = false;
            String line;
            while (!sifra){
                out.println("Unesite lozinku: ");
                String pokusaj = in.readLine();
                brojPokusaja++;
                if(pokusaj.equals(lozinka)){
                    sifra = true;
                    out.println("Lozinka je ispravna. Unesite poruku za server!");
                }else{
                    if(brojPokusaja==2){
                        out.println("Zao nam je, vise srece drugi put.");
                        break;
                    }else{
                        out.println("Pokusajte ponovo.");
                    }
                }
            }
            Random random = new Random();
            n = random.nextInt(101);
            m = random.nextInt(101);

            if (sifra) {
                if (n > m) {
                    int zbir = n + m;
                    if ((65 <= zbir && zbir <= 90) || (97 <= zbir && zbir <= 122)) {
                        char ch = (char) zbir;
                        out.println(ch);
                    } else {
                        out.println("[S]: Zbir brojeva je: " + zbir);
                    }
                } else if (n < m) {
                    int razlika = m - n;
                    if ((65 <= razlika && razlika <= 90) || (97 <= razlika && razlika <= 122)) {
                        char ch = (char) razlika;
                        out.println(ch);
                    } else {
                        out.println("[S]: Razlika brojeva je: " + razlika);
                    }
                } else {
                    out.println("Brojevi n i m su jednaki");
                    while (true) {
                        line = in.readLine();
                        if (line.contains("Prekini konekciju")) break;
                        if (brojKlijenta % 2 == 0) {
                            odgovor = line.toLowerCase();
                            out.println(odgovor);
                        } else {
                            odgovor = line.toUpperCase();
                            out.println(odgovor);
                        }
                    }
                }

            }

            server.disconnect();
            System.out.println(brojKlijenta);
            socket.close();
        }catch (IOException e){
            server.disconnect();
            e.printStackTrace();
        }
    }
}
