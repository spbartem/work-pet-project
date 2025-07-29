package ru.fkr.workpetproject.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "prm_role")
public class PrmRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prm_role_id")
    private Long prmRoleId;

    @Column(name = "name")
    private String roleFullName;

    @Column(name = "active")
    private Boolean isActive;

    @Column(name = "code")
    private String roleName;
}
