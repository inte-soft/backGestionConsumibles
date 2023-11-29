package com.intesoft.syncworks.interfaces.web;

import com.intesoft.syncworks.interfaces.dto.ResponseDto;
import com.intesoft.syncworks.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/messages")
    public ResponseEntity<List<String>> messages(){
        return ResponseEntity.ok(Arrays.asList("first","second"));
    }
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createUser(@RequestBody CreateUserRequest request) {
        userService.createUser(request.getUsername(), request.getPassword(), request.getName(), request.getLastName());

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.CREATED.value());
        responseDto.setInfo("User created successfully", HttpStatus.CREATED.value(), "message");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    static class CreateUserRequest {
        private String username;
        private String password;
        private String name;
        private String lastName;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

}