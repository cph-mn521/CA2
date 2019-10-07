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
    private static Hobby h1, h2, h3;
    private static List<Hobby> hl1, hl2, hl3;
    private static InfoEntity non;

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

        h1 = new Hobby("Tennis", "slå til bolden egår?");
        h2 = new Hobby("Fodbold", "classisk holdsport");
        h3 = new Hobby("Curling", "noget med is og en bowling kugle?");

        hl1 = new ArrayList<>();
        hl2 = new ArrayList<>();
        hl3 = new ArrayList<>();

        hl1.add(h1);
        hl1.add(h2);
        hl1.add(h3);

        hl2.add(h2);
        hl2.add(h1);

        hl3.add(h3);

        non = new InfoEntity();

        p1 = new Person("stig", "Stigson", non, hl1);
        p2 = new Person("test", "testerson", non, hl2);
        p3 = new Person("kim", "kimson", non, hl3);

        h1.addPerson(p1);
        h1.addPerson(p2);
        h2.addPerson(p1);
        h2.addPerson(p2);
        h3.addPerson(p3);
        h3.addPerson(p1);

    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
//            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
//            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.persist(h1);
            em.persist(h2);
            em.persist(h3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetAll() {
        List<Person> P = facade.getAllPersons();
        assertThat(P, containsInAnyOrder(p1, p2, p3));
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
