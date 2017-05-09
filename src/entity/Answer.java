package entity;


import javax.persistence.*;

@Entity
@Table
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String answer;
    private boolean correct;

    @OneToOne
    private Question question;

    public Answer(String answer, boolean correct, Question question) {
        super();
        this.answer = answer;
        this.correct = correct;
        this.question = question;
    }

    public Answer() {
        super();
        //answer = "";
    }

    public Answer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCorrect(){
        return correct;
    }

    public void setCorrect(boolean correct){
        this.correct = correct;
    }
}
