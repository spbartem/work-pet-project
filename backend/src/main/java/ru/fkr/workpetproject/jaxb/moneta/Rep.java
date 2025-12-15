package ru.fkr.workpetproject.jaxb.moneta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;
import ru.fkr.workpetproject.dao.dto.moneta.MonetaRecordDto;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REP", namespace = "http://msp.gcjs.spb/QR/1.0.4")
public class Rep {

    @XmlAttribute(name = "PERIOD_ACC")
    private String periodAcc;

    @XmlAttribute(name = "REP_ACC")
    private String repAcc;

    @XmlAttribute(name = "SQ_PAY")
    private Double sqPay;

    @XmlAttribute(name = "LS_TYPE")
    private Integer lsType;

    @XmlAttribute(name = "OBJECT_GUID")
    private String objectGuid;

    @XmlAttribute(name = "FLAT")
    private String flat;

    @XmlAttribute(name = "PREMISE_GUID")
    private String premiseGuid;

    @XmlAttribute(name = "LS_ID")
    private Integer lsId;

    @XmlAttribute(name = "OBJECT_ADDRESS")
    private String objectAddress;

    public MonetaRecordDto toCoinRecord() {
        return new MonetaRecordDto(
                periodAcc,
                repAcc,
                null,
                sqPay,
                premiseGuid,
                flat,
                null,
                null,
                objectAddress,
                objectGuid,
                lsType,
                lsId,
                null, null, null, null, null, null
        );
    }
}
