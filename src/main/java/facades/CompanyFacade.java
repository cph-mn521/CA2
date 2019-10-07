/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import Entities.Company;
import Entities.Person;
import exceptions.CompanyNotFoundException;
import exceptions.PersonNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import util.EMF_Creator;

/**
 *
 * @author jonab
 */
public class CompanyFacade {

    private static CompanyFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private CompanyFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static CompanyFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CompanyFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void addCompany(Company company) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(company);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Long getCompanyCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long companyCount = (long) em.createQuery("SELECT COUNT(c) FROM Company c").getSingleResult();
            return companyCount;
        } finally {
            em.close();
        }
    }

    public List getAllCompanies() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Company> query = em.createQuery("Select c from Company c", Company.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Company editCompany(Company c) throws CompanyNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.merge(c);
        } catch (Exception e) {
            throw new CompanyNotFoundException("No Company with provided id exists in database");
        }
    }
    
    

    public void populate() {
        CompanyFacade cf = getFacade(EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.DROP_AND_CREATE));
//        cf.addCar(new Car(2000, "VW", "Golf", 10000, "Angela Merkel", "am11111"));
//        cf.addCar(new Car(2008, "Ford", "Ka", 15000, "Donald Trump", "dt22222"));
//        cf.addCar(new Car(2017, "Audi", "RS7", 800000, "Kim Jung Un", "kj33333"));
    }

}
