package com.bradenn.dev.enclave.models;

import com.bradenn.dev.enclave.persistent.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.BasicBSONObject;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

public class EnclaveModel {

    private UUID enclaveUUID;
    private List<String> members;

    MongoDatabase db = Database.getDatabase();
    MongoCollection<Document> collection = db.getCollection("enclaves");

    // Get existing enclave
    public EnclaveModel(UUID uuid) {
        Document enclaveDoc = collection.find(Filters.eq("uuid", uuid.toString())).first();
        enclaveUUID = uuid;
    }

    // Create a new enclave
    public EnclaveModel(UUID uuid, String name) {
        members = new ArrayList<>();
        members.add(uuid.toString());
        enclaveUUID = UUID.randomUUID();
        new PlayerModel(uuid).setEnclave(enclaveUUID);
        Document enclaveDoc = new Document("uuid", enclaveUUID.toString())
                .append("name", name)
                .append("color", "#CCCCCC")
                .append("tags", new BsonArray())
                .append("owner", uuid.toString())
                .append("members", members);
        collection.insertOne(enclaveDoc);
    }

    public boolean isOwner(UUID playerUUID){
        Bson query = Filters.and(
                Filters.eq("uuid", enclaveUUID.toString()),
                Filters.eq("owner", playerUUID.toString()));
        Document ownerDoc = collection.find(query).first();
        return ownerDoc != null;
    }

    public void disbandEnclave(){
        collection.findOneAndDelete(new Document("uuid", enclaveUUID.toString()));
        MongoCollection<Document> regions = db.getCollection("regions");
        FindIterable<Document> regionDocs = regions.find(Filters.eq("enclave", enclaveUUID.toString()));
        for(Document d : regionDocs){
            regions.findOneAndUpdate(d, Updates.set("enclave", null));
        }
        MongoCollection<Document> players = db.getCollection("players");
        FindIterable<Document> playerDocs = players.find(Filters.eq("enclave", enclaveUUID.toString()));
        for(Document p : playerDocs){
            regions.findOneAndUpdate(p, Updates.set("enclave", null));
        }
    }

    public UUID getUUID(){
        return enclaveUUID;
    }

    public void setOwner(UUID uuid) {
        collection.findOneAndUpdate(new Document("uuid", enclaveUUID.toString()), Updates.set("owner", uuid.toString()));
    }

    public UUID getOwner() {
        Document enclaveDoc = collection.find(Filters.eq("uuid", enclaveUUID.toString())).first();
        if(enclaveDoc != null) {
            return UUID.fromString(Objects.requireNonNull(enclaveDoc).getString("owner"));
        }else{
            return null;
        }
    }

    public void setName(String name) {
        collection.findOneAndUpdate(new Document("uuid", enclaveUUID.toString()), Updates.set("name", name));
    }

    public String getName() {
        Document enclaveDoc = collection.find(Filters.eq("uuid", enclaveUUID.toString())).first();
        return Objects.requireNonNull(enclaveDoc).getString("name");
    }

    public void setColor(String color) {
        collection.findOneAndUpdate(new Document("uuid", enclaveUUID.toString()), Updates.set("color", color));
    }

    public String getColor() {
        Document enclaveDoc = collection.find(Filters.eq("uuid", enclaveUUID.toString())).first();
        return Objects.requireNonNull(enclaveDoc).getString("color");
    }

    public void setTag(String tag, boolean  value){

    }

    public boolean getTag(String tag){
        Document enclaveDoc = collection.find(Filters.eq("uuid", enclaveUUID.toString())).first();
        BsonArray bd = ((BsonArray) enclaveDoc.get("tags"));
        return false;
    }

}
