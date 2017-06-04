package entity.useranswers;

import entity.Question;
import entity.Test;
import org.eclipse.persistence.indirection.IndirectList;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * User Test
 * A user answered test. Keeps information such as if it has been graded, how many points it got
 * what grade it got, keeps a reference to the test it answers.
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "UTest.selectAll",
                query = "SELECT t FROM UTest t"),
        @NamedQuery(name = "UTest.findByGroupAndTest",
                query = "SELECT t FROM UTest t JOIN User u JOIN UserGroup ug WHERE t MEMBER OF u.takenTests AND u MEMBER OF ug.users" +
                        " AND t.testAnswered.testId = :testId AND ug.groupId = :groupId")
})
public class UTest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int UTestId;

    @OneToOne
    private Test testAnswered;

    private int timeSpent;
    private int score;
    private String grade, comment;
    private boolean corrected;
    private boolean showResults;

    @Temporal(TemporalType.DATE)
    private Date turnedIn;

    @OneToMany(targetEntity = UQuestion.class, cascade = CascadeType.PERSIST)
    private List questions;

    public UTest(Test testAnswered, int timeSpent){
        this.testAnswered = testAnswered;
        corrected = false;
        showResults = false;
        questions = new IndirectList();
        turnedIn = new Date();
        this.timeSpent = timeSpent;
        this.comment = "";
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

    public Test getTestAnswered(){
        return testAnswered;
    }

    public List getQuestions(){
        return questions;
    }

    public int getUTestId(){return UTestId;}

    public String getComment(){return comment;}

    public void setComment(String comment){ this.comment = comment;}
}
