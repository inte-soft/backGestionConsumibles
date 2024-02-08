package com.intesoft.syncworks.interfaces.web;

import com.intesoft.syncworks.domain.entities.Rol;
import com.intesoft.syncworks.interfaces.dto.*;
import com.intesoft.syncworks.service.UserService;
import com.intesoft.syncworks.service.RoleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Base64;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/admin/register")
    public ResponseEntity<UserDto> register(@RequestBody SignupDto signupDto) {
        UserDto user = userService.register(signupDto);
        return ResponseEntity.created(URI.create("/users/"+ user.getId())).body(user);
    }

    // create service for update users
    @PutMapping("/admin/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody SignupDto signupDto) {
        UserDto user = userService.update(id, signupDto);
        return ResponseEntity.ok(user);
    }


    // create service to list all users
    @GetMapping("/admin/list")
    public ResponseEntity<List<UserDto>> listAll() {
        List<UserDto> users = userService.listAll();
        return ResponseEntity.ok(users);
    }



    @PutMapping("myuser/{id}/password")
    public ResponseEntity<ResponseDto> updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateDto passwordUpdateDto) {
        Long userId = userService.updatePassword(id, passwordUpdateDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setInfo("Password updated successfully for user with ID: " + userId,200,"message");
        return ResponseEntity.ok(responseDto);
    }

    //create endpoint to delete users
    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long id) {
        userService.delete(id);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setInfo("User deleted successfully"
                , 200, "message");
        return ResponseEntity.ok(responseDto);
    }

    //create endpoint to list roles
    @GetMapping("/admin/roles")
    public ResponseEntity<List<RolDto>> listRoles() {
        List<RolDto> roles = roleService.listAll();
        return ResponseEntity.ok(roles);
    }

    @PutMapping("/admin/{id}/roles")
    public ResponseEntity<ResponseDto> updateRoles(@PathVariable Long id, @RequestBody SignupDto signupDto) {
        UserDto userDto = userService.updateRol(id, signupDto);
        ResponseDto responseDto =  new ResponseDto();
        responseDto.setInfo("Roles updated successfully for user with ID: " + userDto.getId(),200,"OK");
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/admin/arealist")
    public ResponseEntity<List<AreaDto>> listAreas() {
        List<AreaDto> areas = userService.listAreas();
        return ResponseEntity.ok(areas);
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<ResponseDto> updateUser(@PathVariable Long id, @RequestBody SignupDto signupDto) {
        UserDto userDto = userService.updateUser(id, signupDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setInfo("User updated successfully for user with ID: " + userDto.getId(),200,"message");
        return ResponseEntity.ok(responseDto);
    }

    // metodo para obtener una foto del usuario que se encuentra en una ruta
    @GetMapping("myuser/{id}/photo")
    public ResponseEntity<ResponseDto> getPhoto(@PathVariable String id) {
        Long idUser = Long.parseLong(id);
        String photo = userService.getPhoto(idUser);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setInfo(photo,200,"message");
        return ResponseEntity.ok(responseDto);
    }

    //metodo para guardar o actualizar una foto de perfil del usuario
    @PutMapping("myuser/photo")
    public ResponseEntity<ResponseDto> updatePhoto(@RequestParam("file") MultipartFile photo,
                                                   @RequestParam("id") Long id) {

        String photoback = userService.updatePhoto(id, photo);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setInfo(photoback, 200, "message");
        return ResponseEntity.ok(responseDto);
    }

    //metodo para obtener los datos de usurio como nombres correo
    @GetMapping("myuser/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        Long idUser = Long.parseLong(id);
        UserDto userDto = userService.getUser(idUser);
        return ResponseEntity.ok(userDto);
    }
}