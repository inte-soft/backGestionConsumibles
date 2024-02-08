package com.intesoft.syncworks.interfaces.dto;

import com.intesoft.syncworks.domain.entities.Area;
import com.intesoft.syncworks.domain.entities.Rol;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    private String email;






}
