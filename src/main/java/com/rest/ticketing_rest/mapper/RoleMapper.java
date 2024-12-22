package com.rest.ticketing_rest.mapper;

import com.rest.ticketing_rest.dto.RoleDTO;
import com.rest.ticketing_rest.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    private final ModelMapper modelMapper;

    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RoleDTO convertToDto(Role entity){
        return modelMapper.map(entity, RoleDTO.class);
    }

    public Role convertToEntity(RoleDTO dto){

        return  modelMapper.map(dto,Role.class);
    }
}
