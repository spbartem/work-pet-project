package ru.fkr.workpetproject.dao.dto.moneta;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.fkr.workpetproject.dao.entity.VckpMoneta;
import ru.fkr.workpetproject.dao.entity.VckpMonetaSession;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonetaRecordDto {

    private String periodAcc;
    private String repAcc;
    private String els;
    private Double sqPay;
    private String premiseGuid;
    private String flat;
    private String roomGuid;
    private String room;
    private String objectAddress;
    private String objectGuid;
    private Integer lsType;
    private Integer lsId;
    private Double krSum;
    private Double repTrf;
    private String regn;
    private Double sumT;
    private Double sumLgt;
    private String resCode;

    public VckpMoneta toEntity(VckpMonetaSession session) {
        return VckpMoneta.builder()
                .period(this.periodAcc)
                .repAcc(this.repAcc)
                .els(this.els)
                .sqPay(toBigDecimal(this.sqPay))
                .premiseGuid(this.premiseGuid)
                .nkv(this.flat)
                .roomGuid(this.roomGuid)
                .room(this.room)
                .address(this.objectAddress)
                .kodFias(this.objectGuid)
                .lsType(this.lsType)
                .lsId(this.lsId)
                .krSum(this.krSum != null ? this.krSum.toString() : null)
                .repTrf(toBigDecimal(this.repTrf))
                .regn(this.regn)
                .krSt(toBigDecimal(this.sumT))
                .lgtKr(toBigDecimal(this.sumLgt))
                .resCode(this.resCode != null ? Integer.parseInt(this.resCode) : null)
                .vckpMonetaSession(session)
                .build();
    }

    private static java.math.BigDecimal toBigDecimal(Double value) {
        return value != null ? java.math.BigDecimal.valueOf(value) : null;
    }
}