package com.rest.ticketing_rest.service;

import com.rest.ticketing_rest.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username);
    void save(UserDTO user);
    void update(UserDTO user);
    void delete(String username);

    List<UserDTO> listAllByRole(String role);
}
