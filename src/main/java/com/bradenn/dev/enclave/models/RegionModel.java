package com.bradenn.dev.enclave.models;

import com.bradenn.dev.enclave.persistent.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RegionModel {

    private World world;
    private Chunk chunk;

    MongoDatabase db = Database.getDatabase();
    MongoCollection<Document> collection = db.getCollection("regions");

    public RegionModel(Chunk chunk, World world) {
        this.chunk = chunk;
        this.world = world;
    }

    public boolean isClaimed() {
        Bson query = Filters.and(
                Filters.eq("x", chunk.getX()),
                Filters.eq("z", chunk.getZ()),
                Filters.eq("world", world.getName()));
        Document regionDoc = collection.find(query).first();

        return regionDoc != null;
    }

    public EnclaveModel getEnclave() {
        if (isClaimed()) {
            Bson query = Filters.and(
                    Filters.eq("x", chunk.getX()),
                    Filters.eq("z", chunk.getZ()),
                    Filters.eq("world", world.getName()));
            Document regionDoc = collection.find(query).first();

            assert regionDoc != null;
            return new EnclaveModel(UUID.fromString(regionDoc.getString("enclave")));

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
