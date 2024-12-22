package com.rest.ticketing_rest;

import com.rest.ticketing_rest.dto.ResponseWrapper;
import com.rest.ticketing_rest.dto.UserDTO;
import com.rest.ticketing_rest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper> getUsers(){


                List<UserDTO> userDtoList= userService.listAllUsers();

        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved", userDtoList));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable("username") String username){
        UserDTO user = userService.findByUserName(username);

        return ResponseEntity.ok(new ResponseWrapper("User "+username+" successfully retrieved", user));

    }

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> createNewUser(@RequestBody UserDTO userDto){
        userService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User-->"+userDto.getUserName()+" successfully created",
                201,userDto));
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<ResponseWrapper> updateUser(@PathVariable("username")String username,
                                                      @RequestBody UserDTO userDTO){

        userService.update(username,userDTO);
        return ResponseEntity.ok(new ResponseWrapper("User--> "+username+" successfully updated",userDTO));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username){

        userService.delete(username);
        return
                 ResponseEntity.ok(new ResponseWrapper("User successfully deleted"));
    }
}
