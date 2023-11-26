package com.intesoft.syncworks.domain.entities;

import jakarta.persistence.*;

@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idModule")
    private Module module;
}
