package org.example;

import java.io.*;
import java.net.Socket;

public class Client {
    public Client()throws IOException {
        Socket socket=new Socket("localhost",2026);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));

        String message;

        message=in.readLine();
        System.out.println(message);

        message = kin.readLine();
        out.println("[Client]: "+message);


        while (true){
            message=in.readLine();
            System.out.println("[ServerThread]: "+message);

            message = kin.readLine();
            out.println(message);

            message = in.readLine();
            System.out.println("[ServerThread]: "+message);

            if(message.contains("Rezultat"))break;
        }
        socket.close();
    }

    static void main(String[] args)throws IOException {
        new Client();
    }
}
