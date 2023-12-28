package com.intesoft.syncworks.mappers;

import com.intesoft.syncworks.domain.entities.Area;
import com.intesoft.syncworks.interfaces.dto.AreaDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AreaMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public AreaDto toAreaDto(Area area);

    @IterableMapping(elementTargetType = AreaDto.class)
    List<AreaDto> toAreaDtoList(List<Area> areas);
}
