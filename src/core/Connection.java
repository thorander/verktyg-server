package core;

import entity.User;
import service.UserService;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection extends Thread{

    private User user;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String input;

    private UserService us;

    public Connection(Socket socket, User user){
        this.socket = socket;
        this.user = user;
        us = new UserService();
    }

    public void run(){
        try {
            out = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), "UTF8"));
            while(true){
                input = in.readLine();
                if(input.equalsIgnoreCase("end"))
                    break;
                handleInput(input);
                out.println("Echo: " + input);
            }
        } catch (IOException e) {
            System.out.println(socket.getInetAddress() + " disconnected.");
        } finally {
            try {
                System.out.println(socket.getInetAddress() + " disconnected");
                socket.close();
                Server.removeConnection(this);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private void handleInput(String input){
        String[] split = input.split("#");
        switch(split[0]){
            case "REGISTER":
                User u = new User(split[1], split[2], split[3], split[4]);
                us.createUser(u);
                break;
        }
    }

}
