package ru.fkr.workpetproject.dao.entity;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "\"ext_house_category-reg\"")
public class ExtHouseCategoryReg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "house_category_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ext_system_id", referencedColumnName = "ext_system_id", insertable = true, updatable = true)
    private ExtSystem externalSystem;

}
