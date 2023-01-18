package com.vova7865.konkarusel.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RideRepository {
    private final MongoCollection<Ride> collection;

    public RideRepository(MongoDatabase database) {
        this.collection = database.getCollection("rides", Ride.class);
    }

    public List<Ride> getRides() {
        List<Ride> res = new ArrayList<>();
        collection.find().into(res);
        return res;
    }

    public List<Ride> getRidesForPlayer(UUID playerId) {
        List<Ride> res = new ArrayList<>();
        collection.find(Filters.eq("playerUUID", playerId)).into(res);
        return res;
    }

    public void addRide(Ride ride) {
        collection.insertOne(ride);
    }
}
