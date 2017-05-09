package core;

import entity.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

    private static ArrayList<Connection> connections = new ArrayList<>();
    private ServerSocket server;

    private int port;

    public Server(int port){
        this.port = port;
    }

    public void run(){
        try {
            server = new ServerSocket(port);
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Starting server...");
        while(true){
            try {
                Socket socket = server.accept();
                Connection c = new Connection(socket, new User("Markus", "Gustafsson", "student"));
                c.start();
                connections.add(c);
            } catch (IOException e){
                System.out.println(e);
            }
        }
    }

    static void removeConnection(Connection c){
        connections.remove(c);
    }



}
