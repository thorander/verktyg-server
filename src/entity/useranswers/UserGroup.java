package entity.useranswers;

import core.Connection;
import core.Main;
import entity.User;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import org.eclipse.persistence.indirection.IndirectList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phili on 2017-05-11.
 */

@Entity
/*@NamedQuery(name="UserGroup.findByName",
        query="SELECT c FROM User c WHERE c.firstName = :firstname")*/
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int groupId;

    @Column
    private String groupName;

    private String selectedUsers;

    public UserGroup(String gn, String us){
        this();
        this.groupName = gn;
        this.selectedUsers = us;
    }

    public UserGroup(){
        groupId = 0;
        groupName = "";
        selectedUsers = "";
    }

    public void setGroupId(int gi) {
        this.groupId = gi;
    }

    public int getGroupId(){
        return groupId;
    }

    public void setGroupName(String gn) {
        this.groupName = gn;
    }

    public String getGroupName(){
        return groupName;
    }

    public String getSelectedUsers() {
        /*if (selectedUsers.contains(",")) {
        } else {
            throw new IllegalArgumentException("String " + selectedUsers + " does not contain -");
        }*/
        return selectedUsers;
    }

    /*public void setGroupList(List gn) {
        groupList.add(gn);
    }

    public List getGroupList(){
        return groupList;
    }*/


}
