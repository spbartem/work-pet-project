package ru.fkr.workpetproject.dao.entity;

import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "\"ext_house-reg\"")
public class ExtHouseReg extends BaseHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "house_id")
    private Long houseId;

    @Column(name = "house_num")
    private String houseNum;

    @Column(name = "building")
    private String building;

    @Column(name = "liter")
    private String liter;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "zip")
    private String zip;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private ExtDistrictReg districtId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "house_category_id")
    private ExtHouseCategoryReg houseCategory;

    @Column(name = "built_year")
    private Integer builtYear;

    @Column(name = "sqr_full")
    private BigDecimal sqrFull;

    @Column(name = "sqr_rooms")
    private BigDecimal sqrRooms;

    @Column(name = "date_priv")
    private LocalDate datePriv;

    @Column(name = "separation")
    private String separation;

    @Column(name = "house_num_norm")
    private Integer houseNumNorm;

    @Column(name = "num_pp")
    private String numPP;

    @Column(name = "construction")
    private String construction;
}
