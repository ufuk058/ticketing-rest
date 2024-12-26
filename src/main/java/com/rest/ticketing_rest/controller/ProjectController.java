package com.rest.ticketing_rest.controller;

import com.rest.ticketing_rest.dto.ProjectDTO;
import com.rest.ticketing_rest.dto.ResponseWrapper;
import com.rest.ticketing_rest.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@Tag(name=" Project Controller", description = " Project Api")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all projects")
    public ResponseEntity<ResponseWrapper> getAllProjects(){

        return ResponseEntity.ok( new ResponseWrapper("All projects retrieved successfully",projectService.listAllProjects()));
    }

    @GetMapping("/{projectCode}")
    @Operation(summary = "Get project by project code")
    public ResponseEntity<ResponseWrapper> getProjectByProjectCode(@PathVariable("projectCode")String projectCode){

        return ResponseEntity.ok(new ResponseWrapper("Project--> "+projectCode+" retrieved successfully",
                                                                    projectService.getByProjectCode(projectCode)));
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new project")
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO){

        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("New project successfully created",201,projectDTO));
    }

    @PutMapping("/update/{projectCode}")
    @Operation(summary = "Update existing project by project code")
    public ResponseEntity<ResponseWrapper> updateProject(@PathVariable("projectCode")String projectCode,
                                                         @RequestBody ProjectDTO projectDTO){
        projectService.update(projectCode,projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project Successfully updated", projectDTO));
    }

    @DeleteMapping("/delete/{projectCode}")
    @Operation(summary = "Delete existing project by project code")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectCode")String projectCode){
        projectService.delete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project Successfully updated"));
    }

    @GetMapping("/manager/project-status")
    @Operation(summary = "Get manager project by project status")
    public ResponseEntity<ResponseWrapper> getProjectsByManager(){
        List<ProjectDTO> projectDTOList= projectService.listAllProjectDetails();
        return ResponseEntity.ok(new ResponseWrapper("Projetcs successfully retrieved", projectDTOList));
    }

    @PutMapping("/manager/complete/{projectCode}")
    @Operation(summary = "Update project status")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode")String projectCode){

        projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project completed", projectService.getByProjectCode(projectCode)));

    }
}
