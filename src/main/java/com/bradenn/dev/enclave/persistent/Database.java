package com.bradenn.dev.enclave.persistent;

import com.bradenn.dev.enclave.Main;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.configuration.Configuration;

import java.util.Objects;

public class Database {

    public static MongoClient mongoClient;
    public static MongoDatabase database;

    public static void connect() {
        Configuration config = Main.plugin.getConfig();

        String uri = Objects.requireNonNull(config.getString("mongo.uri"));
        mongoClient = MongoClients.create(uri);

        String databaseName = Objects.requireNonNull(config.getString("mongo.database"));
        database = mongoClient.getDatabase(databaseName);
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    public static MongoCollection<Document> getCollection(String collection) {
        return database.getCollection(collection);
    }


}
