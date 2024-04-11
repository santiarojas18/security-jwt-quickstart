package org.acme.security.jwt;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Optional;

@Path("/posts")
@RequestScoped
public class PostService {
    @Inject
    JsonWebToken jwt;

    @Inject
    @Claim(standard = Claims.iss)
    String iss;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getAllPosts(@Context SecurityContext ctx){
        System.out.println(getResponseString(ctx) + "iss: " + iss);
        return Response.ok(Main.postDao.listPosts()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response postNewPost (@QueryParam("username") String username, String post, @Context SecurityContext ctx) {
        //System.out.println(hasJwt());
        Main.postDao.addPost(username, post);
        return Response.ok(Main.postDao.listPosts()).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deletePost (String post) {
        return Response.ok(Main.postDao.deletePost(post)).build();
    }

    private String getResponseString(SecurityContext ctx) {
        String name;
        if (ctx.getUserPrincipal() == null) {
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName();
        }
        return String.format("hello + %s,"
                        + " isHttps: %s,"
                        + " authScheme: %s,"
                        + " hasJWT: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
    }

    private boolean hasJwt() {
        //System.out.println(jwt.getClaim("iss").toString());
        return jwt.getClaimNames() != null;
    }
}
