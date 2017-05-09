package core;

import entity.User;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection extends Thread{

    private User user;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String input;

    public Connection(Socket socket, User user){
        this.socket = socket;
        this.user = user;
    }

    public void run(){
        try {
            out = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), "UTF8"));
            while(true){
                input = in.readLine();
                System.out.println(input);
                if(input.equalsIgnoreCase("end"))
                    break;
                out.println("Echo: " + input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                Server.removeConnection(this);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

}
