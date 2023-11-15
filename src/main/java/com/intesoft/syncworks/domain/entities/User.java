package com.intesoft.syncworks.domain.entities;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "USER")
    private String user;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "APELLIDO")
    private String apellido;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROL_ID", referencedColumnName = "ID")
    private Rol rol;


}
