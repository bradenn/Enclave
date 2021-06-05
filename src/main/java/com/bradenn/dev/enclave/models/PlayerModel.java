package com.bradenn.dev.enclave.models;

import com.bradenn.dev.enclave.persistent.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerModel {

    private final UUID playerUUID;
    private final MongoCollection<Document> collection = Database.getCollection("players");
    private final Document player;

    /**
     * Initialize the playerModel, if the player is new, create a new doc.
     * @param playerUUID Player's Microsoft-assigned UUID
     */
    public PlayerModel(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.player = getDocument();

        if (this.player == null) {
            List<String> invites = new ArrayList<>();
            Document newPlayerDoc = new Document("uuid", playerUUID.toString())
                    .append("enclave", null)
                    .append("invites", invites);
            this.collection.insertOne(newPlayerDoc);
        }
    }

    /**
     * Get the query document for the current player.
     * @return Document Update document
     */
    private Document queryDocument() {
        return new Document("uuid", playerUUID.toString());
    }

    /**
     * Get the query document for the current player.
     * @return Document Update document
     */
    private Document getDocument() {
        return this.collection.find(queryDocument()).first();
    }

    /**
     * Execute bson on the current region.
     * @param bson Update document
     */
    private void updatePlayer(Bson bson) {
        this.collection.findOneAndUpdate(queryDocument(), bson);
    }

    /**
     * Change the player's enclave. This will overwrite.
     * Make sure the player is not the owner of another enclave before using this method.
     * @param enclaveUUID The UUID of the new enclave.
     */
    public void setEnclave(UUID enclaveUUID) {
        updatePlayer(Updates.set("enclave", enclaveUUID.toString()));
    }

    /**
     * Get the enclave the player is a member of, if any.
     * @return EnclaveModel returns null if there is no enclave.
     */
    public EnclaveModel getEnclave() {
        String uuid = player.getString("enclave");
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

    /**
     * Invite the player to join an enclave.
     */
    public void addInvite(UUID enclaveUUID) {
        updatePlayer(Updates.addToSet("invites", enclaveUUID.toString()));
    }

    /**
     * Get the player's UUID
     * @return UUID
     */
    public UUID getPlayerUUID() {
        return playerUUID;
    }

}
