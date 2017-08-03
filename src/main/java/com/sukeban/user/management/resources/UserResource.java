package com.sukeban.user.management.resources;

import com.sukeban.user.management.api.DbQuery;
import com.sukeban.user.management.api.User;
import com.sukeban.user.management.api.UserStatus;
import java.util.List;
import javax.ws.rs.Consumes;
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

    DbQuery dbQuery;

    public UserResource(Datastore datastore) {

        this.dbQuery = new DbQuery(datastore);

    }

    @GET
    @Path("/get/{lastName}/{firstName}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("lastName") String lastName, @PathParam("firstName") String firstName) {
        return this.dbQuery.getUser(lastName, firstName);
    }

    @POST
    @Path("/add-one-user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserStatus addUser(User user) {
        return this.dbQuery.addUser(user);
    }

    @POST
    @Path("/add-multiple-user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserStatus> addMultipleUser(List<User> users) {
        return this.dbQuery.addMultipleUser(users);
    }
}
