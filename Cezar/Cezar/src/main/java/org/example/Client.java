package org.example;

import java.io.*;
import java.net.Socket;

public class Client {
    private static String encode(String message, int n){
        StringBuilder codeMessage = new StringBuilder();
        for(char ch : message.toCharArray()){
            codeMessage.append((char) (ch+n));
        }
        return codeMessage.toString();
    }
    private static String decode(String message, int n){
        StringBuilder decodeMessage = new StringBuilder();
        for(char ch : message.toCharArray()){
            decodeMessage.append((char)(ch-n));
        }
        return decodeMessage.toString();
    }

    public Client()throws IOException {
        Socket socket = new Socket("localhost", 2026);
        String message;

        BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out  = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));

        message = in.readLine();
        System.out.println(message);

        System.out.println("Unesite broj n: ");
        message = kin.readLine();
        int shiftValue = Integer.parseInt(message);
        out.println(message);

        message = in.readLine();
        System.out.println(message);

        while (true){
            System.out.println("Unesite poruku za server: ");
            message = kin.readLine();
            String codedMessage = encode(message,shiftValue);
            System.out.println("Poslata poruka serveru: " + codedMessage);

            out.println(codedMessage);

            if("exit".equals(message)){
                break;
            }

            String recivedMessage = in.readLine();
            System.out.println("Sifovana poruka je: " + recivedMessage);
            System.out.println("Desifrovana primljena poruka je: " + decode(recivedMessage,shiftValue));
        }
        socket.close();

    }

    static void main(String[] args)throws IOException {
        new Client();
    }
}
