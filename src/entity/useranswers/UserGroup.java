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

    public UserGroup(String gn){
        this();
        this.groupName = gn;

    }

    public UserGroup(){
        groupId = 0;
        groupName = "";
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

    /*public void setGroupList(List gn) {
        groupList.add(gn);
    }

    public List getGroupList(){
        return groupList;
    }*/


}
