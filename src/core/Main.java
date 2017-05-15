package core;

import core.Server;
import entity.Answer;
import entity.Question;
import entity.Test;
import entity.User;
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

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAVerktyg");
        EntityManager em = emf.createEntityManager();

        Test t = new Test("ESC2017");

        Question q1 = new Question("Var hålls Eurovision Song Contest i år?", t);
        Answer a1q1 = new Answer("Kiev", true, q1);
        Answer a2q1 = new Answer("Stockholm", false, q1);
        q1.addAnswer(a1q1);
        q1.addAnswer(a2q1);

        User markus = new User("Markus", "Gustafsson", "mackan", "1234", "admin");

        t.addQuestion(q1);
        t.addTestTaker(markus);
        t.setCreator(markus);

        System.out.println("Everything created up to here.");

        em.getTransaction().begin();
        System.out.println("Begun transaction");
        em.persist(t);
        System.out.println("Finished persisting");
        em.getTransaction().commit();
        System.out.println("Persistence committed. Ready to use.");
    }
}
