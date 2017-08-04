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
        this.datastore = morphia.createDatastore(mongoClient, DB_NAME);
        this.datastore.ensureIndexes();
        this.mongoProducer = new MongodbProducer(TOPIC_PRODUCER);
    }

    public DbQuery(Datastore datastore) {
        this.datastore = datastore;
        this.mongoProducer = new MongodbProducer(TOPIC_PRODUCER);
    }

    public Datastore getDatastore() {
        return this.datastore;
    }

    public User getUser(String lastName, String firstName) {

        User user = null;
        Query<User> query = this.datastore.createQuery(User.class)
                .field("lastName").contains(lastName)
                .field("firstName").contains(firstName);

        if (query.asList().size() == 1) {
            user = query.asList().get(0);
        }
        return user;
    }

    public Status addUser(User user) {

        String status = "Existing";
        Query<User> query = this.datastore.createQuery(User.class)
                .field("lastName").contains(user.getLastName())
                .field("firstName").contains(user.getFirstName());

        if (query.asList().isEmpty()) {
            this.datastore.save(user);
            this.mongoProducer.produceOneEvent(user.UserAsString());
            status = "Created";

        }

        return new Status(user, status);
    }

    public List<Status> addMultipleUser(List<User> users) {

        String status = "Existing";

        ListIterator<User> it = users.listIterator();
        List<Status> listUserStatus = new ArrayList<>();

        while (it.hasNext()) {

            User user = it.next();
            Query<User> query = this.datastore.createQuery(User.class).field("lastName").contains(user.getLastName())
                    .field("firstName").contains(user.getFirstName());

            if (query.asList().isEmpty()) {
                this.datastore.save(user);
                this.mongoProducer.produceOneEvent(user.UserAsString());
                status = "Created";
            }

            listUserStatus.add(new Status(user, status));

        }

        return listUserStatus;
    }
}
