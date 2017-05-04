package entity;

import javax.persistence.*;

/**
 * Created by phili on 2017-05-04.
 */
@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int uid;
    private int admin = 1;
    private int teacher = 2;
    private int student = 3;
    private String firstName;
    private String lastName;




    public User(int uid, String firstName, String lastName) {
        super();
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;

        this.admin = admin;
        this.teacher = teacher;
        this.student = student;

    }
    public User(){
        super();
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
    public int getAdmin() {
        return admin;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public int getStudent() {
        return student;
    }

    public void setStudent(int student) {
        this.student = student;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
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
