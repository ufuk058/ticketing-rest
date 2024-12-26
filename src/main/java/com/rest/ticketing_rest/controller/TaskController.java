package com.rest.ticketing_rest.controller;

import com.rest.ticketing_rest.dto.ResponseWrapper;
import com.rest.ticketing_rest.dto.TaskDTO;
import com.rest.ticketing_rest.enums.Status;
import com.rest.ticketing_rest.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@Tag(name = "Task Controller", description = "Task Api")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all tasks")
    ResponseEntity<ResponseWrapper> getAllTasks(){

        return ResponseEntity.ok(new ResponseWrapper("All tasks restrieved", taskService.listAllTasks()));
    }

    @GetMapping("/{taskCode}")
    @Operation(summary = "Get task by task code")
    public ResponseEntity<ResponseWrapper> getTaskByTaskCode(@PathVariable("taskCode")String taskCode){
       TaskDTO taskDto= taskService.findByTaskCode(taskCode);

       return ResponseEntity.ok(new ResponseWrapper("Task retrieved by task Code", taskDto));
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new task")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO){

        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("New Task created",201, taskDTO));
    }

    @PutMapping("/update/{taskCode}")
    @Operation(summary = "Update existing task by task code")
    public ResponseEntity<ResponseWrapper> createTask(@PathVariable("taskCode")String taskCode, @RequestBody TaskDTO taskDTO){

        taskService.update(taskCode,taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task updated", taskDTO));
    }

    @DeleteMapping("/delete/{taskCode}")
    @Operation(summary = "Delete existing task by task code")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("taskCode")String taskCode){
        taskService.delete(taskCode);

        return ResponseEntity.ok(new ResponseWrapper("Task successfully deleted"));
    }

    @GetMapping("/employee/pending-tasks")
    @Operation(summary = "Get Employee pending tasks")
    public ResponseEntity<ResponseWrapper> employeePendingTask(){
        List<TaskDTO> taskDTOList= taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Employee pending tasks retrieved",taskDTOList));
    }

    @GetMapping("/employee/archived-tasks")
    @Operation(summary = "Get Employee archived tasks")
    public ResponseEntity<ResponseWrapper> employeeArchivedTasks(){
        List<TaskDTO> archivedTaskList=taskService.listAllTasksByStatus(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Archived Tasks retrieved", archivedTaskList));
    }

    @PutMapping("/employee/update/{taskCode}")
    @Operation(summary = "Update Employee task by task code")
    public ResponseEntity<ResponseWrapper> employeeUpdateTask(@PathVariable("taskCode")String taskCode,@RequestBody TaskDTO taskDTO){

        taskService.update(taskCode,taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Employee Task Successfully  updated",taskDTO));
    }
}
