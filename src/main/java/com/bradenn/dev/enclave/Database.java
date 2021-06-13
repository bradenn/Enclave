package com.bradenn.dev.enclave;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.configuration.Configuration;

import java.util.Objects;
import java.util.logging.Level;

public class Database {

    public static MongoClient mongoClient;
    public static MongoDatabase database;

    public static void connect() {

        try {
            mongoClient = getClient();


        MongoCollection<Document> c = mongoClient.getDatabase("enclaves").getCollection("regions");
        Main.plugin.getLogger().log(Level.INFO, "Successfully loaded " + c.countDocuments() + " enclaves.");

        Configuration config = Main.plugin.getConfig();
        String databaseName = Objects.requireNonNull(config.getString("mongo.database"));
        database = mongoClient.getDatabase(databaseName);
        } catch (Exception e) {
            Main.plugin.getLogger().log(Level.SEVERE, "Failed.");

        }
    }

    private static MongoClient getClient() {
        Configuration config = Main.plugin.getConfig();
        String uri = Objects.requireNonNull(config.getString("mongo.uri"));
        ConnectionString cS = new ConnectionString(uri);

        return MongoClients.create(cS);
    }

    public static MongoCollection<Document> getCollection(String collection) {
        return database.getCollection(collection);
    }


}
