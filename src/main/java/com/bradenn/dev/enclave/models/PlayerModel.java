package com.bradenn.dev.enclave.models;

import com.bradenn.dev.enclave.persistent.Database;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class PlayerModel {

    private UUID playerUUID;
    MongoDatabase db = Database.getDatabase();
    MongoCollection<Document> collection = db.getCollection("players");

    public PlayerModel(UUID uuid) {
        Document playerDoc = collection.find(Filters.eq("uuid", uuid.toString())).first();
        if (playerDoc == null) {
            Document newPlayerDoc = new Document("uuid", uuid.toString()).append("enclave", null).append("username", Bukkit.getPlayer(uuid).getName());
            collection.insertOne(newPlayerDoc);
        } else {
            playerUUID = uuid;
        }
    }

    public void setEnclave(UUID uuid) {
        collection.findOneAndUpdate(new Document("uuid", playerUUID.toString()), Updates.set("enclave", uuid.toString()));
    }

    public EnclaveModel getEnclave() {
        Document playerDoc = collection.find(Filters.eq("uuid", playerUUID.toString())).first();
        if(playerDoc.getString("enclave") != null) {
            return new EnclaveModel(UUID.fromString(playerDoc.getString("enclave")));
        }else{
            return null;
        }
    }

    public String getUsername() {
        return getOnlinePlayer().getName();
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Player getOnlinePlayer() {
        return Bukkit.getPlayer(playerUUID);
    }


}
