package entity;


import javax.persistence.*;

/**
 * An answer. Keeps properties related to an answer.
 */
@Entity
@Table
@NamedQuery(name="Answer.findById",
        query="SELECT c FROM Answer c WHERE c.id = :id")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String answer;
    private boolean correct;
    private int answerOrder;

    @OneToOne
    private Question question;

    public Answer(String answer, boolean correct, Question question) {
        super();
        this.answer = answer;
        this.correct = correct;
        this.question = question;
    }

    public Answer(String answer, boolean correct, Question question, int answerOrder){
        this(answer, correct, question);
        this.answerOrder = answerOrder;
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

    public int getAnswerOrder(){
        return answerOrder;
    }

    public void setOrder(int answerOrder){
        this.answerOrder = answerOrder;
    }
}
