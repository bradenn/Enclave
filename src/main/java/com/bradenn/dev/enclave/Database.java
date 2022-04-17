package com.bradenn.dev.enclave;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
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

        return MongoClients.create(buildMongoClientSettings(cS));
    }

    private static MongoClientSettings buildMongoClientSettings(ConnectionString clusterUrl) {
        return MongoClientSettings.builder()
                .codecRegistry(codecRegistries())
                .applyConnectionString(clusterUrl)
                .build();
    }

    private static CodecRegistry codecRegistries() {
        return CodecRegistries.fromRegistries(
                // save uuids as UUID, instead of LUUID
                CodecRegistries.fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD)),
                MongoClientSettings.getDefaultCodecRegistry()
        );
    }
    public static MongoCollection<Document> getCollection(String collection) {
        return database.getCollection(collection);
    }


}
