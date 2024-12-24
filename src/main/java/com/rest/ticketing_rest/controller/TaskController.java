package com.rest.ticketing_rest.controller;

import com.rest.ticketing_rest.dto.ResponseWrapper;
import com.rest.ticketing_rest.dto.TaskDTO;
import com.rest.ticketing_rest.enums.Status;
import com.rest.ticketing_rest.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    ResponseEntity<ResponseWrapper> getAllTasks(){

        return ResponseEntity.ok(new ResponseWrapper("All tasks restrieved", taskService.listAllTasks()));
    }

    @GetMapping("/{taskCode}")
    public ResponseEntity<ResponseWrapper> getTaskByTaskCode(@PathVariable("taskCode")String taskCode){
       TaskDTO taskDto= taskService.findByTaskCode(taskCode);

       return ResponseEntity.ok(new ResponseWrapper("Task retrieved by task Code", taskDto));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO){

        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("New Task created",201, taskDTO));
    }

    @PutMapping("/update/{taskCode}")
    public ResponseEntity<ResponseWrapper> createTask(@PathVariable("taskCode")String taskCode, @RequestBody TaskDTO taskDTO){

        taskService.update(taskCode,taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task updated", taskDTO));
    }

    @DeleteMapping("/delete/{taskCode}")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("taskCode")String taskCode){
        taskService.delete(taskCode);

        return ResponseEntity.ok(new ResponseWrapper("Task successfully deleted"));
    }

    @GetMapping("/employee/pending-tasks")
    public ResponseEntity<ResponseWrapper> employeePendingTask(){
        List<TaskDTO> taskDTOList= taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Employee pending tasks retrieved",taskDTOList));
    }

    @GetMapping("/employee/archived-tasks")
    public ResponseEntity<ResponseWrapper> employeeArchivedTasks(){
        List<TaskDTO> archivedTaskList=taskService.listAllTasksByStatus(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Archived Tasks retrieved", archivedTaskList));
    }

    @PutMapping("/employee/update/{taskCode}")
    public ResponseEntity<ResponseWrapper> employeeUpdateTask(@PathVariable("taskCode")String taskCode,@RequestBody TaskDTO taskDTO){

        taskService.update(taskCode,taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Employee Task Successfully  updated",taskDTO));
    }
}
