package org.acme.security.jwt;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoUtil {
    private static final String CONNECTION_STRING = "mongodb+srv://santiar18:santi123@clusterada.y7s7pje.mongodb.net/twitterDB?retryWrites=true&w=majority&appName=ClusterAda";
    private static final String DATABASE_NAME = "twitterDB";

    public static MongoDatabase getDB() {
        MongoClient client = MongoClients.create(CONNECTION_STRING);
        return client.getDatabase(DATABASE_NAME);
    }
}
