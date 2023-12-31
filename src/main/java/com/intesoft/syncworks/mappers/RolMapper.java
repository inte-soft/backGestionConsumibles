package com.intesoft.syncworks.mappers;



import com.intesoft.syncworks.domain.entities.Rol;
import com.intesoft.syncworks.interfaces.dto.RolDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public RolDto toRolDto(Rol rol);

    @IterableMapping(elementTargetType = RolDto.class)
    List<RolDto> toRolDto(List<Rol> rol);
}
