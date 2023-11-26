package com.intesoft.syncworks.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
