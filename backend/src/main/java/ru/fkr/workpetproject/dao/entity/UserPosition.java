package ru.fkr.workpetproject.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_position")
public class UserPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_position_id")
    private Integer userPositionId;

    @Column(name = "name")
    private String namePosition;

    @Column(name = "active")
    private Boolean active;
}
