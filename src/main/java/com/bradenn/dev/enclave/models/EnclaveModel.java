package com.bradenn.dev.enclave.models;

import com.bradenn.dev.enclave.persistent.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.md_5.bungee.api.ChatColor;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

public class EnclaveModel {

    private final UUID enclaveUUID;

    MongoDatabase db = Database.getDatabase();
    MongoCollection<Document> collection = db.getCollection("enclaves");

    /**
     * Find Enclave model
     * @param uuid
     */
    public EnclaveModel(UUID uuid) {
        enclaveUUID = uuid;
    }

    /**
     * Create model model
     * @param uuid
     * @param name
     */
    public EnclaveModel(UUID uuid, String name) {
        List<String> members;
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

    /**
     * Check if the instance exists in the database
     * @return boolean
     */
    public boolean isValid(){
        return collection.find(new Document("uuid", enclaveUUID.toString())).first() != null;
    }

    /**
     * Check if the provided UUID belongs to the Enclave owner
     * @param playerUUID
     * @return boolean
     */
    public boolean isOwner(UUID playerUUID){
        Bson query = Filters.and(
                Filters.eq("uuid", enclaveUUID.toString()),
                Filters.eq("owner", playerUUID.toString()));
        Document ownerDoc = collection.find(query).first();
        return ownerDoc != null;
    }

    /**
     * Upon disbanding the enclave, remove all claims from regions, remove all reference from players in guild
     */
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

    /**
     * Get the enclave's UUID
     * @return UUID
     */
    public UUID getUUID(){
        return enclaveUUID;
    }

    /**
     * Set the owner of the enclave (overwrite)
     * @param uuid
     */
    public void setOwner(UUID uuid) {
        collection.findOneAndUpdate(new Document("uuid", enclaveUUID.toString()), Updates.set("owner", uuid.toString()));
    }

    /**
     * Get the UUID of the player whom owns the enclave
     * @return UUID
     */
    public UUID getOwner() {
        Document enclaveDoc = collection.find(Filters.eq("uuid", enclaveUUID.toString())).first();
        if(enclaveDoc != null) {
            return UUID.fromString(Objects.requireNonNull(enclaveDoc).getString("owner"));
        }else{
            return null;
        }
    }

    /**
     * Get the UUID of the player whom owns the enclave
     * @return UUID
     */
    public boolean hasMember(UUID uuid) {
        Document enclaveDoc = collection.find(Filters.eq("uuid", enclaveUUID.toString())).first();
        if(enclaveDoc != null) {
           return Objects.requireNonNull(enclaveDoc).getString("members").contains(uuid.toString());
        }else{
            return false;
        }
    }

    /**
     * Set the name of the enclave (overwrite)
     * @param name
     */
    public void setName(String name) {
        collection.findOneAndUpdate(new Document("uuid", enclaveUUID.toString()), Updates.set("name", name));
    }

    /**
     * Get the name of the enclave
     * @return String
     */
    public String getName() {
        Document enclaveDoc = collection.find(Filters.eq("uuid", enclaveUUID.toString())).first();
        return Objects.requireNonNull(enclaveDoc).getString("name");
    }

    /**
     * Get the formatted name of the enclave
     * @return String
     */
    public String getDisplayName() {
        return ChatColor.of(getColor())+getName();
    }


    /**
     * Set the color of the enclave tag (overwrite)
     * @param color
     */
    public void setColor(String color) {
        collection.findOneAndUpdate(new Document("uuid", enclaveUUID.toString()), Updates.set("color", color));
    }

    /**
     * Get the hex color of the enclave
     * @return String
     */
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
