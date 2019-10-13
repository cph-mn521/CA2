/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REST;

import Entities.Hobby;
import Entities.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import exceptions.PersonNotFoundException;
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
        return "{\"msg\":\"Person API\"}";
    }

    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersons() {
        return GSON.toJson(FC.getPersonFacade().getAllPersons());
    }

    @Path("hobby/{name}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByHobby(@PathParam("name") String name) throws PersonNotFoundException {
        return GSON.toJson(FC.getPersonFacade().getByHobby(name));
    }
    
    @Path("zipcode/{zipcode}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByZipcode(@PathParam("zipcode") int zipcode) throws PersonNotFoundException {
        return GSON.toJson(FC.getPersonFacade().getByZipcode(zipcode));
    }

    @Path("edit/{id}")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String editPerson(@PathParam("id") long id, String person) throws PersonNotFoundException {
        Person p = GSON.fromJson(person, Person.class);
        p.setId(id);
        FC.getPersonFacade().editPerson(p);
        return GSON.toJson(FC.getPersonFacade().getPersonById(p.getId()));
    }

    @Path("createperson")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public void createPerson(String person) {
        Person p = new Person();
        Hobby h = new Hobby();
        JsonObject json = GSON.fromJson(person, JsonObject.class);
        JsonObject personObject = json.getAsJsonObject("person");
        JsonObject hobbyObject = json.getAsJsonObject("hobby");

        p.setFirstName(personObject.get("firstName").toString().replace("\"", ""));
        p.setLastName(personObject.get("lastName").toString().replace("\"", ""));

        h.setName(hobbyObject.get("name").toString().replace("\"", ""));
        h.setDescription(hobbyObject.get("description").toString().replace("\"", ""));
  
        FC.getPersonFacade().addPersonWithHobby(p, h);
    }
   

    @GET
    @Path("populate")
    public void populate() {
        FC.getPersonFacade().populate();
    }
}
