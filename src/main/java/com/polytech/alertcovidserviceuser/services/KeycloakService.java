package com.polytech.alertcovidserviceuser.services;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;


public class KeycloakService {


    private String serverUrl;

    private String realm;

    private String adminUsername;

    private String adminPassword;

    public KeycloakService(String serverUrl, String realm, String adminUsername, String adminPassword) {
        this.serverUrl = serverUrl;
        this.realm = realm;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    public String createAgentOnKeyclock(String firstname, String lastname, String email, String password) {

        try {
            String clientId = "admin-cli";

            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientId)
                    .username(adminUsername)
                        .password(adminPassword)
                    .build();

// Define user
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(email);
            user.setFirstName(firstname);
            user.setLastName(lastname);
            user.setEmail(email);

// Get realm
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource userRessource = realmResource.users();

            Response response = userRessource.create(user);

            System.out.println("response : " + response.getStatus());

            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            System.out.println("userId : " + userId);
// Define password credential
            CredentialRepresentation passwordCred = new CredentialRepresentation();
//
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(password);

// Set password credential
            userRessource.get(userId).resetPassword(passwordCred);

            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failure";
        }
    }
}
