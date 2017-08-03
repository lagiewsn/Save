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

    Datastore datastore;
    private static final String DB_NAME = "USER-MANAGEMENT-DB";
    private final MongodbProducer mongoProducer;
    private static final String TOPIC_PRODUCER = "UPDATE-CAR-MANAGEMENT-DB";

    public DbQuery() {
        Morphia morphia = new Morphia();
        MongoClient mongoClient = new MongoClient();

        // create the Datastore connecting to the default port on the local host
        datastore = morphia.createDatastore(mongoClient, DB_NAME);
        datastore.ensureIndexes();
        mongoProducer = new MongodbProducer(TOPIC_PRODUCER);
    }

    public DbQuery(Datastore datastore) {
        this.datastore = datastore;
        mongoProducer = new MongodbProducer(TOPIC_PRODUCER);
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public User getUser(String lastName, String firstName) {

        User user = null;
        Query<User> query = null;

        query = this.datastore.createQuery(User.class).field("lastName").contains(lastName).field("firstName").contains(firstName);
        if (query.asList().size() == 1) {
            user = query.asList().get(0);
        }
        return user;
    }

    public UserStatus addUser(String lastName, String firstName) {

        User user = null;
        UserStatus userStatus = null;
        Query<User> query = null;
        String status = "";
        
        query = this.datastore.createQuery(User.class).field("lastName")
                .contains(lastName).field("firstName").contains(firstName);

        if (query.asList().isEmpty()) {
            user = new User(lastName, firstName);
            this.datastore.save(user);
            mongoProducer.produceOneEvent(user.UserAsString());
            status = "Created";

        } else {

            user = query.asList().get(0);

            status = "Existing";
        }

        return userStatus = new UserStatus(user, status);
    }
}
