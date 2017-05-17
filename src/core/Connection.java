package core;

import entity.Answer;
import entity.Question;
import entity.Test;
import entity.User;
import service.TestService;
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
    private TestService ts;

    private Question question;

    public Connection(Socket socket, User user){
        this.socket = socket;
        this.user = user;
        us = new UserService();
        ts = new TestService();
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
        Test t;
        switch(split[0]){
            case "REGISTER":
                u = new User(split[1], split[2], split[3], split[4], split[5]);
                us.createUser(u);
                break;
            case "LOGIN":
                TypedQuery<User> userByUsername = us.getEm().createNamedQuery("User.findByName", User.class);
                try{
                    u = userByUsername.setParameter("username", split[1]).getSingleResult();
                    if(!u.getPassword().equals(split[2])){
                        out.println("ERROR#Wrong password");
                        return;
                    }
                    out.println("LOGIN#" + u.getFirstName() + "#" + u.getRole() + "#" + u.getUid());
                    user = u;
                } catch (NoResultException e){
                    out.println("ERROR#No such user registered. Check your username.");
                }
                break;
            case "CREATEQUIZ":
                t = new Test(split[1], split[2]);
                ts.createTest(t);
                break;
            case "CREATETEST":
                Test test = new Test(split[1]);
                test.setCreator(user);
                ts.setTest(test);
                break;
            case "QUESTION":
                if(Main.DEBUG){
                    for(int i = 0; i < split.length; i++){
                        System.out.println(i + " " + split[i]);
                    }
                }
                int i = 0;
                while(i < split.length){
                    if(split[i].equals("QUESTION")){
                        Question q = new Question(split[++i], split[++i], ts.getTest());
                        while(i + 1 < split.length){
                            if(split[++i].equals("ANSWER")){
                                Answer a = new Answer(split[++i], split[++i].equals("true") ? true : false, q);
                                q.addAnswer(a);
                            } else {
                                break;
                            }
                        }
                        System.out.println("Adding the question");
                        ts.addQuestion(q);
                    }
                    i++;
                }
                break;
            case "PERSISTTEST":
                ts.persistTest();
                break;
        }
    }

}
