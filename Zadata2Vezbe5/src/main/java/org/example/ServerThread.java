package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable{
    Server server;
    Socket socket;
    int brojKlikenta,brPokusaj=0;
    String sifra;

    public ServerThread(Server server, Socket socket, int brojKlikenta)throws Exception{
        this.server=server;
        this.socket=socket;
        this.brojKlikenta=brojKlikenta;
        this.sifra="raf";
    }
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
    private static boolean isPalindrome(String message){
        String palindrome = new StringBuilder(message).reverse().toString();
        if(palindrome.equals(message)) return true;
        return false;
    }
    private static String getOddCharacters(String message){
        String oddCharacters = "";
        for(int i = 0 ; i<message.length() ; i++){
            if(i%2!=0){
                oddCharacters += message.charAt(i);
            }
        }
        return oddCharacters;
    }
    private static String getEvenCharacters(String message){
        String evenCharacters = "";
        for(int i = 0 ; i<message.length() ; i++){
            if(i%2==0){
                evenCharacters += message.charAt(i);
            }
        }
        return evenCharacters;
    }

    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            boolean lozinka = false;

            while (!lozinka){
                out.println("Unesite lozinku:");
                String line = in.readLine();
                brPokusaj++;
                if(line.equals(sifra)){
                    out.println("Lozinka je ispravna. Unesite poruku za server!");
                    lozinka=true;
                }else{
                    if(brPokusaj==3){
                        out.println("Žao mi je, više sreće sledeći put.");
                        break;
                    }else{
                        out.println("Neispravna lozinka. Pokušajte ponovo!");
                    }
                }
            }

            while (lozinka){
                out.println("Unesite rec:");

                String line = in.readLine();
                int duzinaReci = line.length();
                String decodedMessage=cesarDecode(line,duzinaReci);
                if(isPalindrome(decodedMessage)){
                    String odd_chars = getOddCharacters(decodedMessage);
                    String out_message = "Uneta poruka je palindrom. Poruka sa izbačenim parnim pozicijama je:".concat(odd_chars);
                    String encoded_Message = cesarEncode(out_message, out_message.length());
                    out.println(encoded_Message);
                }else{
                    String even_chars = getEvenCharacters(decodedMessage);
                    String out_message = "Uneta poruka nije palindrom. Poruka sa izbačenim parnim pozicijama je:".concat(even_chars);
                    System.out.println(out_message.length());
                    String encoded_Message = cesarEncode(out_message, out_message.length());
                    out.println(encoded_Message);
                }
            }



            server.disconnect();
            socket.close();
        }catch(Exception e){
            server.disconnect();
            e.printStackTrace();
        }
    }
}
