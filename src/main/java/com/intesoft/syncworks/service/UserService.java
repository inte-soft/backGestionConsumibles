package com.intesoft.syncworks.service;

import com.intesoft.syncworks.domain.entities.User;
import com.intesoft.syncworks.domain.repositories.UserRepository;

import com.intesoft.syncworks.exceptions.AppException;
import com.intesoft.syncworks.interfaces.dto.CredentialDto;
import com.intesoft.syncworks.interfaces.dto.PasswordUpdateDto;
import com.intesoft.syncworks.interfaces.dto.SignupDto;
import com.intesoft.syncworks.interfaces.dto.UserDto;
import com.intesoft.syncworks.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;
import java.util.List;
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

    public UserDto update(Long id, SignupDto signupDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(signupDto.name());
            user.setLastName(signupDto.lastName());
            User savedUser = userRepository.save(user);
            return userMapper.toUserDto(savedUser);
        }else {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }

    }


    public List<UserDto> listAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserDtoList(users);
    }

    public Long updatePassword(Long id, PasswordUpdateDto passwordUpdateDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        
        boolean isMatch = passwordEncoder.matches(passwordUpdateDto.getOldPassword(), user.getPassword());
        if (!isMatch) {
            throw new AppException("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }
        
        String encryptedPassword = passwordEncoder.encode(passwordUpdateDto.getNewPassword());
        user.setPassword(encryptedPassword);
        
        userRepository.save(user);
        
        return user.getId();
    }


}