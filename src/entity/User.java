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
    private String firstName;
    private String lastName;

    public User(int uid, String firstName, String lastName) {
        super();
        this.uid = uid;
        this.firstName = firstName;

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
