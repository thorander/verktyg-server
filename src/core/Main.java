package core;

import core.Server;
import entity.Answer;
import entity.Question;
import entity.Test;
import entity.User;
import entity.useranswers.UTest;
import javafx.application.Application;
import javafx.stage.Stage;
import service.Mail;

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
        Mail.setup();

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

        //Add to the user which tests are available for them

        //Add everything to be persisted.
        //Only users and tests, questions and answers persist by cascade.
        em.getTransaction().begin();
        em.persist(markus);
        em.persist(teacher);
        em.persist(student);
        em.persist(philip);
        em.getTransaction().commit();

        System.out.println("Persistence committed. Ready to use.");
    }
}
