package com.intesoft.syncworks.mappers;

import com.intesoft.syncworks.domain.entities.User;
import com.intesoft.syncworks.interfaces.dto.SignupDto;
import com.intesoft.syncworks.interfaces.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "userName", target = "userName")
    User signUpToUser(SignupDto signupDto);
}
