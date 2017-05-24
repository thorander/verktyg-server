package service;

import entity.Test;
import entity.useranswers.UserGroup;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by phili on 2017-05-18.
 */
public class GroupService {

    EntityManagerFactory emf;
    EntityManager em;

    private UserGroup userGroup;

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

    public void persistGroup(){
        em.getTransaction().begin();
        em.persist(userGroup);
        em.getTransaction().commit();
    }

    public void createGroup(UserGroup ug){
        this.userGroup = ug;
        persistGroup();
    }

    public EntityManager getEm(){
        return em;
    }
}
