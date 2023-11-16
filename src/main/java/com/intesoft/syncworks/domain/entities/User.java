package com.intesoft.syncworks.domain.entities;

import javax.persistence.*;

@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user")
    private String user;
    @Column(name = "password")
    private String password;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido")
    private String apellido;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "rolId", referencedColumnName = "id")
    private Rol rol;


}
