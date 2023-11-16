package com.intesoft.syncworks.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

@Entity(name = "operacion")
public class Operacion {
    private int id;
    private String nombre;
    @ManyToMany(fetch = FetchType.EAGER)
    @Column(name = "idRol")
    private Modulo modulo;
}
