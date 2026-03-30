package org.example;

import javax.swing.plaf.synth.SynthTreeUI;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private static String cesarDecode(String message, int n){
        StringBuilder decoded_Message = new StringBuilder();
        for(char ch : message.toCharArray()){
            decoded_Message.append((char) (ch - n));
        }
        return decoded_Message.toString();
    }
    private static String cesarEncode(String message, int n){
        StringBuilder encoded_Message = new StringBuilder();
        for(char ch : message.toCharArray()){
            encoded_Message.append((char) (ch + n));
        }
        return encoded_Message.toString();
    }
    public Client()throws Exception{
        Socket socket = new Socket("localhost",75%24+2000);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));

        boolean pogodak=false;
        while(!pogodak){
            String message=in.readLine();
            System.out.println(message);

            String lozika = kin.readLine();
            out.println(lozika);

            String odgovor = in.readLine();
            System.out.println(odgovor);
            if(odgovor.equals("Lozinka je ispravna. Unesite poruku za server!")){
                pogodak=true;
            }else{
                if(odgovor.equals("Žao mi je, više sreće sledeći put."))break;
            }
        }

        while (pogodak){
            String message=in.readLine();
            System.out.println(message);

            String poruka = kin.readLine();
            String encodedMessage = cesarEncode(poruka,poruka.length());
            out.println(encodedMessage);

            if(poruka.equals("exit")){break;}

            message=in.readLine();
            String decodedMessage = cesarDecode(message,poruka.length());
            System.out.println(decodedMessage);
        }
    }


    static void main(String[] args)throws Exception {
        new Client();
    }
}
