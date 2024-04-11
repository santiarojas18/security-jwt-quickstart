package org.acme.security.jwt;

import com.amazonaws.http.SdkHttpMetadata;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import com.amazonaws.regions.Regions;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
@Path("/login")
@RequestScoped
public class CognitoAuthentication {
    private static final String USER_POOL_ID = "us-east-1_IospIx8R3";
    private static final String CLIENT_ID = "hoca69nl7pvs51dec41kmfill";

    @GET
    @Path("signin")
    public Response signin(@QueryParam("username") String username, @QueryParam("password") String password) {
        // Crea un cliente de Amazon Cognito Identity Provider
        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(Regions.US_EAST_1) // Reemplaza YOUR_REGION con la región que corresponde a tu User Pool
                .build();

        // Inicia el proceso de autenticación
        InitiateAuthRequest authRequest = new InitiateAuthRequest();
        authRequest.withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .withClientId(CLIENT_ID)
                .addAuthParametersEntry("USERNAME", username)
                .addAuthParametersEntry("PASSWORD", password);

        // Realiza la solicitud de autenticación
        try {
            InitiateAuthResult authResult = cognitoIdentityProvider.initiateAuth(authRequest);

            // Obtiene el token de acceso del resultado de autenticación
            AuthenticationResultType authenticationResult = authResult.getAuthenticationResult();
            String accessToken = authenticationResult.getAccessToken();
            String idToken = authenticationResult.getIdToken();
            String refreshToken = authenticationResult.getRefreshToken();

            // Maneja el token de acceso según sea necesario
            System.out.println("Token de acceso: " + accessToken);
            System.out.println("Token de identificación: " + idToken);
            System.out.println("Token de actualización: " + refreshToken);
            return Response.ok(accessToken).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).entity("Acceso denegado").build();
        }

    }
}