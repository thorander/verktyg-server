package core;

import com.sun.xml.internal.stream.Entity;
import entity.Answer;
import entity.Question;
import entity.Test;
import entity.User;
import entity.useranswers.UAnswer;
import entity.useranswers.UQuestion;
import entity.useranswers.UTest;
import entity.useranswers.UserGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import service.GroupService;
import service.TestService;
import service.UTestService;
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
    private String users;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String input;

    private UserService us;
    private TestService ts;
    private UTestService uts;

    private GroupService gs;
    private UserGroup usergroup;

    private Question question;


    public Connection(Socket socket, User user){
        this.socket = socket;
        this.user = user;
        us = new UserService();
        ts = new TestService();
        gs = new GroupService();
        uts = new UTestService();
    }

    public Connection() {

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
        UserGroup ug;
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
                Test test = new Test(split[1], split[2], split[3], split[4], split[5], split[6], split[7], split[8]);
                test.setCreator(user);
                ts.setTest(test);
                break;
            case "GETUTEST":
                TypedQuery<UTest> getAllUTest = ts.getEm().createNamedQuery("UTest.selectAll", UTest.class);
                try {
                    ObservableList<UTest> allUTest = FXCollections.observableArrayList(getAllUTest.getResultList());//Sparar data i arraylist
                    String tests = "GETUTEST#";
                    for (int i = 0; i <allUTest.size(); i++) {
                        tests += allUTest.get(i).getTestAnswered().getTitle() + "@" + allUTest.get(i).getTestAnswered().getTestId()
                                + "@";
                    }
                    System.out.println(tests);
                    out.println(tests);//Skickar Sträng med data
                }
                catch(NoResultException e){}
                break;

            case "GETSTUDENTS":
                TypedQuery<User> getAllStudents = us.getEm().createNamedQuery("User.getStudents", User.class);
                try {
                    ObservableList<User> allStudents = FXCollections.observableArrayList(getAllStudents.getResultList());//Sparar data i arraylist
                    String tests = "GETSTUDENTS#";
                    for (int i = 0; i < allStudents.size(); i++) {
                        tests += allStudents.get(i).getFirstName() + " " + allStudents.get(i).getLastName()
                                + "@" + allStudents.get(i).getUid() + "@";
                    }
                    System.out.println(tests);
                    out.println(tests);//Skickar Sträng med data
                }
                catch(NoResultException e){}
                break;
            case "ALLTESTS":
                TypedQuery<Test> getAllTests = ts.getEm().createNamedQuery("Test.selectAll", Test.class);//Hämtar data från databasen
            try {
                ObservableList<Test> allTests = FXCollections.observableArrayList(getAllTests.getResultList());//Sparar data i arraylist
                String tests = "ALLTESTS# ";
                for (int i = 0; i < allTests.size(); i++) {
                    tests += allTests.get(i).getTitle() + "@" + allTests.get(i).getTestId() + "@";
                }
                out.println(tests);//Skickar Sträng med data
            }
                catch(NoResultException nre){
                System.out.println("Oj då");
                }
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
                        Question q = new Question(split[++i], split[++i], ts.getTest(), Integer.parseInt(split[++i]));
                        while(i + 1 < split.length){
                            if(split[++i].equals("ANSWER")){
                                Answer a = new Answer(split[++i], split[++i].equals("true") ? true : false, q, Integer.parseInt(split[++i]));
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
                ug = new UserGroup(split[1], split[2]);
                gs.createGroup(ug);
                break;
            case "GETUSERSFORGROUP":
                out.println(gs.getUsers());
                break;
            case "GETGROUPS":
                out.println(gs.getGroups());
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
                try{
                    Test testFromId = ts.getTestFromId(Integer.parseInt(split[1]));
                    System.out.println(testFromId.getTitle());
                    out.println("TAKETEST#"
                                + testFromId.getTitle() + "#"
                                + testFromId.getDescription() + "#"
                                + testFromId.getTime() + "#"
                                + testFromId.getTestId());
                    for(Object o : testFromId.getQuestions()){
                        out.println(((Question)o).getSendData());
                    }
                    out.println("SHOWTEST#");
                } catch (NoResultException e){
                    out.println("ERROR#No such test. This shouldn't happen.");
                }
                break;
            case "ADDTAKENTEST":
                Test originalTest = ts.getTestFromId(Integer.parseInt(split[1]));
                UTest utest = new UTest(originalTest, Integer.parseInt(split[2]));
                uts.setTest(utest);
                break;
            case "ADDUSERQUESTION":
                TypedQuery<Question> query = uts.getEm().createNamedQuery("Question.findById", Question.class);
                Question resultQuestion = query.setParameter("id", Integer.parseInt(split[1])).getSingleResult();
                UQuestion uQuestion = new UQuestion(resultQuestion);
                for(int j = 2; j < split.length; j++){
                    TypedQuery<Answer> answerQuery = uts.getEm().createNamedQuery("Answer.findById", Answer.class);
                    Answer resultAnswer = answerQuery.setParameter("id", Integer.parseInt(split[j++])).getSingleResult();
                    UAnswer uAnswer = new UAnswer(resultAnswer, Integer.parseInt(split[j++]), split[j++], split[j].equals("true") ? true : false);
                    uQuestion.getUserAnswers().add(uAnswer);
                }
                uts.addQuestion(uQuestion);
                break;
            case "PERSISTTAKENTEST":
                uts.persistTest();
                user.addTakenTest(uts.getTest());
                user.removeTestToTake(uts.getTest().getTestAnswered());
                us.createUser(user);
                break;
            case "GETTEST":
                out.println(gs.getGroups());
        }
    }

    /*public void setUser(String u) {
        this.users = u;

        if(!users.equals("")){
            out.println(users);
        }
        //System.out.println(users);
    }*/

}
