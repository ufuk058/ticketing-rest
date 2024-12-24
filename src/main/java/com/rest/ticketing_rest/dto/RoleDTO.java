package com.rest.ticketing_rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NotBlank
public class RoleDTO {

    @JsonIgnore
    private Long id;
    private String description;
}
