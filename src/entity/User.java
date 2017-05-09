package entity;

import entity.useranswers.UTest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class User {

    @Transient
    public static final int ADMIN = 1;
    @Transient
    public static final int TEACHER = 2;
    @Transient
    public static final int STUDENT = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int uid;
    private String firstName;
    private String lastName;
    private int role;

    @OneToMany(targetEntity = Test.class, cascade = CascadeType.PERSIST)
    private List testsToTake;

    @OneToMany(targetEntity = UTest.class, cascade = CascadeType.PERSIST)
    private List takenTests;

    public User(String firstName, String lastName, int role) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
    public User(){
        super();
        testsToTake = new ArrayList<Test>();
        takenTests = new ArrayList<UTest>();

}

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String ename) {
        this.firstName = ename;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String fname) {
        this.lastName = fname;
    }
}
