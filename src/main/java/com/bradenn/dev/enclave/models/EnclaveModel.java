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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EnclaveModel {

    private final UUID uuid;
    private final Document enclave;

    MongoDatabase db = Database.getDatabase();
    MongoCollection<Document> collection = db.getCollection("enclaves");

    /**
     * Find Enclave model
     */
    public EnclaveModel(UUID uuid) {
        this.uuid = uuid;
        enclave = getDocument();
    }

    private Document getDocument() {
        return collection.find(new Document("uuid", uuid.toString())).first();
    }

    /**
     * Create model model
     */
    public EnclaveModel(UUID playerUUID, String name) {
        List<String> members;
        members = new ArrayList<>();
        members.add(playerUUID.toString());
        this.uuid = UUID.randomUUID();
        new PlayerModel(playerUUID).setEnclave(this.uuid);
        Document enclaveDoc = new Document("uuid", this.uuid.toString())
                .append("name", name)
                .append("color", "#CCCCCC")
                .append("tags", new BsonArray())
                .append("owner", playerUUID.toString())
                .append("members", members);
        collection.insertOne(enclaveDoc);
        enclave = getDocument();
    }

    /**
     * Check if the instance exists in the database
     *
     * @return boolean
     */
    public boolean isValid() {
        return collection.find(new Document("uuid", uuid.toString())).first() != null;
    }

    /**
     * Check if the provided UUID belongs to the Enclave owner
     * @return boolean
     */
    public boolean isOwner(UUID playerUUID) {
        return enclave.getString("owner").equalsIgnoreCase(playerUUID.toString());
    }

    /**
     * Upon disbanding the enclave, remove all claims from regions, remove all reference from players in guild
     */
    public void disbandEnclave() {
        collection.findOneAndDelete(new Document("uuid", uuid.toString()));
        MongoCollection<Document> regions = db.getCollection("regions");
        FindIterable<Document> regionDocs = regions.find(Filters.eq("enclave", uuid.toString()));
        for (Document d : regionDocs) {
            regions.findOneAndDelete(d);
        }
        MongoCollection<Document> players = db.getCollection("players");
        FindIterable<Document> playerDocs = players.find(Filters.eq("enclave", uuid.toString()));
        for (Document p : playerDocs) {
            players.findOneAndUpdate(p, Updates.set("enclave", null));
        }
    }

    /**
     * Get the enclave's UUID
     * @return UUID
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Set the owner of the enclave (overwrite)
     */
    public void setOwner(UUID uuid) {
        collection.findOneAndUpdate(new Document("uuid", uuid.toString()), Updates.set("owner", uuid.toString()));
    }

    /**
     * Get the UUID of the player whom owns the enclave
     *
     * @return UUID
     */
    public UUID getOwner() {
        return UUID.fromString(enclave.getString("owner"));
    }

    /**
     * Get the UUID of the player whom owns the enclave
     *
     * @return UUID
     */
    public boolean hasMember(UUID uuid) {
        return enclave.getList("members", String.class).contains(uuid.toString());
    }

    /**
     * Set the name of the enclave (overwrite)
     */
    public void setName(String name) {
        collection.findOneAndUpdate(new Document("uuid", uuid.toString()), Updates.set("name", name));
    }

    /**
     * Get the name of the enclave
     *
     * @return String
     */
    public String getName() {
        return enclave.getString("name");
    }

    /**
     * Get the formatted name of the enclave
     *
     * @return String
     */
    public String getDisplayName() {
        return ChatColor.of(getColor()) + getName();
    }


    /**
     * Set the color of the enclave tag (overwrite)
     */
    public void setColor(String color) {
        collection.findOneAndUpdate(new Document("uuid", uuid.toString()), Updates.set("color", color));
    }

    /**
     * Get the hex color of the enclave
     *
     * @return String
     */
    public String getColor() {
        Document enclaveDoc = collection.find(Filters.eq("uuid", uuid.toString())).first();
        return Objects.requireNonNull(enclaveDoc).getString("color");
    }

    public boolean toggleTag(EnclaveTag tag) {
        if(!enclave.getList("tags", String.class).contains(tag.toString())){
            collection.findOneAndUpdate(new Document("uuid", uuid.toString()), Updates.addToSet("tags", tag.toString()));
            return true;
        }else{
            collection.findOneAndUpdate(new Document("uuid", uuid.toString()), Updates.pull("tags", tag.toString()));
            return false;
        }
    }

    public List<String> getTags() {
        return enclave.getList("tags", String.class);
    }

    public boolean checkTag(EnclaveTag tag) {
        return enclave.getList("tags", String.class).contains(tag.toString());
    }

}
