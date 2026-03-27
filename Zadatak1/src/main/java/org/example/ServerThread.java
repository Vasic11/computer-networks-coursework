package org.example;

import java.awt.desktop.OpenURIEvent;
import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable{
    Socket socket;
    String name;
    String komadna;
    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
    }
    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));
            String message;

            out.println("Unesite vase ime:");
            name = in.readLine();


            while (true){
                out.println("Postovani: " + name + " unesite poruku:");
                message = in.readLine();
                if(message.trim().isEmpty()){
                    out.println("Postovani: " + name + " uneli ste praznu poruku.");
                    continue;
                }
                out.println("MENI -> Velika slova: V | Mala slova: m | Obrni rec: ob | Izlaz: 9");
                komadna = in.readLine();
                if(komadna.trim().equals("V")){
                    out.println("Povecali ste sva slova: "+message.toUpperCase());
                    continue;
                }else if(komadna.trim().equals("m")){
                    out.println("Smanjili ste sva slova: "+message.toLowerCase());
                }else if(komadna.trim().equals("ob")){
                    String obrnutaPorika = new StringBuilder(message).reverse().toString();
                    out.println("Obrnuli ste celu reci: "+obrnutaPorika);
                } else if (komadna.trim().equals("9")) {
                    out.println("Uspesno ste se diskonektovali.");
                    break;
                }else{
                    out.println("Niste uneli nista od navedenog.");
                }

            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
