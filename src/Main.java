import core.Server;
import entity.Answer;
import entity.Question;
import entity.Test;
import entity.User;
import entity.useranswers.UTest;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.persistence.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{



        Server server = new Server(4436);
        server.start();

    }

    public static void main(String[] args) {
/*        launch(args);*/

        Server server = new Server(4436);
        server.start();

        System.out.println("Kom vi hit?");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAVerktyg");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Test t = new Test("ESC2017");

        Question q1 = new Question("Var hålls Eurovision Song Contest i år?", t);
        Answer a1q1 = new Answer("Kiev", true, q1);
        Answer a2q1 = new Answer("Stockholm", false, q1);
        q1.addAnswer(a1q1);
        q1.addAnswer(a2q1);

        User markus = new User("Markus", "Gustafsson", "admin");

        t.addQuestion(q1);
        t.addTestTaker(markus);

        UTest utest = new UTest();

        em.persist(t);
        em.persist(utest);
        em.getTransaction().commit();
        System.out.println("Persistence committed");
    }
}
