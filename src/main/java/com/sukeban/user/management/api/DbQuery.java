/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sukeban.user.management.api;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;


public class DbQuery {


    private final Datastore datastore;
    private final Morphia morphia;
    private final MongoClient mongoClient;
    private static final String DB_NAME = "USER-MANAGEMENT-DB";

    public DbQuery() {
        
        morphia = new Morphia();
        mongoClient = new MongoClient();
        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("com.sukeban.user.management.api");

        // create the Datastore connecting to the default port on the local host
        datastore = morphia.createDatastore(mongoClient, DB_NAME);
        datastore.ensureIndexes();
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public static String getDB_NAME() {
        return DB_NAME;
    }

    public User getUser(String lastName, String firstName, Datastore datastore) {

        User user = null;
        Query<User> query = null;

        query = datastore.createQuery(User.class).field("lastName").contains(lastName).field("firstName").contains(firstName);
        if (query.asList().size() == 1) {
            user = query.asList().get(0);
        }
        return user;
    }

    public UserStatus addUser(String lastName, String firstName, Datastore datastore) {

        User user = null;
        UserStatus userStatus= null;
        Query<User> query = null;
        String status = "";
        DbQuery dbQuery = new DbQuery();

        query = datastore.createQuery(User.class).field("lastName")
                .contains(lastName).field("firstName").contains(firstName);


        if (query.asList().isEmpty()) {
            user = new User(lastName, firstName);
            datastore.save(user);
            status = "Created";

        } else {
            user = dbQuery.getUser(lastName, firstName, datastore);
            status = "Existing";
        }

        
        return userStatus = new UserStatus(user, status);
    }
}
