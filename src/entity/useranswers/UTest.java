package entity.useranswers;

import entity.Question;
import entity.Test;
import org.eclipse.persistence.indirection.IndirectList;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
public class UTest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int UTestId;

    @OneToOne
    private Test testAnswered;

    private int timeSpent;
    private int score;
    private String grade;
    private boolean corrected;
    private boolean showResults;

    @Temporal(TemporalType.DATE)
    private Date turnedIn;

    @OneToMany(targetEntity = UQuestion.class, cascade = CascadeType.PERSIST)
    private List questions;

    public UTest(Test testAnswered){
        this.testAnswered = testAnswered;
        questions = new IndirectList();
        turnedIn = new Date();
    }

    public UTest(){}

    public void addQuestion(UQuestion u){
        questions.add(u);
    }


    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public boolean isCorrected() {
        return corrected;
    }

    public void setCorrected(boolean corrected) {
        this.corrected = corrected;
    }

    public boolean isShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }
}
