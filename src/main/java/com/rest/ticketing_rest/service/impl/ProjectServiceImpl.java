package com.rest.ticketing_rest.service.impl;

import com.rest.ticketing_rest.dto.ProjectDTO;
import com.rest.ticketing_rest.dto.UserDTO;
import com.rest.ticketing_rest.entity.Project;
import com.rest.ticketing_rest.entity.User;
import com.rest.ticketing_rest.enums.Status;
import com.rest.ticketing_rest.mapper.MapperUtil;
import com.rest.ticketing_rest.repository.ProjectRepository;
import com.rest.ticketing_rest.service.KeycloakService;
import com.rest.ticketing_rest.service.ProjectService;
import com.rest.ticketing_rest.service.TaskService;
import com.rest.ticketing_rest.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;
    private final TaskService taskService;
    private final KeycloakService keycloakService;

    public ProjectServiceImpl(ProjectRepository projectRepository, MapperUtil mapperUtil, UserService userService, TaskService taskService, KeycloakService keycloakService) {
        this.projectRepository = projectRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
        this.taskService = taskService;
        this.keycloakService = keycloakService;
    }


    @Override
    public ProjectDTO getByProjectCode(String projectCode) {

        Project project = projectRepository.findByProjectCode(projectCode);

        return mapperUtil.convert(project,new ProjectDTO());
    }

    @Override
    public List<ProjectDTO> listAllProjects() {

        List<Project> list = projectRepository.findAll(Sort.by("projectCode"));

        return list.stream().map(project -> mapperUtil.convert(project, new ProjectDTO())).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO project) {

        project.setProjectStatus(Status.OPEN);
        project.setAssignedManager(userService.findByUserName(project.getAssignedManager().getUserName()));
        Project convertedProject = mapperUtil.convert(project,new Project());
        projectRepository.save(convertedProject);

    }

    @Override
    public void update(String projectCode, ProjectDTO project) {

        Project foundProject = projectRepository.findByProjectCode(projectCode);

        Project convertedProject = mapperUtil.convert(project, new Project());

        convertedProject.setId(foundProject.getId());

        convertedProject.setProjectStatus(foundProject.getProjectStatus());
        convertedProject.setProjectCode(projectCode);
        User assignedManager= mapperUtil.convert(userService.findByUserName(project.getAssignedManager().getUserName()),new User());
        convertedProject.setAssignedManager(assignedManager);

        projectRepository.save(convertedProject);

    }

    @Override
    public void delete(String projectCode) {

        Project project = projectRepository.findByProjectCode(projectCode);

        project.setIsDeleted(true);
        project.setProjectCode(project.getProjectCode() + "-" + project.getId()); // SP00-1

        projectRepository.save(project);

        taskService.deleteByProject(mapperUtil.convert(project,new ProjectDTO()));
    }

    @Override
    public void complete(String projectCode) {

        Project project = projectRepository.findByProjectCode(projectCode);

        project.setProjectStatus(Status.COMPLETE);

        projectRepository.save(project);

        taskService.completeByProject(mapperUtil.convert(project,new ProjectDTO()));

    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        UserDTO currentUser = userService.findByUserName(keycloakService.getLoggedInUsername());

        User user = mapperUtil.convert(currentUser,new User());

        List<Project> list = projectRepository.findAllByAssignedManager(user);

        return list.stream().map(project -> {

            ProjectDTO dto = mapperUtil.convert(project,new ProjectDTO());

            dto.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            dto.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));

            return dto;

        }).collect(Collectors.toList());

    }

    @Override
    public List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager) {

        List<Project> projects = projectRepository.findAllByProjectStatusIsNotAndAssignedManager(Status.COMPLETE, mapperUtil.convert(assignedManager, new User()));

        return projects.stream().map(project -> mapperUtil.convert(project, new ProjectDTO())).collect(Collectors.toList());
    }
}
