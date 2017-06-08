package entity.useranswers;

import entity.Answer;
import entity.Comment;
import entity.Question;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User question.
 * Keeps information about the answered question such as a reference to the actual question
 * and also how much score it got, and also keeps a list of user answers.
 */
@Entity
public class UQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int UQuestionId;
    @OneToOne
    private Question question;
    @OneToMany(targetEntity = UAnswer.class, cascade = CascadeType.PERSIST)
    private List userAnswers;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Comment comment;

    private int score;

    public UQuestion() {

    }

    public UQuestion(Question question) {
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

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }

    public void setComment(Comment c){
        comment = c;
    }

    public Comment getComment(){
        return comment;
    }

    public String getCommentText(){
        if(comment == null){
            return " ";
        }
        return comment.getComment();
    }


}
