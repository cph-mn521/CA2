/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import util.EMF_Creator;

/**
 *
 * @author jonab
 */
public class FacadeController {

    private static CompanyFacade companyFacade;
    private static PersonFacade personFacade;
    
    public FacadeController() {
        FacadeController.companyFacade = CompanyFacade.getFacade(EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE));
        FacadeController.personFacade = PersonFacade.getFacade(EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE));
    }

    public CompanyFacade getCompanyFacade() {
        return companyFacade;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }
    

}
