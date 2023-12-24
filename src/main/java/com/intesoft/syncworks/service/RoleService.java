package com.intesoft.syncworks.service;

import com.intesoft.syncworks.domain.entities.Rol;
import com.intesoft.syncworks.domain.repositories.RolRepository;
import com.intesoft.syncworks.interfaces.dto.RolDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.intesoft.syncworks.mappers.RolMapper;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    public  List<RolDto> listAll() {
        List<Rol> roles = rolRepository.findAll();
        return rolMapper.toRolDto(roles);

    }
}
