package ru.fkr.workpetproject.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vckp_moneta_session_status")
public class VckpMonetaSessionStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vckp_moneta_session_status_id")
    private Long vckpMonetaSessionStatusId;

    @Column(name = "name")
    private String statusName;

    @Column(name = "code")
    private String statusCode;
}
