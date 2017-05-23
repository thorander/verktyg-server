package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String question;

    private String questionType;

    private int score;

    @OneToMany(targetEntity = Answer.class, cascade = CascadeType.PERSIST)
    private List answers;

    @OneToOne
    private Test test;

    public Question(String question, String questionType, Test test) {
        super();
        this.question = question;
        this.test = test;
        answers = new ArrayList<Answer>();
        this.questionType = questionType;
    }

    public Question() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addAnswer(Answer a){
        answers.add(a);
    }

    public void setQuestionType(String questionType){this.questionType = questionType;}

    public String getQuestionType(){return questionType;}

    public String getSendData(){
        String s = "ADDTQUESTION#";
        s += getQuestion() + "#";
        s += getQuestionType() + "#";
        s += getId();
        for(Object o : answers){
            Answer temp = ((Answer)o);
            s += "#ANSWER#";
            s += temp.getAnswer() + "#";
            s += temp.getId() + "#";
            s += temp.isCorrect() + "#";
            s += temp.getAnswerOrder();
        }
        return s;
    }

}
