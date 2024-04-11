package org.acme.security.jwt;

import com.mongodb.client.MongoDatabase;

import jakarta.ws.rs.core.Response;

public class PostService {

    public static Response getAllPosts(){
        MongoDatabase database = MongoUtil.getDB();
        PostDAO postDao = new PostDAO(database);
        return Response.ok(postDao.listPosts()).build();
    }

    public static Response postNewPost (String username, String post) {
        MongoDatabase database = MongoUtil.getDB();
        PostDAO postDao = new PostDAO(database);
        postDao.addPost(username, post);
        return Response.ok(postDao.listPosts()).build();
    }

    public Response deletePost (String post) {
        MongoDatabase database = MongoUtil.getDB();
        PostDAO postDao = new PostDAO(database);
        return Response.ok(postDao.deletePost(post)).build();
    }
}
