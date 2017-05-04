package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String question;

    @OneToMany(targetEntity = Answer.class, cascade = CascadeType.PERSIST)
    private List answers;

    @OneToOne
    private Test test;

    public Question(String question, Test test) {
        super();
        this.question = question;
        this.test = test;
        answers = new ArrayList<Answer>();
    }

    public Question() {
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

    public void addAnswer(Answer a){
        answers.add(a);
    }

}
