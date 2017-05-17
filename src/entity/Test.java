package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
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

    @ManyToOne
    private User creator;

/*    @Temporal(TemporalType.DATE)
    private Date open;
    @Temporal(TemporalType.DATE)
    private Date close;*/

    private int timeLimit;

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
        timeLimit = 0;
        questions = new ArrayList<Question>();
        testTakers = new ArrayList<User>();
    }

    public void addQuestion(Question q){
        questions.add(q);
    }

    public void addTestTaker(User u){ testTakers.add(u);}
    public void removeTestTaker(User u){testTakers.remove(u);}

/*    public Date getOpen() {
        return open;
    }

    public Date getClose() {
        return close;
    }

    public void setClose(Date close) {
        this.close = close;
    }

    public void setOpen(Date open) {
        this.open = open;
    }*/

    public void setCreator(User creator) {
        this.creator = creator;
        System.out.println(creator);
    }

    public String toString(){
        return title + " ; " + questions.size();
    }
}
