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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dileep Kumar
 */
@Entity
@Table(name = "filter_rule", catalog = "fund", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FilterRule.findAll", query = "SELECT f FROM FilterRule f"),
    @NamedQuery(name = "FilterRule.findByFilterId", query = "SELECT f FROM FilterRule f WHERE f.filterId = :filterId"),
    @NamedQuery(name = "FilterRule.findByRuleCode", query = "SELECT f FROM FilterRule f WHERE f.ruleCode = :ruleCode"),
    @NamedQuery(name = "FilterRule.findByRuleValue", query = "SELECT f FROM FilterRule f WHERE f.ruleValue = :ruleValue")})
public class FilterRule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "filter_id")
    private Integer filterId;
    @Size(max = 20)
    @Column(name = "rule_code")
    private String ruleCode;
    @Column(name = "rule_value")
    private Integer ruleValue;
    @JoinColumn(name = "state_id", referencedColumnName = "state_code")
    @ManyToOne(optional = false)
    private State stateId;
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    @ManyToOne(optional = false)
    private Department departmentId;

    public FilterRule() {
    }

    public FilterRule(Integer filterId) {
        this.filterId = filterId;
    }

    public Integer getFilterId() {
        return filterId;
    }

    public void setFilterId(Integer filterId) {
        this.filterId = filterId;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public Integer getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(Integer ruleValue) {
        this.ruleValue = ruleValue;
    }

    public State getStateId() {
        return stateId;
    }

    public void setStateId(State stateId) {
        this.stateId = stateId;
    }

    public Department getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Department departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (filterId != null ? filterId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FilterRule)) {
            return false;
        }
        FilterRule other = (FilterRule) object;
        if ((this.filterId == null && other.filterId != null) || (this.filterId != null && !this.filterId.equals(other.filterId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fundpkg.FilterRule[ filterId=" + filterId + " ]";
    }
    
}
