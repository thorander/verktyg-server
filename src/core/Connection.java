package core;

import entity.Answer;
import entity.Question;
import entity.User;
import service.UserService;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
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

    private Question question;

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
        User u;
        Question q = new Question();
        switch(split[0]){
            case "REGISTER":
                u = new User(split[1], split[2], split[3], split[4], split[5]);//hej
                us.createUser(u);
                break;
            case "LOGIN":
                TypedQuery<User> userByUsername = us.getEm().createNamedQuery("User.findByName", User.class);
                try{
                    u = userByUsername.setParameter("username", split[1]).getSingleResult();
                    out.println("LOGIN#" + u.getFirstName() + "#" + u.getRole() + "#" + u.getUid());
                } catch (NoResultException e){
                    out.println("ERROR#No such user registered. Check your username.");
                }
                break;
            case "CREATEQUIZ":
                try {
                   //Do something
                } catch(NoResultException e) {
                    out.println("ERROR# Couldn't save the quiz.");
                }
        }
    }

}
