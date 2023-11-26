package com.intesoft.syncworks.domain.entities;

import jakarta.persistence.*;

@Entity(name = "rolOperation")
public class RolOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Rol rol;
    @ManyToOne
    @JoinColumn(name = "operation_id")
    private Operation operation;

}
