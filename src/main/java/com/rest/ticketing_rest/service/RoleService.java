package com.rest.ticketing_rest.service;

import com.rest.ticketing_rest.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> listAllRoles();
    RoleDTO findById(Long id);
}
