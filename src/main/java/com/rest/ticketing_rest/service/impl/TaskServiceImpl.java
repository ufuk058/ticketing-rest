package com.rest.ticketing_rest.service.impl;

import com.rest.ticketing_rest.dto.ProjectDTO;
import com.rest.ticketing_rest.dto.TaskDTO;
import com.rest.ticketing_rest.dto.UserDTO;
import com.rest.ticketing_rest.entity.Project;
import com.rest.ticketing_rest.entity.Task;
import com.rest.ticketing_rest.entity.User;
import com.rest.ticketing_rest.enums.Status;
import com.rest.ticketing_rest.mapper.MapperUtil;
import com.rest.ticketing_rest.repository.TaskRepository;
import com.rest.ticketing_rest.service.KeycloakService;
import com.rest.ticketing_rest.service.ProjectService;
import com.rest.ticketing_rest.service.TaskService;
import com.rest.ticketing_rest.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {


    private final TaskRepository taskRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;
    private final ProjectService projectService;
    private final KeycloakService keycloakService;

    public TaskServiceImpl(TaskRepository taskRepository, MapperUtil mapperUtil, UserService userService, @Lazy ProjectService projectService, KeycloakService keycloakService) {
        this.taskRepository = taskRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
        this.projectService = projectService;
        this.keycloakService = keycloakService;
    }

    @Override
    public TaskDTO findById(Long id) {

        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()){
            return mapperUtil.convert(task.get(), new TaskDTO());
        }

        return null;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        return taskRepository.findAll().stream().map(task -> mapperUtil.convert(task,new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public void save(TaskDTO task) {

        task.setAssignedDate(LocalDate.now());
        task.setTaskStatus(Status.OPEN);
        task.setAssignedEmployee(userService.findByUserName(task.getAssignedEmployee().getUserName()));
        task.setProject(projectService.getByProjectCode(task.getProject().getProjectCode()));

        Task convertedTask = mapperUtil.convert(task,new Task());

        taskRepository.save(convertedTask);

    }

    @Override
    public void update(String taskCode, TaskDTO task) {

        Task foundTask = taskRepository.findByTaskCode(taskCode);

        Task convertedTask = mapperUtil.convert(task,new Task());

        if (foundTask!=null){

            convertedTask.setAssignedDate(foundTask.getAssignedDate());
            convertedTask.setTaskStatus(task.getTaskStatus() == null ? foundTask.getTaskStatus() : task.getTaskStatus());
            convertedTask.setTaskCode(taskCode);
            convertedTask.setId(foundTask.getId());
            convertedTask.setAssignedEmployee(mapperUtil.convert(userService.findByUserName(task.getAssignedEmployee().getUserName()),new User()));
            convertedTask.setProject(mapperUtil.convert(projectService.getByProjectCode(task.getProject().getProjectCode()),new Project()));

            taskRepository.save(convertedTask);

        }
    }

    @Override
    public void delete(String taskCode) {

        Task foundTask = taskRepository.findByTaskCode(taskCode);

        if (foundTask!=null){
            foundTask.setIsDeleted(true);
            taskRepository.save(foundTask);
        }

    }

    @Override
    public int totalNonCompletedTask(String projectCode) {

        return  taskRepository.totalNonCompletedTasks(projectCode);

    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO project) {

        List<Task> tasksToDelete = taskRepository.findAllByProject(mapperUtil.convert(project,new Project()));

        tasksToDelete.forEach(task -> delete(task.getTaskCode()));
    }

    @Override
    public void completeByProject(ProjectDTO project) {

        List<Task> tasksToComplete = taskRepository.findAllByProject(mapperUtil.convert(project,new Project()));

        tasksToComplete.forEach(task -> {

            TaskDTO taskDTO = mapperUtil.convert(task,new TaskDTO()); // SELECT * FROM users WHERE id = 4 AND is_deleted = false

            taskDTO.setTaskStatus(Status.COMPLETE);

            update(taskDTO.getTaskCode(),taskDTO);

        });
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {

        UserDTO loggedInUser = userService.findByUserName(keycloakService.getLoggedInUsername());

        List<Task> tasks = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status, mapperUtil.convert(loggedInUser,new User()));

        return tasks.stream().map(task -> mapperUtil.convert(task,new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {

        UserDTO loggedInUser = userService.findByUserName(keycloakService.getLoggedInUsername());

        List<Task> tasks = taskRepository.findAllByTaskStatusAndAssignedEmployee(status, mapperUtil.convert(loggedInUser,new User()));

        return tasks.stream().map(task -> mapperUtil.convert(task,new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO employee) {

        List<Task> tasks = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(Status.COMPLETE,mapperUtil.convert(employee,new User()) );

        return tasks.stream().map(task -> mapperUtil.convert(task,new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public TaskDTO findByTaskCode(String taskCode) {
        Task task = taskRepository.findByTaskCode(taskCode);

        return mapperUtil.convert(task, new TaskDTO());
    }

}
