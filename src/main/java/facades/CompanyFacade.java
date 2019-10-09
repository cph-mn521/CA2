/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import Entities.Address;
import Entities.CityInfo;
import Entities.Company;
import Entities.Hobby;
import Entities.InfoEntity;
import Entities.Person;
import Entities.Phone;
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

    public void addInfoEntity(InfoEntity ie) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(ie);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void addAddress(Address a) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(a);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void addPhone(Phone p) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
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

    public void editCompany(Company c) throws CompanyNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new CompanyNotFoundException("No Company with provided id exists in database");
        } finally {
            em.close();
        }
    }

    public Company getCompanyById(long id) throws CompanyNotFoundException {
        EntityManager em = emf.createEntityManager();
        if (em.find(Company.class, id) != null) {
            return em.find(Company.class, id);
        }
        throw new CompanyNotFoundException("No Company with provided id exists in database");
    }

    public void populate() {
        CompanyFacade cf = getFacade(EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.DROP_AND_CREATE));
        EntityManager em = cf.getEntityManager();
        Company c1 = new Company();
        c1.setName("McDonald's");
        c1.setDescription("Fast Food");
        c1.setId((long) 123456789);
        c1.setNumEmployees(30000);
        c1.setMarketValue(20000000);

        InfoEntity ie = new InfoEntity();
        ie.setEmail("McDonalds@mcd.dk");

        Phone p = new Phone();
        p.setNumber("+45 33 45 12 28");
        p.setDescription("Official McD");

        List<Phone> phones = new ArrayList();
        phones.add(p);
//        phone.setInfo(ie);
//        List<Phone> phones = new ArrayList();
//        phones.add(phone);

        Address a = new Address();
        a.setStreet("Kongevejen");
        a.setCity(new CityInfo("Lyngby", 2800, null));
        a.setAdditionalInfo("AdditionalInfo");

        cf.addPhone(p);
        cf.addInfoEntity(ie);
        cf.addCompany(c1);
        cf.addAddress(a);

        try {
            em.getTransaction().begin();
            Company company = em.find(Company.class, (long) 1);
            InfoEntity info = em.find(InfoEntity.class, (long) 1);
            Address address = em.find(Address.class, (long) 1);
            info.setAddress(address);
            info.setPhones(phones);
            company.setInfo(info);
            em.merge(company);
            em.merge(info);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }

}
