/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import Entities.Hobby;
import Entities.Person;
import exceptions.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import util.EMF_Creator;

/**
 *
 * @author jonab
 */
public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void addPerson(Person person) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void addHobby(Hobby h) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(h);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Long getPersonCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long personCount = (long) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return personCount;
        } finally {
            em.close();
        }
    }

    public List getAllPersons() {
        EntityManager em = getEntityManager();
        try {            
            return em.createNamedQuery("Person.getAll").getResultList();
        } finally {
            em.close();
        }
    }


    public List getByHobby(String name) throws PersonNotFoundException{
        EntityManager em = getEntityManager();
        
        try {
            List<Person> out = em.createNamedQuery("Person.getByHobby").setParameter("name", name).getResultList();
            if(out.size()<1)throw new PersonNotFoundException("No person with hobbies found");
            return out;         
        } finally {
            em.close();
        }
    }

    public void editPerson(Person p) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new PersonNotFoundException("No Person with provided id exists in database");
        } finally {
            em.close();
        }
    }

    public Person getPersonById(long id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        if (em.find(Person.class, id) != null) {
            return em.find(Person.class, id);
        }
        throw new PersonNotFoundException("No Person with provided id exists in database");
    }

    public void populate() {
        PersonFacade cf = getFacade(EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.DROP_AND_CREATE));
        Hobby h1 = new Hobby();
        Person p1 = new Person();

        h1.setName("Frimærker");
        h1.setDescription("Samler på frimærker");
        p1.setFirstName("Stein");
        p1.setLastName("Bagger");

        cf.addPerson(p1);
        cf.addHobby(h1);

        EntityManager em = cf.getEntityManager();
        try {
            em.getTransaction().begin();
            Person person1 = em.find(Person.class, (long) 1);
            Hobby hobby1 = em.find(Hobby.class, (long) 1);
            person1.addHobby(hobby1);
            em.merge(person1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}


