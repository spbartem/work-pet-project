package ru.fkr.workpetproject.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "vckp_moneta_session")
public class VckpMonetaSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vckp_moneta_session_id")
    private Long vckpMonetaSessionId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "path_folder")
    private String pathFolder;

    @Column(name = "file_loaded")
    private Boolean fileLoaded;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vckp_moneta_session_status_id")
    private VckpMonetaSessionStatus vckpMonetaSessionStatus;

    @Column(name = "date_unload")
    private LocalDate dateUnload;

    @Column(name = "count_row_moneta")
    private Integer countRowMoneta;

    @Column(name = "path_new_file")
    private String pathNewFile;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "vckp_moneta_type_id")
    private Long vckpMonetaTypeId;

    @Column(name = "file_id")
    private Long fileId;

    // @Column(name = "progress")
    // private Double progress;

    // @Column(name = "period")
    // private LocalDate period;
}
