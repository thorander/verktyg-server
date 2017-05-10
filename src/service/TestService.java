package service;

import entity.Test;
import entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Matilda on 2017-05-10.
 */
public class TestService {

    EntityManagerFactory emf;
    EntityManager em;

    private Test test;

    public TestService(){
        emf = Persistence.createEntityManagerFactory("JPAVerktyg");
        em = emf.createEntityManager();
    }

    public void createTest(Test test){
        this.test = test;
        em.getTransaction().begin();
        em.persist(test);
        em.getTransaction().commit();
    }

    public EntityManager getEm(){
        return em;
    }
}
