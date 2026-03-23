package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {
    Socket socket;
    String message;
    int clientNumber;

    public ServerThread(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
    }
    private String decode(String message, int n) {
        StringBuilder decodedMessage = new StringBuilder();
        for (char ch : message.toCharArray()) {
            decodedMessage.append((char) (ch - n));
        }
        return decodedMessage.toString();
    }

    private String encode(String message, int n) {
        StringBuilder encodedMessage = new StringBuilder();
        for (char ch : message.toCharArray()) {
            encodedMessage.append((char) (ch + n));
        }
        return encodedMessage.toString();
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);

            out.println("Dobrodosao na server, tvoj redni broj je: " + clientNumber);

            String  message = in.readLine();
            int shiftValue = Integer.parseInt(message);
            out.println("Uspesno izabran broj n: " + shiftValue);
            System.out.println("Klijent je izabrao broj n: " + shiftValue);

            while (true) {
                message = in.readLine();
                if(message==null){
                    break;
                }

                System.out.println("Primljena poruka od klijenta: " + message);
                String decodedMessage = decode(message, shiftValue);
                System.out.println("Desifrovana poruka je: " + decodedMessage);

                if("exit".equals(decodedMessage)){
                    break;
                }

                String reversedMessage = new StringBuilder(decodedMessage).reverse().toString();
                String codedMessage = encode(reversedMessage, shiftValue);
                out.println(codedMessage);

            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
