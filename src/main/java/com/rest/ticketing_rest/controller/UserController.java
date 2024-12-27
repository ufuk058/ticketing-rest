package com.rest.ticketing_rest.controller;

import com.rest.ticketing_rest.annotation.ExecutionTime;
import com.rest.ticketing_rest.dto.ResponseWrapper;
import com.rest.ticketing_rest.dto.UserDTO;
import com.rest.ticketing_rest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name="User Controller", description = "User Api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ExecutionTime
    @GetMapping("/all")
    @Operation(summary = "Get Users")
    @ApiResponses(value={
            @ApiResponse(
                    responseCode = "200",description = "Retrieve all users",
                    content = @Content( mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "User already exist")
})
    public ResponseEntity<ResponseWrapper> getUsers(){


                List<UserDTO> userDtoList= userService.listAllUsers();

        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved", userDtoList));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get user by username")
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable("username") String username){
        UserDTO user = userService.findByUserName(username);

        return ResponseEntity.ok(new ResponseWrapper("User "+username+" successfully retrieved", user));

    }

    @PostMapping("/create")
    @Operation(summary = "Create a new user")
    public ResponseEntity<ResponseWrapper> createNewUser(@RequestBody @Valid UserDTO userDto){
        userService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User-->"+userDto.getUserName()+" successfully created",
                201,userDto));
    }

    @PutMapping("/update/{username}")
    @Operation(summary = "Update existing user by username")
    public ResponseEntity<ResponseWrapper> updateUser(@PathVariable("username")String username,
                                                      @RequestBody UserDTO userDTO){

        userService.update(username,userDTO);
        return ResponseEntity.ok(new ResponseWrapper("User--> "+username+" successfully updated",userDTO));
    }

    @DeleteMapping("/delete/{username}")
    @Operation(summary = "Delete user by username")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username){

        userService.delete(username);
        return
                 ResponseEntity.ok(new ResponseWrapper("User successfully deleted"));
    }
}
