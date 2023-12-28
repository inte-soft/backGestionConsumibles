package com.intesoft.syncworks.domain.repositories;

import com.intesoft.syncworks.domain.entities.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> findByName(String name);


}
