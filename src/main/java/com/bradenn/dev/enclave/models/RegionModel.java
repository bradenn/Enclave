package com.bradenn.dev.enclave.models;

import com.bradenn.dev.enclave.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Date;
import java.util.UUID;

public class RegionModel {

    private final World world;
    private final Chunk chunk;
    private final Document region;

    private final MongoCollection<Document> collection = Database.getCollection("regions");

    /**
     * @param chunk Bukkit Chunk
     * @param world Bukkit World
     * @deprecated World param is redundant, as a chunk always contains world data.
     */
    public RegionModel(Chunk chunk, World world) {
        this.chunk = chunk;
        this.world = world;
        this.region = getDocument();
    }

    /**
     * Get the region data for a given chunk.
     *
     * @param chunk Bukkit Chunk
     */
    public RegionModel(Chunk chunk) {
        this.chunk = chunk;
        this.world = chunk.getWorld();
        this.region = getDocument();
    }

    /**
     * Get the query document for the current region.
     *
     * @return Bson Update document
     */
    private Bson queryDocument() {
        return Filters.and(
                Filters.eq("x", chunk.getX()),
                Filters.eq("z", chunk.getZ()),
                Filters.eq("world", world.getName()));
    }

    /**
     * Get the query document for the current region.
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
    private void updateRegion(Bson bson) {
        this.collection.findOneAndUpdate(queryDocument(), bson);
    }

    /**
     * Determine whether the current chunk is claimed or not.
     */
    public boolean isClaimed() {
        if (region != null) {
            return !region.isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Determine whether a region is claimed by a provided enclave.
     * This is essentially a macro of isClaimed.
     *
     * @param enclaveUUID The target enclave UUID.
     * @return boolean returns true if the provided enclave owns the region.
     */
    public boolean isEnclave(UUID enclaveUUID) {
        if (isClaimed()) {
            return getEnclave().getUUID().equals(enclaveUUID);
        }
        return false;
    }

    /**
     * Get the enclave owning the current region, if any.
     *
     * @return EnclaveModel returns null if the chunk is not claimed.
     */
    public EnclaveModel getEnclave() {
        if (isClaimed()) {
            return new EnclaveModel(UUID.fromString(region.getString("enclave")));
        } else {
            return null;
        }
    }

    /**
     * Claim this region for a given enclave.
     * This method will overwrite previous ownership.
     *
     * @param enclaveUUID UUID of the target enclave.
     */
    public void claimChunk(UUID enclaveUUID) {
        Document chunkDoc = new Document("x", chunk.getX())
                .append("z", chunk.getZ())
                .append("world", world.getName())
                .append("enclave", enclaveUUID.toString())
                .append("date", new Date());
        this.collection.insertOne(chunkDoc);
    }

    /**
     * Unclaim this region for a given enclave.
     */
    public void unclaimChunk() {
        this.collection.findOneAndDelete(region);
    }

    /**
     * The time since the region was claimed.
     */
    public String originDate() {
        return this.region.getDate("date").toString();
    }


}
