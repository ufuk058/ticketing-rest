package com.rest.ticketing_rest.service.impl;

import com.rest.ticketing_rest.config.KeycloakProperties;
import com.rest.ticketing_rest.dto.UserDTO;
//import com.rest.ticketing_rest.exception.UserNotFoundException;
import com.rest.ticketing_rest.exception.UserNotFoundException;
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

    @Override
    public void userUpdate(UserDTO userDTO) {
        try(Keycloak keycloak= getKeycloakInstance()){
            RealmResource realmResource= keycloak.realm(keycloakProperties.getRealm());
            UsersResource userResource= realmResource.users();
            List<UserRepresentation> userRepresentations= userResource.search(userDTO.getUserName());

            if(userRepresentations.isEmpty()){
                throw new UserNotFoundException("User does not exist!");
            }
            UserRepresentation keyCloakUser= userRepresentations.get(0);
            updateRoles(realmResource,keyCloakUser.getId(),userDTO.getRole().getDescription());
            keyCloakUser.setFirstName(userDTO.getFirstName());
            keyCloakUser.setLastName(userDTO.getLastName());

            if(userDTO.getPassWord() != null && !userDTO.getPassWord().isEmpty()){

                updatePassword(userResource,keyCloakUser.getId(),userDTO.getPassWord());
            }

            userResource.get(keyCloakUser.getId()).update(keyCloakUser);
        }
    }

    private void updateRoles(RealmResource realmResource, String userId, String role){
        ClientRepresentation appClient= realmResource.clients()
                .findByClientId(keycloakProperties.getClientId()).get(0);

        String clientId= appClient.getId();

        List<RoleRepresentation> existingRoles= realmResource.users().get(userId)
                .roles().clientLevel(clientId).listEffective();
        existingRoles.forEach(existingRole ->
                realmResource.users().get(userId)
                        .roles().clientLevel(clientId).remove(List.of(existingRole)));

        RoleRepresentation userClientRole= realmResource.clients().get(clientId)
                .roles().get(role).toRepresentation();

        realmResource.users().get(userId).roles().clientLevel(clientId)
                .add(List.of(userClientRole));
    }

    private void updatePassword(UsersResource usersResource, String userId, String newPassword){
        CredentialRepresentation credential= new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        credential.setValue(newPassword);
        usersResource.get(userId).resetPassword(credential);

    }

    @Override
    public void userDelete(String username) {

        try(Keycloak keycloak = getKeycloakInstance()){
            RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
            UsersResource userResource= realmResource.users();

            List<UserRepresentation> userRepresentations= userResource.search(username);

            String uid= userRepresentations.get(0).getId();
            userResource.delete(uid);
        }
    }
}
