package entity;


import javax.persistence.*;

@Entity
@Table
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String answer;

    public Answer(int id, String answer) {
        super();
        this.id = id;
        this.answer = answer;
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
}
