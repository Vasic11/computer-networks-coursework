package org.example;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    public Client()throws IOException {
        Socket socket = new Socket("localhost", 75%24+2000);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));

        boolean lozinka = false;
        while (!lozinka){
            String poruka = in.readLine();
            System.out.println(poruka);

            String sifra = kin.readLine();
            out.println(sifra);

            String odgovor = in.readLine();
            System.out.println(odgovor);

            if(odgovor.contains("ispravna")){
                lozinka = true;
            }else if(odgovor.contains("vise srece")){
                break;
            }
        }
        if (lozinka){
            String odgovor = in.readLine();
            System.out.println(odgovor);

            if(odgovor.contains("jednaki")){
                while (true){
                    String posaljiporuku = kin.readLine();
                    out.println(posaljiporuku);
                    if(posaljiporuku.contains("Prekini konekciju")){break;}
                    else{
                        String povratnaPoruka = in.readLine();
                        System.out.println(povratnaPoruka);
                    }
                }
            }

        }
        socket.close();
        kin.close();
    }

    static void main(String[] args)throws IOException {
            new Client();
    }

}
