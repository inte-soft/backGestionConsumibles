package com.intesoft.syncworks.interfaces.web;

import com.intesoft.syncworks.interfaces.dto.PasswordUpdateDto;
import com.intesoft.syncworks.interfaces.dto.ResponseDto;
import com.intesoft.syncworks.interfaces.dto.SignupDto;
import com.intesoft.syncworks.interfaces.dto.UserDto;
import com.intesoft.syncworks.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignupDto signupDto) {
        UserDto user = userService.register(signupDto);
        return ResponseEntity.created(URI.create("/users/"+ user.getId())).body(user);
    }

    // create service for update users
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody SignupDto signupDto) {
        UserDto user = userService.update(id, signupDto);
        return ResponseEntity.ok(user);
    }

    // create service to list all users
    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> listAll() {
        List<UserDto> users = userService.listAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateDto passwordUpdateDto) {
        Long userId = userService.updatePassword(id, passwordUpdateDto);
    return ResponseEntity.ok("Password updated successfully for user with ID: " + userId);
    }
}