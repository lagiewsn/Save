/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sukeban.user.management.resources;

import com.sukeban.user.management.api.DbQuery;
import com.sukeban.user.management.api.User;
import com.sukeban.user.management.api.UserStatus;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.mongodb.morphia.Datastore;

@Path("sukeban/user/")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {


    Datastore datastore;
    
    DbQuery dbQuery = new DbQuery();

    public UserResource(Datastore datastore) {

        this.datastore = datastore;
    }

    @GET
    @Path("/{lastName}/{firstName}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("lastName") String lastName, @PathParam("firstName") String firstName) {
       return dbQuery.getUser(lastName, firstName, datastore);
    }

    @POST
    @Path("/{lastName}/{firstName}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserStatus addUser(@PathParam("lastName") String lastName, @PathParam("firstName") String firstName) {
        return dbQuery.addUser(lastName, firstName, datastore);
    }
}
