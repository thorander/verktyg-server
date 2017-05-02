package entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String answer;

    public Answer(){
        answer = "";
    }

    public Answer(String answer){
        this.answer = answer;
    }

    public String getAnswer(){
        return answer;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}
