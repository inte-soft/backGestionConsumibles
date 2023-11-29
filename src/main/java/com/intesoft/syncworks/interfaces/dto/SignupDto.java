package com.intesoft.syncworks.interfaces.dto;

import com.intesoft.syncworks.domain.entities.Area;
import com.intesoft.syncworks.domain.entities.Rol;

public record SignupDto (String name, String lastName, String userName, char [] password, Rol rol, Area area) {

}
