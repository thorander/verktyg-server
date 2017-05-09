package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Test {

    @OneToMany(targetEntity = Question.class, cascade = CascadeType.PERSIST)
    private List questions;
    @OneToMany(targetEntity = User.class, cascade = CascadeType.PERSIST)
    private List testTakers;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int testId;

    private String title;
    private String description;

    public Test(String title){
        this();
        this.title = title;
        this.description = "";
    }

    public Test(String title, String description){
        this(title);
        this.description = description;
    }

    public Test(){
        title = "";
        questions = new ArrayList<Question>();
        testTakers = new ArrayList<User>();
    }

    public void addQuestion(Question q){
        questions.add(q);
    }

    public void addTestTaker(User u){ testTakers.add(u);}
    public void removeTestTaker(User u){testTakers.remove(u);}

}
