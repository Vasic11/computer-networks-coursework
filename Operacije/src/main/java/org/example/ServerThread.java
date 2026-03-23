package org.example;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable{
    Socket socket;
    char ch;
    int res=0;
    String currNumber="";
    char operator = '+';
    public ServerThread(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            out.println("[ServerThread] Dobrodosao na server. Unesite vase ime: ");
            String name = in.readLine();

            while (true){
                out.println(name + ", unesite izraz za izracunavanje: ");
                String line = in.readLine();

                if(line.matches("[0-9+-]+") && !line.contains("++") && !line.contains("+-") && !line.contains("--") && !line.contains("-+") ){
                    for(int i=0; i<line.length();i++){
                        ch =  line.charAt(i);
                        if(ch=='+' || ch=='-'){
                            if(operator=='+'){
                                res+= Integer.parseInt(currNumber);
                            }else{
                                res-=Integer.parseInt(currNumber);
                            }
                            operator = ch;
                            currNumber="";
                        }else{
                            currNumber+=ch;
                        }
                    }
                    if(operator=='+'){
                        res+= Integer.parseInt(currNumber);
                    }else{
                        res-=Integer.parseInt(currNumber);
                    }
                    out.println("[ServerThread] Rezultat je: " + res);
                    break;
                }else{
                    out.println("[ServerThread] Niste dobro uneli.");
                }

            }
            socket.close();
            System.out.println("[ServerThread] Test = "+res);





        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
