package com.intesoft.syncworks.mappers;

import com.intesoft.syncworks.domain.entities.Rol;
import com.intesoft.syncworks.interfaces.dto.RolDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolMapper {

    @IterableMapping(elementTargetType = RolDto.class)
    List<RolDto> toRolDto(List<Rol> rol);
}
