package com.rest.ticketing_rest.entity;

import com.rest.ticketing_rest.service.KeycloakService;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BaseEntityListener {
    private final KeycloakService keycloakService;

    public BaseEntityListener(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PrePersist
    private void onPrePersist(BaseEntity baseEntity ){

        String username= keycloakService.getLoggedInUsername();

        if(username !=null && !username.equals("anonymous")){
            baseEntity.setInsertDateTime(LocalDateTime.now());
            baseEntity.setLastUpdateDateTime(LocalDateTime.now());
            baseEntity.setInsertUserUsername(username);
            baseEntity.setLastUpdateUserUsername(username);
        }
    }


    @PreUpdate
    private void onPreUpdate(BaseEntity baseEntity ){

        String username= keycloakService.getLoggedInUsername();

        if(username !=null && !username.equals("anonymous")){
            baseEntity.setLastUpdateDateTime(LocalDateTime.now());
            baseEntity.setLastUpdateUserUsername(username);
        }
    }
}
