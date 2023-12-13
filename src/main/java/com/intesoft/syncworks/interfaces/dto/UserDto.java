package com.intesoft.syncworks.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
public class UserDto {
    private Long id;
    private String userName;
    private String name;
    private String lastName;
    private String token;

    public UserDto() {
    }

    public UserDto(Long id, String userName, String name, String lastName, String token) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.lastName = lastName;
        this.token = token;
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
}
