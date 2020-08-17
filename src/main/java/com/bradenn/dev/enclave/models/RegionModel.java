package com.bradenn.dev.enclave.models;

import com.bradenn.dev.enclave.persistent.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegionModel {

    private UUID enclaveUUID;
    private List<String> members;

    MongoDatabase db = Database.getDatabase();

    // Get existing enclave
    public RegionModel(UUID uuid) {
        MongoCollection<Document> collection = db.getCollection("enclaves");
        Document enclaveDoc = collection.find(Filters.eq("uuid", uuid.toString())).first();
        enclaveUUID = uuid;
    }

    // Create a new enclave
    public RegionModel(UUID uuid, String name) {
        members = new ArrayList<>();
        members.add(uuid.toString());
        MongoCollection<Document> collection = db.getCollection("enclaves");
        enclaveUUID = UUID.randomUUID();
        new PlayerModel(uuid).setEnclave(enclaveUUID);
        Document enclaveDoc = new Document("uuid", enclaveUUID.toString())
                .append("name", name)
                .append("color", "#CCCCCC")
                .append("owner", uuid.toString())
                .append("members", members);
        collection.insertOne(enclaveDoc);
    }

    public String getName(){
        MongoCollection<Document> collection = db.getCollection("enclaves");
        Document enclaveDoc = collection.find(Filters.eq("uuid", enclaveUUID.toString())).first();
        return enclaveDoc.getString("name");
    }

}
