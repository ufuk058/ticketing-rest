package com.rest.ticketing_rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rest.ticketing_rest.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDTO {

    private Long id;

    @NotBlank(message = "Task Code is a required field")
    private String taskCode;

    @NotNull(message = "Please select a Project")
    private ProjectDTO project;

    @NotNull(message = "Please select an Employee")
    private UserDTO assignedEmployee;

    @NotBlank(message = "Task Subject is a required field")
    private String taskSubject;

    @NotBlank(message = "Task Detail is a required field")
    private String taskDetail;

    private LocalDate assignedDate;
    private Status taskStatus;
}
