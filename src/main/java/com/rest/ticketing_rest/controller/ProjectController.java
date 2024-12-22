package com.rest.ticketing_rest.controller;

import com.rest.ticketing_rest.dto.ProjectDTO;
import com.rest.ticketing_rest.dto.ResponseWrapper;
import com.rest.ticketing_rest.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper> getAllProjects(){

        return ResponseEntity.ok( new ResponseWrapper("All projects retrieved successfully",projectService.listAllProjects()));
    }

    @GetMapping("/{projectCode}")
    public ResponseEntity<ResponseWrapper> getProjectByProjectCode(@PathVariable("projectCode")String projectCode){

        return ResponseEntity.ok(new ResponseWrapper("Project--> "+projectCode+" retrieved successfully",
                                                                    projectService.getByProjectCode(projectCode)));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO){

        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("New project successfully created",201,projectDTO));
    }

    @PutMapping("/update/{projectCode}")
    public ResponseEntity<ResponseWrapper> updateProject(@PathVariable("projectCode")String projectCode,
                                                         @RequestBody ProjectDTO projectDTO){
        projectService.update(projectCode,projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project Successfully updated", projectDTO));
    }

    @DeleteMapping("/delete/{projectCode}")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectCode")String projectCode){
        projectService.delete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project Successfully updated"));
    }

    @GetMapping("/manager/project-status")
    public ResponseEntity<ResponseWrapper> getProjectsByManager(){
        List<ProjectDTO> projectDTOList= projectService.listAllProjectDetails();
        return ResponseEntity.ok(new ResponseWrapper("Projetcs successfully retrieved", projectDTOList));
    }

    @PutMapping("/manager/complete/{projectCode}")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode")String projectCode){

        projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project completed", projectService.getByProjectCode(projectCode)));

    }
}
