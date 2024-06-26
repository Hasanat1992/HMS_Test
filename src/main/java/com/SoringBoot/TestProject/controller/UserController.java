package com.SoringBoot.TestProject.controller;



import com.SoringBoot.TestProject.dto.MessageResponse;
import com.SoringBoot.TestProject.dto.UserDto;
import com.SoringBoot.TestProject.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
//        List<UserDto> dtoList;
//        try {
//            dtoList = userService.findAll();
//        }catch (Exception e){
////            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
//        }
//        return ResponseEntity.ok(dtoList);
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userName}")
    public ResponseEntity<UserDto> getUser(@PathVariable(name = "userName") final String userName) {
        return ResponseEntity.ok(userService.get(userName));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody final UserDto userDTO)
            throws MethodArgumentNotValidException {
        if (userService.userNameExists(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.emailExists(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        userDTO.setRoles(Collections.singleton("ROLE_USER"));
        try {
            userService.create(userDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse("one of role01s not found"));
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{userName}")
    public ResponseEntity<Void> updateUser(@PathVariable(name = "userName") final String userName,
            @RequestBody @Valid final UserDto userDTO) {
        userService.update(userName, userDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userName}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userName") final String userName) {
        userService.delete(userName);
        return ResponseEntity.noContent().build();
    }

}
