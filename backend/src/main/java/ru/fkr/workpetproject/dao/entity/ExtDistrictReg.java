package ru.fkr.workpetproject.dao.entity;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "\"ext_district-reg\"")
public class ExtDistrictReg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ext_system_id", referencedColumnName = "ext_system_id")
    private ExtSystem externalSystem;

    @Column(name = "address_guja")
    private String addressGuja;

    @Column(name = "address_adm")
    private String addressAdm;

    @Column(name = "auction_contract_code")
    private Integer auctionContractCode;

    @Column(name = "district_sequence", columnDefinition = "int8")
    private Integer districtSequence;

    @Column(name = "name_short")
    private String nameShort;

}
