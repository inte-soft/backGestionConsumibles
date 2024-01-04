package com.intesoft.syncworks.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class UserRol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUserRol;
    @Column(name = "id_user")
    private Long idUser;
    @Column(name = "id_rol")
    private Long idRol;
}
