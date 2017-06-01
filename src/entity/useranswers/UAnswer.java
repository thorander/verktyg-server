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
    private int answerOrder;

    private boolean checked;

    private String answerText;

    public UAnswer() {

    }


    public UAnswer(Answer answer, int answerOrder, String answerText) {

        this.answer = answer;
        this.answerOrder = answerOrder;
        this.answerText = answerText;
        checked = false;

    }

    public UAnswer(Answer answer, int answerOrder, String answerText, boolean checked){
        this(answer, answerOrder, answerText);
        this.checked = checked;
    }

    public void setUAnswerId(int UAnswerId) {
        this.UAnswerId = UAnswerId;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }

    public int getUAnswerId() {
        return UAnswerId;
    }

    public int getAnswerOrder(){
        return answerOrder;
    }

    public boolean isChecked(){
        return checked;
    }

    public String getAnswerText(){ return answerText;}
}
