package com.bradenn.dev.enclave.persistent;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {

    public static MongoClient mongoClient;
    public static MongoDatabase database;

    public static void connect() {
        mongoClient = MongoClients.create("mongodb://localhost:27017/?retryWrites=true&w=majority");
        database = mongoClient.getDatabase("enclaves");
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    public static MongoCollection<Document> getCollection(String collection) {
        return database.getCollection(collection);
    }


}
