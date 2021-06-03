package com.bradenn.dev.enclave.models;

import com.bradenn.dev.enclave.persistent.Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerModel {

    private UUID playerUUID;
    MongoDatabase db = Database.getDatabase();
    MongoCollection<Document> collection = db.getCollection("players");

    /**
     * Access the player model
     * @param uuid
     */
    public PlayerModel(UUID uuid) {
        Document playerDoc = collection.find(Filters.eq("uuid", uuid.toString())).first();
        if (playerDoc == null) {
            List<String> invites = new ArrayList<>();
            Document newPlayerDoc = new Document("uuid", uuid.toString())
                    .append("enclave", null)
                    .append("invites", invites)
                    .append("username", Bukkit.getPlayer(uuid).getName());
            collection.insertOne(newPlayerDoc);
        } else {
            playerUUID = uuid;
        }
    }

    /**
     * Change the player's guild (overwrite)
     * @param uuid
     */
    public void setEnclave(UUID uuid) {
        collection.findOneAndUpdate(new Document("uuid", playerUUID.toString()), Updates.set("enclave", uuid.toString()));
    }

    /**
     * Get the user's enclave
     * @return EnclaveModel
     */
    public EnclaveModel getEnclave() {
        Document playerDoc = collection.find(Filters.eq("uuid", playerUUID.toString())).first();
        assert playerDoc != null;
        String uuid = playerDoc.getString("enclave");
        if(uuid != null){
            return new EnclaveModel(UUID.fromString(uuid));
        }else{
            return null;
        }
    }

    /**
     * Check if player is a member of an Enclave
     * @return boolean
     */
    public boolean hasEnclave() {
        return getEnclave() != null;
    }

    public void addInvite(UUID enclaveUUID) {
        collection.findOneAndUpdate(new Document("uuid", playerUUID), Updates.addToSet("invites", enclaveUUID.toString()));
    }

    /**
     * Get the player's username
     * @return String
     */
    public String getUsername() {
        return getOnlinePlayer().getName();
    }

    /**
     * Get the player's UUID
     * @return UUID
     */
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Get the online player
     * @return Player
     */
    public Player getOnlinePlayer() {
        return Bukkit.getPlayer(playerUUID);
    }


}
