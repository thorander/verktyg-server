package service;

import core.Connection;
import entity.Test;
import entity.User;
import entity.useranswers.UserGroup;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by phili on 2017-05-18.
 */


public class GroupService {

    EntityManagerFactory emf;
    EntityManager em;

    private UserGroup userGroup;
    private User user;

    public GroupService(){
        emf = Persistence.createEntityManagerFactory("JPAVerktyg");
        em = emf.createEntityManager();
    }

    public void setUserGroup(UserGroup ug){
        userGroup = ug;
    }

    public UserGroup getUserGroup(){
        return userGroup;
    }

    public void createGroup(UserGroup ug){
        this.userGroup = ug;
        persistGroup();
    }

    public void persistGroup(){
        System.out.println(userGroup.toString());
        em.getTransaction().begin();
        em.persist(userGroup);
        em.getTransaction().commit();
    }

    public String getUsers() {
        String s = "USERSFORGROUP";
        TypedQuery<User> query =
                em.createQuery("SELECT c FROM User c", User.class);

        List<User> users = query.getResultList();

        for (User u : users) {
            s += "#" + u.getFirstName();
        }
        return s;
    }

    public String getGroups() {
        String s = "GETGROUPS";
        TypedQuery<UserGroup> query =
                em.createQuery("SELECT c FROM UserGroup c", UserGroup.class);

        List<UserGroup> groups = query.getResultList();

        for (UserGroup u : groups) {
            s += "#" + u.getGroupName();
        }
        return s;
    }

    public EntityManager getEm(){
        return em;
    }
}
