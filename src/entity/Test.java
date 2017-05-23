package entity;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
public class Test {

    @OneToMany(targetEntity = Question.class, cascade = CascadeType.PERSIST)
    private List questions;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int testId;

    @Column
    private String title;
    @Column
    private String description;

    @ManyToOne
    private User creator;

    @Temporal(TemporalType.DATE)
    private Date open;
    @Temporal(TemporalType.DATE)
    private Date close;
    @Column
    private Integer time;


    public Test(String title){
        this();
        this.title = title;
        this.description = "";
    }

    public Test(String title, String description){
        this(title);
        this.description = description;
    }

    public Test(String qTitle, String qDescription, String openDate, String closeDate, String qTime) {
        DateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            title = qTitle;
            description = qDescription;
            open = formatter.parse(openDate);
            close = formatter.parse(closeDate);
            time = Integer.parseInt(qTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public Test(){
        title = "";
        description = "";
        time = 0;
        questions = new ArrayList<Question>();
    }

    public void addQuestion(Question q){
        questions.add(q);
    }


   /* public Date getOpen() {
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

    public String getTitle(){
        return title;
    }
}
