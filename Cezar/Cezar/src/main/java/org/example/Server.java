package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server()throws IOException {
        ServerSocket serverSocket = new ServerSocket(2026);
        System.out.println("Server Started-port:"+serverSocket.getLocalPort());
        int clientNumber = 0;

        while (true) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket, ++clientNumber);
            Thread thread = new Thread(serverThread);
            thread.start();
        }
    }

    static void main(String[] args)throws IOException {
        new Server();
    }
}
