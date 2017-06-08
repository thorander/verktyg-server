package service;

import entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * A service class for User
 */
public class UserService {
    EntityManagerFactory emf;
    EntityManager em;

    public UserService(){
        emf = Persistence.createEntityManagerFactory("JPAVerktyg");
        em = emf.createEntityManager();
    }

    public void createUser(User user){
        em.getTransaction().begin();
        em.persist(user);
        em.flush();
        em.getTransaction().commit();
    }

    public EntityManager getEm(){
        return em;
    }
}
