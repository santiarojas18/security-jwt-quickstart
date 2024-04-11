package org.acme.security.jwt;

import com.mongodb.client.MongoDatabase;
import io.quarkus.runtime.Quarkus;

public class Main {
    public static MongoDatabase database = MongoUtil.getDB();
    public static PostDAO postDao = new PostDAO(database);

    public static void main(String[] args) {
        // Iniciar la aplicación Quarkus
        Quarkus.run(args);
        PostService.getAllPosts();
    }
}
