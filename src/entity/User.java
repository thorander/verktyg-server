package entity;

import entity.useranswers.UTest;
import org.eclipse.persistence.indirection.IndirectList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NamedQueries({@NamedQuery(name ="User.getStudents", query="SELECT c FROM User c WHERE   c.role = '" + "student" + "'"),
@NamedQuery(name="User.findByMail",
        query="SELECT c FROM User c WHERE c.email = :email"),
        @NamedQuery(name="User.findUserByUTest",
query = "SELECT p FROM User p, UTest u WHERE u MEMBER OF p.takenTests AND u.testAnswered.testId = :testId "),
@NamedQuery(name="User.findById", query="SELECT c FROM User c WHERE c.uid = :uid"),
@NamedQuery(name="User.findUserWhoTookTest", query="SELECT c FROM User c JOIN UTest t WHERE c.role = 'student' AND t MEMBER OF c.takenTests AND t.testAnswered.title = :testTitle")})
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int uid;
    private String firstName;
    private String lastName;
    private String role;
    @Column(unique = true)
    private String email;
    private String password;

    @OneToMany(targetEntity = Test.class)
    private List testsToTake;

    @OneToMany(targetEntity = UTest.class)
    private List takenTests;

    public User(String firstName, String lastName, String role) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public User(String firstName, String lastName, String email, String password, String role){
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(){
        super();
        testsToTake = new IndirectList();
        takenTests = new IndirectList();
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

    public String getEmail() { return email;}

    public void setEmail(String email){this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    public void addTestToTake(Test t){
        testsToTake.add(t);
    }

    public void removeTestToTake(Test t) {
        for(Object test : testsToTake){
            if(((Test)test).getTestId() == t.getTestId()){
                testsToTake.remove(((Test)test));
                return;
            }
        }
    }

    public void addTakenTest(UTest t) { takenTests.add(t);}

    public void removeTakenTest(UTest t) { takenTests.remove(t);}

    public String getAvailableTests(){
        String tests = "AVAILABLETESTS#";
        for(Object o : testsToTake){
            tests += ((Test)o).getTitle() + "#" + ((Test)o).getTestId() + "#";
        }
        return tests;
    }

    @Override
    public String toString() {
        return "User [firstName=" + firstName + "]";
    }

    public boolean hasNotTaken(Test testById) {
        for(Object u : takenTests){
            if(((UTest)u).getTestAnswered().getTestId() == testById.getTestId()){
                return false;
            }
        }

        for(Object t : testsToTake){
            if(((Test)t).getTestId() == testById.getTestId()){
                return false;
            }
        }
        return true;
    }
}
