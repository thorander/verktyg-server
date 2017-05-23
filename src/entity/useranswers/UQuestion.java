package entity.useranswers;

import entity.Answer;
import entity.Question;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class UQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int UQuestionId;
    @OneToOne
    private Question question;
    @OneToMany(targetEntity = UAnswer.class, cascade = CascadeType.PERSIST)
    private List userAnswers;

    private int score;

    public UQuestion() {

    }

    public UQuestion(Question question, int UQuestionId) {
        this.UQuestionId = UQuestionId;
        this.question = question;
        this.userAnswers = new ArrayList<UAnswer>();


    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setUQuestionId(int UQuestionId) {
        this.UQuestionId = UQuestionId;
    }

    public int getUQuestionId() {
        return UQuestionId;
    }

    public void setUserAnswers(List userAnswers) {
        this.userAnswers = userAnswers;
    }

    public List getUserAnswers() {
        return userAnswers;
    }


}
