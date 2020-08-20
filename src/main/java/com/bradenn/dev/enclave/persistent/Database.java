package com.bradenn.dev.enclave.persistent;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {

    public static MongoClient mongoClient;
    public static MongoDatabase database;

    public static void connect() {
        mongoClient = MongoClients.create("mongodb+srv://minecraft:fRenrJDxHO7RanSh@bn-mda-001-wjpga.mongodb.net/?retryWrites=true&w=majority");
        database = mongoClient.getDatabase("minecraft");
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}
