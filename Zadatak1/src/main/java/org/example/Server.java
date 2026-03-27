package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server()throws IOException {
        ServerSocket serverSocket = new ServerSocket(2026);
        System.out.println("Server started-port: " + serverSocket.getLocalSocketAddress());

        while (true) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket);
            Thread thread = new Thread(serverThread);
            thread.start();
        }
    }

    public static void main(String[] args)throws IOException {
        new Server();
    }
}
