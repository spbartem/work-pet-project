package ru.fkr.workpetproject.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "notified")
    private Boolean notified;

    @Column(name = "user_position_id")
    private Long userPositionId;

    @Column(name = "user_department_id")
    private Long userDepartmentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private PrmRole role;
}
