package com.intesoft.syncworks.interfaces.dto;

import com.intesoft.syncworks.domain.entities.Area;
import com.intesoft.syncworks.domain.entities.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
public class UserDto {
    private Long id;
    private String userName;
    private String name;
    private String lastName;
    private String token;
    private List<Rol> rol;
    private Area area;

    public UserDto() {
    }

    public UserDto(Long id, String userName, String name, String lastName, String token, List<Rol> rol, Area area) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.lastName = lastName;
        this.token = token;
        this.rol= rol;
        this.area = area;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Rol> getRoles() {
        return rol;
    }

    public void setRoles(List<Rol> rol) {
        this.rol = rol;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

}
