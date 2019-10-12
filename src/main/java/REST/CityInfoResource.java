/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REST;

import DTO.ZipCodeDTO;
import DTO.ZipCodeErrorDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.ResponseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;

/**
 *
 * @author Niels Bang
 */
@Path("ZipCode")
public class CityInfoResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String URL = "https://dawa.aws.dk/postnumre";
    private final String USER_AGENT = "Mozilla/5.0";

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"ZipCode API\"}";
    }

    @Path("/{nr}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String editPerson(@PathParam("nr") int nr) throws ResponseException {
        String inputLine;
        StringBuilder response = new StringBuilder();
        ZipCodeDTO result;
        ZipCodeErrorDTO err;
        BufferedReader in;

        try {
            URL obj = new URL(URL + "/" + nr);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();

            if (responseCode == 200) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (responseCode == 200) {
                result = GSON.fromJson(response.toString(), ZipCodeDTO.class);
                return GSON.toJson(result);
            } else {
                err = GSON.fromJson(response.toString(), ZipCodeErrorDTO.class);
                err.setMsg("No city with that post number.");
                err.setError(responseCode);
                return GSON.toJson(err);
            }

        } catch (IOException me) {
            throw new ResponseException(me.getMessage());
        }
    }
}
