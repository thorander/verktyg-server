package entity.useranswers;

import entity.Test;

import javax.persistence.*;


@Entity
public class UTest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int UTestId;

    @OneToOne
    private Test testAnswered;


}
