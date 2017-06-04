package service;

import entity.Question;
import entity.Test;
import entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * A service class for Test
 */
public class TestService {

    EntityManagerFactory emf;
    EntityManager em;

    private Test test;

    public TestService(){
        emf = Persistence.createEntityManagerFactory("JPAVerktyg");
        em = emf.createEntityManager();
    }

    public void setTest(Test t){
        test = t;
    }

    public Test getTest(){
        return test;
    }

    public void addQuestion(Question q){
        test.addQuestion(q);
    }

    public void persistTest(){
        em.getTransaction().begin();
        em.persist(test);
        em.getTransaction().commit();
    }

    public void createTest(Test test){
        this.test = test;
        persistTest();
    }

    public EntityManager getEm(){
        return em;
    }

    public Test getTestFromId(int id){
        TypedQuery<Test> testById = em.createNamedQuery("Test.findById", Test.class);
        Test testFromId = testById.setParameter("testId", id).getSingleResult();
        return testFromId;
    }
}
