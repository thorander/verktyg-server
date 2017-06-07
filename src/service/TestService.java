package service;

import entity.Question;
import entity.Test;
import entity.User;
import entity.useranswers.UTest;

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
    private UTest userTest;

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

    public UTest getUserTest( int id){

        TypedQuery<UTest> userTestById = em.createNamedQuery("UTest.getAll", UTest.class);
        UTest getUserTest =  userTestById.setParameter("id", id).getSingleResult();
        return getUserTest;
    }

    public Test getTestFromId(int id){
        TypedQuery<Test> testById = em.createNamedQuery("Test.findById", Test.class);
        Test testFromId = testById.setParameter("testId", id).getSingleResult();
        return testFromId;
    }

    public UTest getTestUser( String title, int id){

        TypedQuery<UTest> userTestById = em.createNamedQuery("UTest.getSelected", UTest.class);
        UTest getUserTest =  userTestById.setParameter("userId", id).setParameter("title", title).getSingleResult();
        return getUserTest;
    }

}
