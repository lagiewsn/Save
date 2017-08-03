/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sukeban.user.management.api;

import com.mongodb.MongoClient;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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

    public UserStatus addUser(User user) {

        UserStatus userStatus = null;
        Query<User> query = null;
        String status = "Existing";

        query = this.datastore.createQuery(User.class).field("lastName")
                .contains(user.getLastName()).field("firstName").contains(user.getFirstName());

        if (query.asList().isEmpty()) {
            this.datastore.save(user);
            mongoProducer.produceOneEvent(user.UserAsString());
            status = "Created";

        }

        return userStatus = new UserStatus(user, status);
    }

    public List<UserStatus> addMultipleUser(List<User> users) {
        
        UserStatus userStatus = null;
        String status = "Existing";
        Query<User> query = null;
        ListIterator<User> it = users.listIterator();
        List<UserStatus> listUserStatus = new ArrayList<>();

        while (it.hasNext()) {

            User user = it.next();
            query = this.datastore.createQuery(User.class).field("lastName")
                    .contains(user.getLastName()).field("firstName").contains(user.getFirstName());

            if (query.asList().isEmpty()) {
                this.datastore.save(user);
                mongoProducer.produceOneEvent(user.UserAsString());
                status = "Created";
            }
            
            listUserStatus.add(new UserStatus(user, status));
            
        }
        
        return listUserStatus;
    }
}
