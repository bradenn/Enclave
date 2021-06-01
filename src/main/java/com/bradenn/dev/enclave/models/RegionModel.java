package com.bradenn.dev.enclave.models;

import com.bradenn.dev.enclave.persistent.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.UUID;

public class RegionModel {

    private final World world;
    private final Chunk chunk;
    private final Document document;

    MongoCollection<Document> collection = Database.getCollection("regions");

    public RegionModel(Chunk chunk, World world) {
        this.chunk = chunk;
        this.world = world;
        this.document = getDocument();
    }

    public RegionModel(Chunk chunk) {
        this.chunk = chunk;
        this.world = chunk.getWorld();
        this.document = getDocument();
    }

    private Document getDocument() {
        Bson query = Filters.and(
                Filters.eq("x", chunk.getX()),
                Filters.eq("z", chunk.getZ()),
                Filters.eq("world", world.getName()));
        return collection.find(query).first();
    }

    public boolean isClaimed() {
        if (document != null) {
            return !document.isEmpty();
        } else {
            return false;
        }
    }

    public EnclaveModel getEnclave() {
        if (isClaimed()) {
            return new EnclaveModel(UUID.fromString(document.getString("enclave")));
        } else {
            return null;
        }
    }

    public void claimChunk(UUID enclave) {
        Document chunkDoc = new Document("x", chunk.getX())
                .append("z", chunk.getZ())
                .append("world", world.getName())
                .append("enclave", enclave.toString());
        collection.insertOne(chunkDoc);
    }

    public void unclaimChunk(UUID enclave) {
        Document chunkDoc = new Document("x", chunk.getX())
                .append("z", chunk.getZ())
                .append("world", world.getName())
                .append("enclave", enclave.toString());
        collection.findOneAndDelete(chunkDoc);
    }


}
