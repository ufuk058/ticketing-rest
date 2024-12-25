package com.rest.ticketing_rest.service.impl;

import com.rest.ticketing_rest.service.KeycloakService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KeyCloakServiceImpl implements KeycloakService {
    @Override
    public String getLoggedInUsername() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Map<String,Object> attributes= ((JwtAuthenticationToken) authentication).getTokenAttributes();
        return (String)attributes.get("preferred_username");
    }
}
