package core;

import core.Server;
import entity.Answer;
import entity.Question;
import entity.Test;
import entity.User;
import entity.useranswers.UTest;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.persistence.*;

public class Main extends Application {

    public static boolean DEBUG = true;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Server server = new Server(4436);
        server.start();

    }

    public static void main(String[] args) {
/*        launch(args);*/

        Server server = new Server(4436);
        server.start();

        //persistTestData();
    }

    private static void persistTestData(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAVerktyg");
        EntityManager em = emf.createEntityManager();

        //Create users here
        User markus = new User("Markus", "Gustafsson", "mackan@bbb.se", "1234", "admin");
        User teacher = new User("Teacher", "Teacherson", "teacher", "1234", "teacher");
        User student = new User("Student", "Studentson", "student", "1234", "student");
        User philip = new User("Philip", "Persson", "chulle","123","admin");

        //We create a test
        Test t = new Test("ESC2017");
        t.setCreator(markus);

        Question q1 = new Question("Var hålls Eurovision Song Contest i år?", "One choice", t, 1);
        Answer a1q1 = new Answer("Kiev", true, q1, 1);
        Answer a2q1 = new Answer("Stockholm", false, q1, 2);
        q1.addAnswer(a1q1);
        q1.addAnswer(a2q1);

        Question q2 = new Question("Rangordna dessa", "Order", t, 2);
        Answer a1q2 = new Answer("Tre", false, q2, 3);
        Answer a2q2 = new Answer("Ett", false, q2, 1);
        Answer a3q2 = new Answer("Två", false, q2, 2);
        q2.addAnswer(a1q2);
        q2.addAnswer(a2q2);
        q2.addAnswer(a3q2);

        t.addQuestion(q1);
        t.addQuestion(q2);

        t.setMaxPoints(3);

        t.setCreator(philip);

        //Add to the user which tests are available for them
        markus.addTestToTake(t);

        //Add everything to be persisted.
        //Only users and tests, questions and answers persist by cascade.
        em.getTransaction().begin();
        em.persist(markus);
        em.persist(teacher);
        em.persist(student);
        em.persist(philip);
        em.persist(t);
        em.getTransaction().commit();

        System.out.println("Persistence committed. Ready to use.");
    }
}
