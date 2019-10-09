/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import Entities.Company;
import Entities.Hobby;
import Entities.InfoEntity;
import Entities.Person;
import Entities.Phone;
import facades.CompanyFacade;
import facades.PersonFacade;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.EMF_Creator;

/**
 *
 * @author Martin
 */
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person p1, p2, p3;
    private static Hobby h1, h2, h3, h4;
    private static List<Hobby> hl;
    private static List<Person> pl;
    private static InfoEntity non;
    private static long testnr = 0;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/CA2_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = PersonFacade.getFacade(emf);
        EntityManager em = emf.createEntityManager();
        h1 = new Hobby("Tennis", "slå til bolden egår?");
        h2 = new Hobby("Fodbold", "classisk holdsport");
        h3 = new Hobby("Curling", "noget med is og en bowling kugle?");
        h4 = new Hobby("Venskab", "aaawwwww");
        try {
            em.getTransaction().begin();
            em.persist(h1);
            em.persist(h2);
            em.persist(h3);
            em.persist(h4);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(h4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {

        EntityManager em = emf.createEntityManager();
        p1 = new Person("Donald", "J Trump", null, null);
        p2 = new Person("Anders", "And", null, null);
        p3 = new Person("NaNNaNNaNNaNNaN", "Batman", null, null);

        try {
            em.getTransaction().begin();
            em.clear();
            em.flush();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
            for (long i = 1; i < 4; i++) {
                Person p = em.find(Person.class, i + (testnr * 3));
                System.out.println(p.getId());
                Hobby h = em.find(Hobby.class, i);
                Hobby h2 = (Hobby) em.createNamedQuery("Hobby.getByName").setParameter("name", "Venskab").getResultList().get(0);
                p.addHobby(h);
                p.addHobby(h2);
                em.getTransaction().begin();
                em.merge(p);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
            
        }
    }

    @AfterEach
    public void tearDown() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows");
            em.getTransaction().commit();
        } finally {
            em.close();
            testnr++;
        }

    }

    @Test
    public void Testadd() {
        long count1 = facade.getPersonCount();
        Person Test = new Person("test", "testerson", null, null);
        facade.addPerson(Test);
        long count2 = facade.getPersonCount();
        assertNotEquals(count1, count2);
    }

    @Test
    public void TestNamedQueryJoin() {
        EntityManager em = emf.createEntityManager();
        List<Person> RS1 = em.createNamedQuery("Person.getByHobby").setParameter("name", "Tennis").getResultList();
        List<Person> RS2 = em.createNamedQuery("Person.getByHobby").setParameter("name", "Venskab").getResultList();
        assertEquals(3+3*testnr,RS2.size());
        assertEquals(1+testnr, RS1.size());
    }
    
    @Test
    public void testEdit(){
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, (long)1);
        try {
            Person p2 = new Person(p.getFirstName(),p.getLastName(),null,null);
            p.setFirstName("change");
            p.setLastName("changed");
            facade.editPerson(p);
            p = em.find(Person.class, (long)1);
            assertTrue(p.getFirstName()!= p2.getFirstName() && p.getLastName() != p2.getLastName());
        } catch (Exception e) {
            fail();
        }
        
        
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
