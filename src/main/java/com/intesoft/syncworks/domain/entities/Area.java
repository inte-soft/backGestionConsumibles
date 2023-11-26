package com.intesoft.syncworks.domain.entities;

import jakarta.persistence.*;

@Entity
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
