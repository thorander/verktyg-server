package entity.useranswers;

import core.Connection;
import core.Main;
import entity.User;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phili on 2017-05-11.
 */

@Entity
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int groupNumber;

    private String groupName;

    @OneToMany(targetEntity = User.class )
    private List users;


    public UserGroup(){
        this.groupNumber = groupNumber;
        this.users = new ArrayList<User>();
        this.groupName = "CREATEGROUP#";

    }


    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public String getGroupName() {
        return groupName;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public List getUsers() {
        return users;
    }

    public void setUsers(List users) {
        this.users = users;
    }
}
