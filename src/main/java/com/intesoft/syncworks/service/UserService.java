package com.intesoft.syncworks.service;

import com.intesoft.syncworks.domain.entities.User;
import com.intesoft.syncworks.domain.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}