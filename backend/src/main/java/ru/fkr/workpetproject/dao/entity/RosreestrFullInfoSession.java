package ru.fkr.workpetproject.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Data
@Entity
@Table(name = "rosreestr_full_info_session")
public class RosreestrFullInfoSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rosreestr_full_info_session_id;

    @Column(name = "mst_house_id")
    private Long mstHouseId;

    @Column(name = "statement_date_formation")
    private LocalDate statementDateFormation;

    @Column(name = "statement_registration_number")
    private String statementRegistrationNumber;

    @Column(name = "cad_number")
    private String cadNum;

    @Column(name = "area")
    private BigDecimal area;

    @Column(name = "okato")
    private String okato;

    @Column(name = "kladr")
    private String kladr;

    @Column(name = "type_street")
    private String typeStreet;

    @Column(name = "name_street")
    private String nameStreet;

    @Column(name = "type_level1")
    private String typeLevel1;

    @Column(name = "name_level1")
    private String nameLevel1;

    @Column(name = "type_level2")
    private String typeLevel2;

    @Column(name = "name_level2")
    private String nameLevel2;

    @Column(name = "type_level3")
    private String typeLevel3;

    @Column(name = "name_level3")
    private String nameLevel3;

    @Column(name = "type_apartment")
    private String typeApartment;

    @Column(name = "name_apartment")
    private String nameApartment;

    @Column(name = "readable_address")
    private String readableAddress;

    @Column(name = "date_cadastral")
    private LocalDate dateCadastral;

    public void setArea(String area) {
        try {
            this.area = new BigDecimal(area);
        } catch (NumberFormatException e) {
            System.out.println("Error: wrong number format for field \"area\"");
        }
    }

    public void setDateCadastral(String dateCadastral) {
        try {
            if (dateCadastral != null && !dateCadastral.isBlank()) {
                this.dateCadastral = LocalDate.parse(dateCadastral);
            } else {
                this.dateCadastral = null;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Error: wrong date format for field \"date_cadastral\"");
        }
    }
}
