package ru.fkr.workpetproject.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vckp_moneta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VckpMoneta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer vckpMonetaId;

    @Column(name = "period")
    private String period;

    @Column(name = "rep_acc")
    private String repAcc;

    @Column(name = "els")
    private String els;

    @Column(name = "sq_pay")
    private BigDecimal sqPay;

    @Column(name = "premise_guid")
    private String premiseGuid;

    @Column(name = "nkv")
    private String nkv;

    @Column(name = "room_guid")
    private String roomGuid;

    @Column(name = "room")
    private String room;

    @Column(name = "ul")
    private String address;

    @Column(name = "kod_fias")
    private String kodFias;

    @Column(name = "ls_type")
    private Integer lsType;

    @Column(name = "ls_id")
    private Integer lsId;

    @Column(name = "kr_sum")
    private String krSum;

    @Column(name = "rep_trf")
    private BigDecimal repTrf;

    @Column(name = "regn")
    private String regn;

    @Column(name = "kr_st")
    private BigDecimal krSt;

    @Column(name = "lgt_kr")
    private BigDecimal lgtKr;

    @Column(name = "res_code")
    private Integer resCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vckp_moneta_session_id")
    private VckpMonetaSession vckpMonetaSession;
}
