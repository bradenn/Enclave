package com.bradenn.dev.enclave.models;

import com.bradenn.dev.enclave.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerModel {

    private final UUID playerUUID;
    private final MongoCollection<Document> collection = Database.getCollection("players");
    private final Document player;

    /**
     * Initialize the playerModel, if the player is new, create a new doc.
     *
     * @param playerUUID Player's Microsoft-assigned UUID
     */
    public PlayerModel(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.player = getDocument();

        if (this.player == null) {
            List<String> invites = new ArrayList<>();
            Document newPlayerDoc = new Document("uuid", playerUUID.toString())
                    .append("enclave", null)
                    .append("invites", invites)
                    .append("date", new Date());
            this.collection.insertOne(newPlayerDoc);
        }
    }

    /**
     * Get the query document for the current player.
     *
     * @return Document Update document
     */
    private Document queryDocument() {
        return new Document("uuid", playerUUID.toString());
    }

    /**
     * Get the query document for the current player.
     *
     * @return Document Update document
     */
    private Document getDocument() {
        return this.collection.find(queryDocument()).first();
    }

    /**
     * Execute bson on the current region.
     *
     * @param bson Update document
     */
    private void updatePlayer(Bson bson) {
        this.collection.findOneAndUpdate(queryDocument(), bson);
    }

    /**
     * Change the player's enclave. This will overwrite.
     * Make sure the player is not the owner of another enclave before using this method.
     *
     * @param enclaveUUID The UUID of the new enclave.
     */
    public void setEnclave(UUID enclaveUUID) {
        updatePlayer(Updates.set("enclave", enclaveUUID.toString()));
    }

    /**
     * Get the enclave the player is a member of, if any.
     *
     * @return EnclaveModel returns null if there is no enclave.
     */
    public EnclaveModel getEnclave() {
        String uuid = player.getString("enclave");
        if (uuid != null) {
            return new EnclaveModel(UUID.fromString(uuid));
        } else {
            return null;
        }
    }

    /**
     * Clear the player's current enclave. This will override everything.
     * Ensure the player is removed from the enclave before calling this function.
     */
    public void clearEnclave() {
        updatePlayer(Updates.set("enclave", null));
    }

    /**
     * Check if player is a member of an Enclave
     *
     * @return boolean
     */
    public boolean hasEnclave() {
        return getEnclave() != null;
    }

    /**
     * Invite the player to join an enclave. This will overwrite any current invites.
     */
    public void addInvite(UUID enclaveUUID) {
        Document doc = new Document("enclave", enclaveUUID.toString()).append("date", new Date());
        updatePlayer(Updates.set("invite", doc));
    }

    /**
     * Get all invites sent to the player.
     */
    public UUID getInvite() {
        Document invite = player.get("invite", Document.class);
        if (invite == null) {
            return null;
        }
        Date inviteSent = invite.getDate("date");
        if (new Date().getTime() - inviteSent.getTime() <= TimeUnit.SECONDS.toMillis(2000)) {
            String enclaveString = invite.getString("enclave");
            return UUID.fromString(enclaveString);
        } else {
            return null;
        }
    }

    /**
     * Get the player's UUID
     *
     * @return UUID
     */
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    /**
     * The time since the region was claimed.
     */
    public String originDate() {
        return this.player.getDate("date").toString();
    }

}
