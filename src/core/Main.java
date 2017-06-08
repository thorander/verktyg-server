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

/**
 * Main class - start here.
 */
public class Main {

    public static boolean DEBUG = true;

    public static void main(String[] args) {
        Mail.setup();

        //Create a server which listens for incoming connections and start it
        Server server = new Server(4436);
        server.start();


        //persistTestData();
    }

    /**
     * Populate the database with test data
     */
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
