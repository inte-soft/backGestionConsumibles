package com.intesoft.syncworks.interfaces.dto;



import com.intesoft.syncworks.domain.entities.Area;
import com.intesoft.syncworks.domain.entities.Rol;

import java.util.List;

public record SignupDto (String name, String lastName, String userName, char [] password, List<Rol> rols, Area area) {

}
