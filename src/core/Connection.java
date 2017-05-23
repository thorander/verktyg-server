package core;

import entity.Answer;
import entity.Question;
import entity.Test;
import entity.User;
import entity.useranswers.UserGroup;
import service.GroupService;
import service.TestService;
import service.UserService;

import javax.jws.soap.SOAPBinding;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Connection extends Thread{

    private User user;
    private List users;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String input;

    private UserService us;
    private TestService ts;
    private UserGroup ug;

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
                out.println("REGSUCCESS#Mhmm");
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
                System.out.println("Array length: " + split.length);
                Test test = new Test(split[1], split[2], split[3], split[4], split[5]);
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
                        ts.addQuestion(q);
                    }
                    i++;
                }
                break;

            case "CREATEGROUP":
                ug = new UserGroup();
                users = new ArrayList<User>();
                ug.setGroupName(split[1]);
                ug.setUsers(users);
                GroupService gs = new GroupService();
                gs.setUg(ug);
                gs.persistGroup();

                break;
            case "ADDUSER":
                for (int x = 0; x < split.length; x++){
                    if (split[0].equals("ADDUSER")){
                        ug = new UserGroup();
                        ug.setUsers(users);
                }}
                break;
            case "PERSISTTEST":
                ts.persistTest();
                us.getEm().getTransaction().begin();
                user.addTestToTake(ts.getTest());
                us.getEm().getTransaction().commit();
                break;
            case "GETAVAILABLETESTS":
                String s = user.getAvailableTests();
                if(!s.equals("")){
                    out.println(s);
                }
                break;
            case "FETCHTESTBYID":
                TypedQuery<Test> testById = ts.getEm().createNamedQuery("Test.findById", Test.class);
                try{
                    Test testFromId = testById.setParameter("testId", Integer.parseInt(split[1])).getSingleResult();
                    System.out.println(testFromId.getTitle());
                    out.println("TAKETEST#"
                                + testFromId.getTitle() + "#"
                                + testFromId.getDescription() + "#"
                                + testFromId.getTime() + "#"
                                + testFromId.getTestId());
                } catch (NoResultException e){
                    out.println("ERROR#No such test. This shouldn't happen.");
                }
        }
    }

}
