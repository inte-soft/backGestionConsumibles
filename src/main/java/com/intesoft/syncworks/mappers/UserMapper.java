package com.intesoft.syncworks.mappers;

import com.intesoft.syncworks.domain.entities.User;
import com.intesoft.syncworks.interfaces.dto.SignupDto;
import com.intesoft.syncworks.interfaces.dto.UserDto;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "id", target = "id")
    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "userName", target = "userName")
    User signUpToUser(SignupDto signupDto);

    @IterableMapping(elementTargetType = UserDto.class)
    List<UserDto> toUserDtoList(List<User> users);
}
