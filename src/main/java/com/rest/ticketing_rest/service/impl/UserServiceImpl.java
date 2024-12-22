package com.rest.ticketing_rest.service.impl;

import com.rest.ticketing_rest.dto.ProjectDTO;
import com.rest.ticketing_rest.dto.TaskDTO;
import com.rest.ticketing_rest.dto.UserDTO;
import com.rest.ticketing_rest.entity.User;
import com.rest.ticketing_rest.mapper.MapperUtil;
import com.rest.ticketing_rest.repository.UserRepository;
import com.rest.ticketing_rest.service.ProjectService;
import com.rest.ticketing_rest.service.TaskService;
import com.rest.ticketing_rest.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {

        List<User> userList = userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);

        return userList.stream().map(user -> mapperUtil.convert(user,new UserDTO())).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {

        User user = userRepository.findByUserNameAndIsDeleted(username, false);

        return mapperUtil.convert(user,new UserDTO());
    }

    @Override
    public void save(UserDTO user) {

        userRepository.save(mapperUtil.convert(user,new User()));
    }

    @Override
    public void update(String username,UserDTO user) {

        User foundUser = userRepository.findByUserNameAndIsDeleted(username, false);

        User updatedUser = mapperUtil.convert(user,new User());

        updatedUser.setId(foundUser.getId());
        updatedUser.setUserName(username);

        userRepository.save(updatedUser);
    }

    @Override
    public void delete(String username) {

        User user = userRepository.findByUserNameAndIsDeleted(username, false);


        if (checkIfUserCanBeDeleted(mapperUtil.convert(user,new UserDTO()))){

            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" + user.getId());

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
