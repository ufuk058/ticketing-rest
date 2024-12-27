package com.rest.ticketing_rest.service.impl;

import com.rest.ticketing_rest.dto.ProjectDTO;
import com.rest.ticketing_rest.dto.RoleDTO;
import com.rest.ticketing_rest.dto.TaskDTO;
import com.rest.ticketing_rest.dto.UserDTO;
import com.rest.ticketing_rest.entity.Role;
import com.rest.ticketing_rest.entity.User;
import com.rest.ticketing_rest.exception.UserAlreadyExistException;
import com.rest.ticketing_rest.exception.UserNotFoundException;
import com.rest.ticketing_rest.mapper.MapperUtil;
import com.rest.ticketing_rest.repository.UserRepository;
import com.rest.ticketing_rest.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final RoleService roleService;
    private final KeycloakService keycloakService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, @Lazy ProjectService projectService, @Lazy TaskService taskService, RoleService roleService, KeycloakService keycloakService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.projectService = projectService;
        this.taskService = taskService;
        this.roleService = roleService;
        this.keycloakService = keycloakService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> listAllUsers() {

        List<User> userList = userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);

        return userList.stream().map(user -> mapperUtil.convert(user,new UserDTO())).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {

        User user = userRepository.findByUserNameAndIsDeleted(username, false);

        if(user ==null) throw new UserNotFoundException("User not exist");
        return mapperUtil.convert(user,new UserDTO());
    }

    @Override
    public void save(UserDTO user) {

        if(userRepository.findByUserNameAndIsDeleted(user.getUserName(),false) !=null){
            throw new UserAlreadyExistException("User already exist");
        }
        user.setRole(roleService.findByDescription(user.getRole().getDescription()));
        keycloakService.userCreate(user);
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        userRepository.save(mapperUtil.convert(user,new User()));
    }

    @Override
    public void update(String username,UserDTO user) {

        User foundUser = userRepository.findByUserNameAndIsDeleted(username, false);

        User updatedUser = mapperUtil.convert(user,new User());

        updatedUser.setId(foundUser.getId());
        updatedUser.setUserName(username);
        Role role= mapperUtil.convert(roleService.findByDescription(user.getRole().getDescription()),new Role());
        updatedUser.setRole(role);
        keycloakService.userUpdate(mapperUtil.convert(updatedUser,new UserDTO()));

        updatedUser.setPassWord(passwordEncoder.encode(user.getPassWord()));

        userRepository.save(updatedUser);
    }

    @Override
    public void delete(String username) {

        User user = userRepository.findByUserNameAndIsDeleted(username, false);


        if (checkIfUserCanBeDeleted(mapperUtil.convert(user,new UserDTO()))){

            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" + user.getId());

            keycloakService.userDelete(username);
            userRepository.save(user);
        }
    }

    private boolean checkIfUserCanBeDeleted(UserDTO user){

        switch (user.getRole().getDescription()){

            case "Manager":

                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(user);

                return projectDTOList.size() == 0;

            case "Employee":

                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(user);

                return taskDTOList.size() == 0;

            default:

                return true;

        }



    }



    @Override
    public List<UserDTO> listAllByRole(String role) {

        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role, false);

        return users.stream().map(user -> mapperUtil.convert(user,new UserDTO())).collect(Collectors.toList());
    }
}
