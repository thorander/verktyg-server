package service;

import entity.Question;
import entity.Test;
import entity.useranswers.UQuestion;
import entity.useranswers.UTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Markus on 2017-05-24.
 */
public class UTestService {

    EntityManagerFactory emf;
    EntityManager em;

    private UTest test;

    public UTestService(){
        emf = Persistence.createEntityManagerFactory("JPAVerktyg");
        em = emf.createEntityManager();
    }

    public void setTest(UTest t){
        test = t;
    }

    public UTest getTest(){
        return test;
    }

    public void addQuestion(UQuestion q){
        test.addQuestion(q);
    }

    public void persistTest(){
        em.getTransaction().begin();
        em.persist(test);
        em.getTransaction().commit();
    }

    public void createTest(UTest test){
        this.test = test;
        persistTest();
    }

    public EntityManager getEm(){
        return em;
    }
}
