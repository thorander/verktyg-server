package entity;

import javax.persistence.*;

/**
 * Created by Matilda on 2017-05-04.
 */

@Entity
@Table
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String question;

    public Question(int id, String question) {
        super();
        this.id = id;
        this.question = question;
    }

    public Question() {
        super();
        //question = "";
    }

    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
