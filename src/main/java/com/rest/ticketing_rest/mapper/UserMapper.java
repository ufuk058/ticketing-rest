package com.rest.ticketing_rest.mapper;

import com.rest.ticketing_rest.dto.UserDTO;
import com.rest.ticketing_rest.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO convertToDto(User entity){
        return modelMapper.map(entity,UserDTO.class);
    }

    public User convertToEntity(UserDTO dto){
        return modelMapper.map(dto,User.class);
    }
}
