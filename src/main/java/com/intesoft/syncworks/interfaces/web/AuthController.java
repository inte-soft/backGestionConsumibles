package com.intesoft.syncworks.interfaces.web;

import com.intesoft.syncworks.config.UserAuthProvider;
import com.intesoft.syncworks.interfaces.dto.CredentialDto;
import com.intesoft.syncworks.interfaces.dto.SignupDto;
import com.intesoft.syncworks.interfaces.dto.UserDto;
import com.intesoft.syncworks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialDto credentialDto) {
        UserDto user = userService.login(credentialDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignupDto signupDto) {
        UserDto user = userService.register(signupDto);
        return ResponseEntity.created(URI.create("/users/"+ user.getId())).body(user);
    }
}
