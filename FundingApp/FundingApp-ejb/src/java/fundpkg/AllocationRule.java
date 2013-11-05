/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkg;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dileep Kumar
 */
@Entity
@Table(name = "allocation_rule", catalog = "fund", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AllocationRule.findAll", query = "SELECT a FROM AllocationRule a"),
    @NamedQuery(name = "AllocationRule.findByAllocationId", query = "SELECT a FROM AllocationRule a WHERE a.allocationId = :allocationId"),
    @NamedQuery(name = "AllocationRule.findByRuleName", query = "SELECT a FROM AllocationRule a WHERE a.ruleName = :ruleName"),
    @NamedQuery(name = "AllocationRule.findByRuleValue", query = "SELECT a FROM AllocationRule a WHERE a.ruleValue = :ruleValue")})
public class AllocationRule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "allocation_id")
    private int allocationId;
    @Size(max = 50)
    @Column(name = "rule_name")
    private String ruleName;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rule_value")
    private Integer ruleValue;

    public AllocationRule() {
    }

    public AllocationRule(Integer ruleValue) {
        this.ruleValue = ruleValue;
    }

    public AllocationRule(Integer ruleValue, int allocationId) {
        this.ruleValue = ruleValue;
        this.allocationId = allocationId;
    }

    public int getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(int allocationId) {
        this.allocationId = allocationId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(Integer ruleValue) {
        this.ruleValue = ruleValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ruleValue != null ? ruleValue.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AllocationRule)) {
            return false;
        }
        AllocationRule other = (AllocationRule) object;
        if ((this.ruleValue == null && other.ruleValue != null) || (this.ruleValue != null && !this.ruleValue.equals(other.ruleValue))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fundpkg.AllocationRule[ ruleValue=" + ruleValue + " ]";
    }
    
}
