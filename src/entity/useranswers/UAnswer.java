package entity.useranswers;

import entity.Answer;

import javax.persistence.*;

@Entity
public class UAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int UAnswerId;
    @OneToOne
    private Answer answer;
    private boolean correctAnswer;

    public UAnswer(){
    }


    public UAnswer(Answer answer,boolean correctAnswer){

        this.answer = answer;
        this.correctAnswer = correctAnswer;

    }

    public void setUAnswerId(int UAnswerId) {
        this.UAnswerId = UAnswerId;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public Answer getAnswer() {
        return answer;
    }

    public int getUAnswerId() {
        return UAnswerId;
    }
}
