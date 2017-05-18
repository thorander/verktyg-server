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

    private UserGroup ug;

    public EntityManager getEm() {
        return em;
    }


    public UserGroup getUg() {
        return ug;
    }

    public void setUg(UserGroup ug) {
        this.ug = ug;
    }

    public GroupService(){
        emf = Persistence.createEntityManagerFactory("JPAVerktyg");
        em = emf.createEntityManager();
    }
    public void persistGroup(){
        em.getTransaction().begin();
        em.persist(ug);
        em.getTransaction().commit();
    }
}
