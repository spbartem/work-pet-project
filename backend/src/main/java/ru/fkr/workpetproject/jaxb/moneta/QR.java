package ru.fkr.workpetproject.jaxb.moneta;



import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "QR", namespace = "http://msp.gcjs.spb/QR/1.0.4")
@XmlAccessorType(XmlAccessType.FIELD)
public class QR {

    @XmlAttribute(name = "PERIOD")
    private String period;

    @XmlAttribute(name = "ORG_CODE")
    private String orgCode;

    @XmlElement(name = "REP")
    private List<Rep> repList;

    public List<Rep> getRepList() {
        return repList;
    }
}
