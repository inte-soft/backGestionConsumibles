package com.intesoft.syncworks.domain.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "rolOperacion")
public class RolOperacion {
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Rol rol;
    @OneToMany(fetch = FetchType.EAGER)
    private Operacion operacion;

}
