/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import Entities.Address;
import Entities.CityInfo;
import Entities.Hobby;
import Entities.InfoEntity;
import Entities.Person;
import exceptions.PersonNotFoundException;
import java.util.ArrayList;
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

    public void addPersonWithHobby(Person p, Hobby h) {
        EntityManager em = getEntityManager();
        PersonFacade cf = getFacade(EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.DROP_AND_CREATE));
        Person person = cf.addPerson(p);
        cf.addHobby(h);
        Person personP = em.find(Person.class, (long) person.getId());
        personP.addHobby(h);
        try {
            em.getTransaction().begin();
            em.merge(personP);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Person addPerson(Person p) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.flush();
            em.getTransaction().commit();
            return p;
        } finally {
            em.close();
        }
    }

    public Hobby addHobby(Hobby h) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(h);
            em.flush();
            em.getTransaction().commit();
            return h;
        } finally {
            em.close();
        }
    }

    public CityInfo addCityInfo(CityInfo ci) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(ci);
            em.flush();
            em.getTransaction().commit();
            return ci;
        } finally {
            em.close();
        }
    }

    public InfoEntity addInfoEntity(InfoEntity ie) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(ie);
            em.flush();
            em.getTransaction().commit();
            return ie;
        } finally {
            em.close();
        }
    }

    public Address addAddress(Address a) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(a);
            em.flush();
            em.getTransaction().commit();
            return a;
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

    public List getByHobby(String name) throws PersonNotFoundException {
        EntityManager em = getEntityManager();

        try {
            List<Person> out = em.createNamedQuery("Person.getByHobby").setParameter("name", name).getResultList();
            if (out.size() < 1) {
                throw new PersonNotFoundException("No person with hobbies found");
            }
            return out;
        } finally {
            em.close();
        }
    }
    
    public List getByZipcode(int zip) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        try {
            List<Person> out = em.createNamedQuery("Person.getByZipcode").setParameter("zipCode", zip).getResultList();
            if (out.size() < 1) {
                throw new PersonNotFoundException("No person with zipcode found");
            }
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
        InfoEntity ie1 = new InfoEntity();
        Address a1 = new Address();
        CityInfo ci1 = new CityInfo();
        List<Address> addresses = new ArrayList();

        h1.setName("Golf");
        h1.setDescription("Spiller Golf");
        p1.setFirstName("Jens");
        p1.setLastName("Larsen");
        a1.setStreet("Strandvejen");
        ci1.setZipCode(2950);
        ci1.setCity("Vedbæk");

        Person person = cf.addPerson(p1);
        Hobby hobby = cf.addHobby(h1);
        Address address = cf.addAddress(a1);
        addresses.add(address);
        CityInfo cityInfo = cf.addCityInfo(ci1);
        cityInfo.setAddresses(addresses);
        InfoEntity infoEntity = cf.addInfoEntity(ie1);

        EntityManager em = cf.getEntityManager();
        try {
            em.getTransaction().begin();
            
            person.addHobby(hobby);
            address.setCity(cityInfo);
            infoEntity.setAddress(address);
            person.setInfo(infoEntity);
            
            em.merge(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
