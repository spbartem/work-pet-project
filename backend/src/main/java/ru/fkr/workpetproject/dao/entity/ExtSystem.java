package ru.fkr.workpetproject.dao.entity;

import lombok.Data;

import jakarta.persistence.*;

/**
 * @author vkrasikov
 * Date: 17.10.2014.
 */

@Data
@Entity
@Table(name = "ext_system")
public class ExtSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ext_system_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "label")
    private String label;

    @Column(name = "billing_agent")
    boolean billingAgent;

    @Column(name = "service_codes")
    private String serviceCodes;

    @Column(name = "validation_code")
    private String validationCode;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "export_billing_agent")
    private boolean exportBillingAgent;

    @Column(name = "group_code")
    private String groupCode;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (billingAgent ? 1231 : 1237);
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExtSystem other = (ExtSystem) obj;
        if (billingAgent != other.billingAgent)
            return false;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (parentId == null) {
            if (other.parentId != null)
                return false;
        } else if (!parentId.equals(other.parentId))
            return false;
        return true;
    }

}