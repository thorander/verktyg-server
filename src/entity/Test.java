package entity;

import org.eclipse.persistence.indirection.IndirectList;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@NamedQueries({ @NamedQuery (name="Test.selectAll",
query="SELECT t FROM Test t"),
@NamedQuery(name="Test.findById",
        query="SELECT c FROM Test c WHERE c.testId = :testId")})
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
    private Integer time, maxPoints;

    private boolean selfcorrecting, showResult;


    public Test(String title){
        this();
        this.title = title;
        this.description = "";
        this.selfcorrecting = true;
        this.showResult = true;
    }

    public Test(String title, String description){
        this(title);
        this.description = description;
    }


    public Test(String qTitle, String qDescription, String openDate, String closeDate, String qTime, String selfCorrecting, String showResults, String qPoints) {
        this(qTitle, qDescription);
        DateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            title = qTitle;
            description = qDescription;
            open = formatter.parse(openDate);
            close = formatter.parse(closeDate);
            time = Integer.parseInt(qTime);
            this.selfcorrecting = selfCorrecting.equals("true") ? true : false;
            this.showResult = showResults.equals("true") ? true : false;
            maxPoints = Integer.parseInt(qPoints);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public Test(){
        title = "";
        description = "";
        time = 0;
        questions = new IndirectList();
    }


    public void addQuestion(Question q){
        questions.add(q);
    }


    public Date getOpen() {
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
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String toString(){
        return title + " ; " + questions.size();
    }

    public String getTitle(){
        return title;
    }

    public int getTestId(){
        return testId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTime(){
        return time;
    }

    public String getDescription(){
        return description;
    }

    public boolean isSelfcorrecting(){
        return selfcorrecting;
    }

    public boolean isShowResult(){
        return showResult;
    }

    public IndirectList getQuestions(){
        return (IndirectList)questions;
    }

    public int getMaxPoints(){
        return maxPoints;
    }
}
