/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REST;

import entities.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.FacadeController;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import util.EMF_Creator;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("person")
public class PersonResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final FacadeController FC = new FacadeController();
//    private static final CarFacade FACADE = CarFacade.getFacade(EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE));

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public PersonResource() {
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hej verden!\"}";
    }

    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersons() {
        return GSON.toJson(FC.getPersonFacade().getAllPersons());
    }
    
//    @Path("edit/{id}")
//    @POST
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces({MediaType.APPLICATION_JSON})
//    public String editPerson(@PathParam("id") long id) {
//        Person p = GSON.fromJson(json, Person.class);
//        return GSON.toJson(FC.getPersonFacade().editPerson(p));
//    }

    /**
     * Retrieves representation of an instance of REST.GenericResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    @GET
    @Path("populate")
    public void populate() {
        FC.getPersonFacade().populate();
    }
}