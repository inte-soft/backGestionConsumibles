package com.intesoft.syncworks.service;

import com.intesoft.syncworks.domain.entities.Area;
import com.intesoft.syncworks.domain.entities.User;
import com.intesoft.syncworks.domain.repositories.AreaRepository;
import com.intesoft.syncworks.domain.repositories.UserRepository;

import com.intesoft.syncworks.exceptions.AppException;
import com.intesoft.syncworks.interfaces.dto.*;
import com.intesoft.syncworks.mappers.AreaMapper;
import com.intesoft.syncworks.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AreaRepository areaRepository;
    private final AreaMapper areaMapper;



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

        
        String encryptedPassword = passwordEncoder.encode(passwordUpdateDto.getNewPassword());
        user.setPassword(encryptedPassword);
        
        userRepository.save(user);
        
        return user.getId();
    }


    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto updateRol(Long id, SignupDto signupDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRol(signupDto.rol());
            User savedUser = userRepository.save(user);
            return userMapper.toUserDto(savedUser);
        }else {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    public List<AreaDto> listAreas() {
        List<Area> areas = areaRepository.findAll();
        return areaMapper.toAreaDtoList(areas);
    }

    public UserDto updateUser(Long id, SignupDto signupDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(signupDto.name());
            user.setLastName(signupDto.lastName());
            user.setArea(signupDto.area());
            user.setEmail(signupDto.email());
            User savedUser = userRepository.save(user);
            return userMapper.toUserDto(savedUser);
        }else {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    public String getPhoto(Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        String imagePath = "C:\\Users\\alejo\\Downloads\\user_image\\" + user.getId() + ".png"; // Reemplaza esto con la ruta real a tus imágenes

        try {
            File file = new File(imagePath);
            if (!file.exists()) {
                imagePath = "C:\\Users\\alejo\\Downloads\\user_image\\0.png"; // Reemplaza esto con la ruta real a tu imagen por defecto
                file = new File(imagePath);
            }
            BufferedImage img = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            byte[] imageInByte = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageInByte);
        } catch (IOException e) {
            throw new AppException("Error reading image file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } else {
        throw new AppException("User not found", HttpStatus.NOT_FOUND);
    }
}

    public String updatePhoto(Long id, MultipartFile imageFile) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String imagePath = "C:\\Users\\alejo\\Downloads\\user_image\\" + user.getId() + ".png"; // Reemplaza esto con la ruta real a tus imágenes

            try {
                File file = new File(imagePath);
                imageFile.transferTo(file);
                return "Image updated successfully";
            } catch (IOException e) {
                throw new AppException("Error writing image file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    public UserDto getUser(Long idUser) {
        Optional<User> optionalUser = userRepository.findById(idUser);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return userMapper.toUserDto(user);
        } else {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }
    }
}