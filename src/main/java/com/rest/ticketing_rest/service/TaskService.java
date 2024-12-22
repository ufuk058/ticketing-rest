package com.rest.ticketing_rest.service;

import com.rest.ticketing_rest.dto.ProjectDTO;
import com.rest.ticketing_rest.dto.TaskDTO;
import com.rest.ticketing_rest.dto.UserDTO;
import com.rest.ticketing_rest.enums.Status;

import java.util.List;

public interface TaskService {
    TaskDTO findById(Long id);

    List<TaskDTO> listAllTasks();

    void save(TaskDTO task);

    void update(TaskDTO task);

    void delete(Long id);

    int totalNonCompletedTask(String projectCode);

    int totalCompletedTask(String projectCode);

    void deleteByProject(ProjectDTO project);

    void completeByProject(ProjectDTO project);

    List<TaskDTO> listAllTasksByStatusIsNot(Status status);

    List<TaskDTO> listAllTasksByStatus(Status status);

    List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO employee);
}
