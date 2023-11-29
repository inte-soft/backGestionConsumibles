package com.intesoft.syncworks.service;

import com.intesoft.syncworks.domain.entities.User;
import com.intesoft.syncworks.domain.repositories.UserRepository;

import com.intesoft.syncworks.exceptions.AppException;
import com.intesoft.syncworks.interfaces.dto.CredentialDto;
import com.intesoft.syncworks.interfaces.dto.SignupDto;
import com.intesoft.syncworks.interfaces.dto.UserDto;
import com.intesoft.syncworks.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public UserDto login(CredentialDto credentialDto){
        User user = userRepository.findByUserName(credentialDto.userName())
                .orElseThrow(()-> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        if (passwordEncoder.matches(CharBuffer.wrap(credentialDto.password()),
                user.getPassword())){
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password",HttpStatus.BAD_REQUEST);
    }


    @Transactional
    public User createUser(String username, String password, String name, String lastName) {
        // Encriptar la contraseña antes de almacenarla en la base de datos
        String encryptedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setUserName(username);
        user.setPassword(encryptedPassword);
        user.setName(name);
        user.setLastName(lastName);

        // Guardar el usuario en la base de datos
        userRepository.save(user);

        return user;
    }

    public UserDto register(SignupDto signupDto) {
        Optional<User> optionalUser = userRepository.findByUserName(signupDto.userName());

        if (optionalUser.isPresent()) {
            throw new AppException("Username alredy exist", HttpStatus.BAD_REQUEST);
        }
        User user = userMapper.signUpToUser(signupDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signupDto.password())));
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }
}