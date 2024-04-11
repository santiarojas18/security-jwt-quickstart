package org.acme.security.jwt;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
public class PostDAO {
    private final MongoCollection<Document> postsCollection;

    public PostDAO(MongoDatabase database) {
        postsCollection = database.getCollection("posts");
    }

    public String addPost(String username, String post) {
        LocalDateTime currentDate = LocalDateTime.now();
        Document newPost = new Document("username", username)
                .append("post", post)
                .append("date", currentDate.toString());
        postsCollection.insertOne(newPost);
        return newPost.toJson();
    }

    public ArrayList<String> listPosts() {
        FindIterable<Document> posts = postsCollection.find();
        ArrayList<String> temporalList = new ArrayList<>();
        for (Document post : posts) {
            temporalList.add(post.toJson());
        }
        if (temporalList.size() > 10) {
            ArrayList<String> finalList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                finalList.add(temporalList.get(temporalList.size() - 10 + i));
            }
            return finalList;
        } else {
            return temporalList;
        }
    }

    public DeleteResult deletePost(String post) {
        return postsCollection.deleteOne(eq("post", post));
    }
}
