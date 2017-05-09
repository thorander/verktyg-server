package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Test {

    @OneToMany(targetEntity = Question.class, cascade = CascadeType.PERSIST)
    private List questions;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int testId;

    String title;

    public Test(String title){
        this();
        this.title = title;
    }

    public Test(){
        title = "";
        questions = new ArrayList<Question>();
    }

    public void addQuestion(Question q){
        questions.add(q);
    }

}
