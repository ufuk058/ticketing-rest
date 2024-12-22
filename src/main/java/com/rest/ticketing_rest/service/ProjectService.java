package com.rest.ticketing_rest.service;

import com.rest.ticketing_rest.dto.ProjectDTO;
import com.rest.ticketing_rest.dto.UserDTO;

import java.util.List;

public interface ProjectService {
    ProjectDTO getByProjectCode(String projectCode);

    List<ProjectDTO> listAllProjects();

    void save(ProjectDTO project);

    void update(String projectCode,ProjectDTO project);

    void delete(String projectCode);

    void complete(String projectCode);

    List<ProjectDTO> listAllProjectDetails();

    List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager);
}
