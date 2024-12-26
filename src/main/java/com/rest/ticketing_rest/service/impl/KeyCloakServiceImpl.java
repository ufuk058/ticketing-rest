package com.rest.ticketing_rest.service.impl;

import com.rest.ticketing_rest.config.KeycloakProperties;
import com.rest.ticketing_rest.dto.UserDTO;
//import com.rest.ticketing_rest.exception.UserNotFoundException;
import com.rest.ticketing_rest.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;

@Service
public class KeyCloakServiceImpl implements KeycloakService {

    private final KeycloakProperties keycloakProperties;

    public KeyCloakServiceImpl(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    @Override
    public String getLoggedInUsername() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Map<String,Object> attributes= ((JwtAuthenticationToken) authentication).getTokenAttributes();
        return (String)attributes.get("preferred_username");
    }

    @Override
    public void userCreate(UserDTO userDTO) {


        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        credential.setValue(userDTO.getPassWord());

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(userDTO.getUserName());
        keycloakUser.setFirstName(userDTO.getFirstName());
        keycloakUser.setLastName(userDTO.getLastName());
        keycloakUser.setEmail(userDTO.getUserName());
        keycloakUser.setCredentials(List.of(credential));
        keycloakUser.setEmailVerified(true);
        keycloakUser.setEnabled(true);

        try (Keycloak keycloak = getKeycloakInstance()) {

            RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());

            UsersResource usersResource = realmResource.users();

            // Create Keycloak user
            Response response = usersResource.create(keycloakUser);
            System.out.println(response.toString());
            if (response.getStatus() != 201) {
                String errorDetails = response.readEntity(String.class);
                throw new RuntimeException("Failed to create user. Status: " + response.getStatus() + ", Error: " + errorDetails);
            }

            String userId = getCreatedId(response);

            ClientRepresentation appClient = realmResource.clients()
                    .findByClientId(keycloakProperties.getClientId()).get(0);

            RoleRepresentation userClientRole = realmResource.clients().get(appClient.getId())
                    .roles().get(userDTO.getRole().getDescription()).toRepresentation();

            realmResource.users().get(userId).roles().clientLevel(appClient.getId())
                    .add(List.of(userClientRole));

        }
    }

    private Keycloak getKeycloakInstance() {

        return Keycloak.getInstance(
                keycloakProperties.getAuthServerUrl(),
                keycloakProperties.getMasterRealm(),
                keycloakProperties.getMasterUser(),
                keycloakProperties.getMasterUserPswd(),
                keycloakProperties.getMasterClient());

    }
}
