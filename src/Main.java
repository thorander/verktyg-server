import core.Server;
import entity.Answer;
import entity.Question;
import entity.Test;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.persistence.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane root = new GridPane();



        Server server = new Server(4436);
        server.run();

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        //launch(args);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAVerktyg");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Test t = new Test("Hur man slår barn utan att det märks");

        Question q1 = new Question("Var ska man slå?", t);
        Answer a1q1 = new Answer("På armen", true, q1);
        Answer a2q1 = new Answer("I ansiktet", false, q1);
        q1.addAnswer(a1q1);
        q1.addAnswer(a2q1);

        t.addQuestion(q1);

        em.persist(t);
        em.getTransaction().commit();
        System.out.println("Persistence committed");
    }
}
