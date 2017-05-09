package service;

import entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Markus on 2017-05-09.
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
        em.getTransaction().commit();
    }
}
