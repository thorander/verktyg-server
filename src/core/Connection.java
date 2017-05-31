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
import service.*;

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

    private TypedQuery<Test> testQuery;
    private TypedQuery<UTest> uTestQuery;
    private TypedQuery<User> userQuery;
    private TypedQuery<Question> questionQuery;
    private TypedQuery<Answer> answerQuery;
    private TypedQuery<UserGroup> userGroupQuery;



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
                Mail.sendWelcomeEmail(u.getEmail(), u.getFirstName());
                break;
            case "LOGIN":
                userQuery = us.getEm().createNamedQuery("User.findByMail", User.class);
                try{
                    u = userQuery.setParameter("email", split[1]).getSingleResult();
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
                uTestQuery = ts.getEm().createNamedQuery("UTest.selectAll", UTest.class);
                try {
                    ObservableList<UTest> allUTest = FXCollections.observableArrayList(uTestQuery.getResultList());//Sparar data i arraylist
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
                userQuery = us.getEm().createNamedQuery("User.getStudents", User.class);
                try {
                    ObservableList<User> allStudents = FXCollections.observableArrayList(userQuery.getResultList());//Sparar data i arraylist
                    String tests = "GETSTUDENTSTOSHARE#";
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
                testQuery = ts.getEm().createNamedQuery("Test.selectAll", Test.class);//Hämtar data från databasen
            try {
                ObservableList<Test> allTests = FXCollections.observableArrayList(testQuery.getResultList());//Sparar data i arraylist
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
                ug = new UserGroup(split[1]);
                for(int j = 2; j < split.length;){
                    userQuery = us.getEm().createNamedQuery("User.findById", User.class);
                    try{
                        ug.addUser(userQuery.setParameter("uid", Integer.parseInt(split[j++])).getSingleResult());
                    } catch (NoResultException e){
                        System.out.println("No user was found with that ID");
                    }
                }
                out.println("SUCCESS#Your group was created successfully");
                gs.createGroup(ug);
                break;
            case "GETUSERSFORGROUP":
                userQuery = us.getEm().createNamedQuery("User.getStudents", User.class);
                try {
                    ObservableList<User> allStudents = FXCollections.observableArrayList(userQuery.getResultList());//Sparar data i arraylist
                    String tests = "STUDENTSFORGROUP#";
                    for (i = 0; i < allStudents.size(); i++) {
                        tests += allStudents.get(i).getFirstName() + " " + allStudents.get(i).getLastName()
                                + "@" + allStudents.get(i).getUid() + "@";
                        System.out.println("Student: " + allStudents.get(i).getFirstName());
                    }
                    out.println(tests);//Skickar Sträng med data
                } catch (Exception e){
                    System.out.println("Couldn't get users for groups...");
                }
                break;
            case "GETGROUPS":
                out.println("GETGROUPS" + gs.getGroups());
                break;
            case "GETGROUPSFORSHARE":
                out.println("GETGROUPSTOSHARE" + gs.getGroups());
                break;
            case "PERSISTTEST":
                ts.persistTest();
                us.getEm().getTransaction().begin();
                user.addTestToTake(ts.getTest());
                us.getEm().getTransaction().commit();
                out.println("SUCCESS#Your test was created successfully");
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
                questionQuery = uts.getEm().createNamedQuery("Question.findById", Question.class);
                Question resultQuestion = questionQuery.setParameter("id", Integer.parseInt(split[1])).getSingleResult();
                UQuestion uQuestion = new UQuestion(resultQuestion);
                for(int j = 2; j < split.length; j++){
                    answerQuery = uts.getEm().createNamedQuery("Answer.findById", Answer.class);
                    Answer resultAnswer = answerQuery.setParameter("id", Integer.parseInt(split[j++])).getSingleResult();
                    UAnswer uAnswer = new UAnswer(resultAnswer, Integer.parseInt(split[j++]), split[j++], split[j].equals("true") ? true : false);
                    uQuestion.getUserAnswers().add(uAnswer);
                }
                uts.addQuestion(uQuestion);
                break;
            case "PERSISTTAKENTEST":
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                uts.persistTest();
                user.addTakenTest(uts.getTest());
                user.removeTestToTake(uts.getTest().getTestAnswered());
                us.createUser(user);
                out.println("SUCCESS#You turned in your test successfully");
                break;
            case "GETTEST":
                out.println(uts.getUTest());
                break;
            case "TESTLIST":
                out.println(uts.testList());
                break;
            case "GETUSERSFORPDF":
                userQuery = us.getEm().createNamedQuery("User.findUserByUTest", User.class);
                try{
                    ArrayList<User> users = new ArrayList<>(userQuery.setParameter("testId", Integer.parseInt(split[1])).getResultList());
                    String output = "GETUSERSFORPDF#";
                    for(User pdfUser : users){
                        output += pdfUser.getFirstName() + " " + pdfUser.getLastName() + "@" + pdfUser.getUid() + "@";
                    }
                    out.println(output);
                } catch (NoResultException e){
                    System.out.println(e);
                }
                break;

            case "GetInfoStatistic":
                uTestQuery = us.getEm().createNamedQuery("UTest.findByGroupAndTest", UTest.class);
                try{
                    uTestQuery.setParameter("groupId", split[2]);
                    ArrayList<UTest> users = new ArrayList<>(uTestQuery.setParameter("testId", Integer.parseInt(split[1])).getResultList());
                    String output = "GETUSERSFORPDF#";
                    for(UTest UTestS : users){
                        output += UTestS.getGrade() + "@" + UTestS.getScore() + "@";
                    }
                    out.println(output);
                } catch (NoResultException e){
                    System.out.println(e);
                }
                break;
            case "GETSTATGROUPSANDTESTS":
                testQuery = ts.getEm().createQuery("SELECT t FROM Test t", Test.class);
                userGroupQuery = us.getEm().createQuery("SELECT u FROM UserGroup u", UserGroup.class);
                try{
                    String tests = "ADDTESTS";
                    String groups = "ADDGROUPS";
                    for(Test te : testQuery.getResultList()){
                        tests += "#" + te.getTitle() + "#" + te.getTestId();
                    }
                    for(UserGroup ugr : userGroupQuery.getResultList()){
                        groups += "#" + ugr.getGroupName() + "#" + ugr.getGroupId();
                    }
                    out.println(tests);
                    out.println(groups);

                } catch(NoResultException e){
                    System.out.println("Found nada, for some reason.");
                }
                break;
            case "GETSTATISTICS":
                uTestQuery = us.getEm().createNamedQuery("UTest.findByGroupAndTest", UTest.class);
                try{
                    System.out.println(split[1] + " " + split[2]);
                    ArrayList<UTest> stattests = new ArrayList<>(uTestQuery.setParameter("testId", Integer.parseInt(split[1])).setParameter("groupId", Integer.parseInt(split[2])).getResultList());
                    System.out.println(stattests.size());
                    int g = 0, vg = 0, ig = 0, score = 0;
                    for(UTest temp : stattests){
                        score += temp.getScore();
                        if(temp.getGrade().equalsIgnoreCase("ig")){
                            ig++;
                        } else if (temp.getGrade().equalsIgnoreCase("g")){
                            g++;
                        } else if (temp.getGrade().equalsIgnoreCase("vg")){
                            vg++;
                        }
                    }
                    out.println("UPDATESTATS"
                                + "#" + stattests.size()
                                + "#" + (g+vg)
                                + "#" + ((float)score/(float)stattests.size())
                                + "#" + ig
                                + "#" + g
                                + "#" + vg);
                }catch (NoResultException e){
                    System.out.println(e);
                }
                break;
            case "SHARETOSTUDENT":
                testQuery = ts.getEm().createNamedQuery("Test.findById", Test.class);
                userQuery = us.getEm().createNamedQuery("User.findById", User.class);
                System.out.println(input);
                try{
                    Test testById = testQuery.setParameter("testId", Integer.parseInt(split[1])).getSingleResult();
                    User userById = userQuery.setParameter("uid", Integer.parseInt(split[2])).getSingleResult();

                    shareToUser(testById, userById);

                } catch (NoResultException e){
                    System.out.println(e);
                }
            case "SHARETOGROUP":
                testQuery = ts.getEm().createNamedQuery("Test.findById", Test.class);
                userGroupQuery = us.getEm().createNamedQuery("UserGroup.findById", UserGroup.class);
                break;
        }
    }

    private void shareToUser(Test t, User u){
        System.out.println("I was here");
        if(u.hasNotTaken(t)){
            us.getEm().getTransaction().begin();
            u.addTestToTake(t);
            us.getEm().getTransaction().commit();
            out.println("SUCCESS#Test was shared successfully");
            Mail.sendNewTestMail(u.getEmail(), u.getFirstName());
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
