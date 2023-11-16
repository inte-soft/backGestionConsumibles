package com.intesoft.syncworks.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

@Entity(name = "operacion")
public class Operation {
    private int id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @Column(name = "idRol")
    private Module module;
}
