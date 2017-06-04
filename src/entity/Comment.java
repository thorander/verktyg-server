package entity;

import entity.useranswers.UQuestion;
import javax.persistence.*;

/**
 * A comment belonging to a question.
 */
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long commentId;

    private String comment;

    @OneToOne
    private UQuestion userQuestion;

    public Comment() {

    }

    public Comment(UQuestion userQuestion, String comment) {
        this.userQuestion = userQuestion;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCommentId() {
        return commentId;
    }

    public UQuestion getUserQuestion() {
        return userQuestion;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public void setUserQuestion(UQuestion userQuestion) {
        this.userQuestion = userQuestion;
    }
}
