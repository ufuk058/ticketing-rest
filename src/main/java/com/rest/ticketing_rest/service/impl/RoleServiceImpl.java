package com.rest.ticketing_rest.service.impl;

import com.rest.ticketing_rest.dto.RoleDTO;
import com.rest.ticketing_rest.entity.Role;
import com.rest.ticketing_rest.mapper.MapperUtil;
import com.rest.ticketing_rest.repository.RoleRepository;
import com.rest.ticketing_rest.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    //    private final RoleMapper roleMapper;
    private final MapperUtil mapperUtil;

    public RoleServiceImpl(RoleRepository roleRepository, MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<RoleDTO> listAllRoles() {

        // ask repository layer to give us a list of roles from database

        List<Role> roleList = roleRepository.findAll();

        return roleList.stream().map(role -> mapperUtil.convert(role,new RoleDTO())).collect(Collectors.toList());
    }

    @Override
    public RoleDTO findById(Long id) {

        Role role = roleRepository.findById(id).get();

        return mapperUtil.convert(role,new RoleDTO());
    }
}
