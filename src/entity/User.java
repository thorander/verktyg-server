package entity;

import entity.useranswers.UTest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name="User.findByName",
        query="SELECT c FROM User c WHERE c.username = :username")
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int uid;
    private String firstName;
    private String lastName;
    private String role;
    @Column(unique = true)
    private String username;
    private String password;

    @OneToMany(targetEntity = Test.class, cascade = CascadeType.PERSIST)
    private List testsToTake;

    @OneToMany(targetEntity = UTest.class, cascade = CascadeType.PERSIST)
    private List takenTests;

    public User(String firstName, String lastName, String role) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public User(String firstName, String lastName, String username, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = "admin";
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

    public String getUsername() { return username;}

    public void setUsername(String username){this.username = username;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}
}
