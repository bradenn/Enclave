package com.bradenn.dev.enclave.models;

import com.bradenn.dev.enclave.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.md_5.bungee.api.ChatColor;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class EnclaveModel {

    private final UUID uuid;
    private final Document enclave;

    private final MongoCollection<Document> collection = Database.getCollection("enclaves");

    /**
     * Get an enclave's database document from it's UUID.
     *
     * @param uuid Enclave UUID
     */
    public EnclaveModel(UUID uuid) {
        this.uuid = uuid;
        this.enclave = getDocument();
    }

    /**
     * Create and insert a new enclave into the database.
     *
     * @param playerUUID Owner UUID
     * @param name       Enclave Name
     */
    public EnclaveModel(UUID playerUUID, String name) {
        // Generate a new UUID for the enclave.
        UUID enclaveUUID = UUID.randomUUID();

        // Initialize a list of members, including the owner.
        List<String> members = new ArrayList<>();
        members.add(playerUUID.toString());
        // Get the provided Player to own then Enclave, then update their record.
        PlayerModel pm = new PlayerModel(playerUUID);
        pm.setEnclave(enclaveUUID);

        // Generate the BSON Document for MongoDB.
        Document enclaveDoc = new Document()
                .append("uuid", enclaveUUID.toString())
                .append("name", name)
                .append("color", "#CCCCCC")
                .append("tags", new BsonArray())
                .append("owner", playerUUID.toString())
                .append("members", members);
        // Insert the document into the database.
        collection.insertOne(enclaveDoc);

        // Initialize the current class with the new UUID.
        this.uuid = enclaveUUID;
        this.enclave = getDocument();
    }

    /**
     * Get the query document for the current enclave.
     * @return Document Update document
     */
    private Document queryDocument() {
        // The query is represented with a key-value pair uuid:<UUID>
        return new Document("uuid", uuid.toString());
    }

    /**
     * Execute bson on the current enclave.
     * @param bson Update document
     */
    private void updateEnclave(Bson bson){
        this.collection.findOneAndUpdate(queryDocument(), bson);
    }

    /**
     * Get the enclave's document from MongoDB.
     *
     * @return Document Enclave Document
     */
    private Document getDocument() {
        // Return the first document with a matching UUID.
        return this.collection.find(queryDocument()).first();
    }

    /**
     * Check if the enclave instance exists in the database.
     *
     * @return boolean UUID is Valid
     */
    public boolean isValid() {
        return getDocument() != null;
    }

    /**
     * Determine if a player's UUID is associated with the ownership of the enclave.
     *
     * @return boolean
     */
    public boolean isOwner(UUID playerUUID) {
        return enclave.getString("owner").equalsIgnoreCase(playerUUID.toString());
    }

    /**
     * Cascade delete an enclave and it's entire influence.
     */
    public void disbandEnclave() {
        // Define the document to find regions and players belonging to the enclave.
        Bson foreignKey = Filters.eq("enclave", uuid.toString());

        // Get the region collection from the database.
        MongoCollection<Document> regions = Database.getCollection("regions");
        // Find all regions claimed or otherwise affiliated with this enclave.
        FindIterable<Document> regionDocs = regions.find(foreignKey);
        // Remove each of the affiliated regions from the database.
        regionDocs.forEach((Consumer<? super Document>) regions::findOneAndDelete);

        // Get the player collection from the database.
        MongoCollection<Document> players = Database.getCollection("players");
        // Find all players belonging to the enclave.
        FindIterable<Document> playerDocs = players.find(foreignKey);
        // Set each player's enclave to null, effectively removing them from the enclave.
        playerDocs.forEach((Consumer<? super Document>) (Document doc) ->
                players.findOneAndUpdate(doc, Updates.set("enclave", null)));

        // Remove the enclave document from the database.
        this.collection.findOneAndDelete(queryDocument());
    }

    /**
     * Get the enclave's UUID
     * @return UUID
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Set the owner of the enclave.
     * @param playerUUID Player UUID
     */
    public void setOwner(UUID playerUUID) {
        updateEnclave(Updates.set("owner", playerUUID.toString()));
    }

    /**
     * Get the UUID of the player whom owns the enclave
     * @return UUID
     */
    public UUID getOwner() {
        return UUID.fromString(enclave.getString("owner"));
    }

    /**
     * Determine whether the enclave has a member with a UUID.
     * @return UUID
     */
    public boolean hasMember(UUID uuid) {
        return enclave.getList("members", String.class).contains(uuid.toString());
    }

    /**
     * Add a member to the enclave. You must also assign the player's enclave from the Player Model.
     */
    public void addMember(UUID uuid) {
        updateEnclave(Updates.addToSet("members", uuid.toString()));
    }

    /**
     * Add a member to the enclave. You must also assign the player's enclave from the Player Model.
     */
    public void removeMember(UUID uuid) {
        updateEnclave(Updates.pull("members", uuid.toString()));
    }

    /**
     * Set the name of the enclave (overwrite)
     */
    public void setName(String name) {
        updateEnclave(Updates.set("name", name));
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
        updateEnclave(Updates.set("color", color));
    }

    /**
     * Get the hex color of the enclave
     *
     * @return String
     */
    public String getColor() {
        return enclave.getString("color");
    }

    /**
     * Set the home of the enclave
     */
    public void setHome(Location location) {
        updateEnclave(Updates.set("home", location.serialize()));
    }


    /**
     * Get the home location of the enclave.
     * @return Location Enclave Home, returns null if no home is set.
     */
    @SuppressWarnings("unchecked")
    public Location getHome() {
        // Get the home location from the enclave document.
        Map<String, Object> location = (Map<String, Object>) enclave.get("home");
        // Return the deserialized location, or null if there is no home set.
        return (location != null)?Location.deserialize(location):null;
    }

    /**
     * Get all tag attributes currently disabled in this enclave.
     * @return List<String> List of disabled properties
     */
    public List<String> getTags() {
        return enclave.getList("tags", String.class);
    }

    /**
     * Check if a tag attribute has been disabled in this enclave.
     * @return boolean If true, the tag attribute is disabled.
     */
    public boolean checkTag(EnclaveTag tag) {
        String serializedTag = tag.toString();
        return getTags().contains(serializedTag);
    }

    /**
     * Toggle the given tag on or off depending on it's previous state.
     * All tags in the list are considered "disabled", all are "enabled" by default.
     * @param tag The EnclaveTag to toggle
     * @return boolean
     */
    public boolean toggleTag(EnclaveTag tag) {
        // Get the current tag list from the enclave document.
        List<String> tagList = getTags();
        String serializedTag = tag.toString();

        // Toggle the tag based on it's current state.
        if (tagList.contains(serializedTag)) {
            // Remove the reference of the provided tag from the enclave document's tag array.
            updateEnclave(Updates.pull("tags", serializedTag));
            // Return false to signify that the tag has been enabled.
            return false;
        } else {
            // Add the provided tag to the enclave document's tag array.
            updateEnclave(Updates.addToSet("tags", serializedTag));
            // Return true to signify that the tag has been disabled.
            return true;
        }
    }
}
