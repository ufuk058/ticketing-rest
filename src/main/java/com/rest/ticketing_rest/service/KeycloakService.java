package com.rest.ticketing_rest.service;

import com.rest.ticketing_rest.dto.UserDTO;

public interface KeycloakService {
    String getLoggedInUsername();
    void userCreate(UserDTO user);
    void userUpdate(UserDTO userDTO);
    void userDelete(String username);
}
