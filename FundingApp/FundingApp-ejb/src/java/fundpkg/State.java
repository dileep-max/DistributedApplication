/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkg;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dileep Kumar
 */
@Entity
@Table(name = "state", catalog = "fund", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "State.findAll", query = "SELECT s FROM State s"),
    @NamedQuery(name = "State.findByStateCode", query = "SELECT s FROM State s WHERE s.stateCode = :stateCode"),
    @NamedQuery(name = "State.findByStateName", query = "SELECT s FROM State s WHERE s.stateName = :stateName")})
public class State implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "state_code")
    private Integer stateCode;
    @Size(max = 45)
    @Column(name = "state_name")
    private String stateName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stateId")
    private Collection<FilterRule> filterRuleCollection;
    @OneToMany(mappedBy = "stateId")
    private Collection<RequestHistory> requestHistoryCollection;

    public State() {
    }

    public State(Integer stateCode) {
        this.stateCode = stateCode;
    }

    public Integer getStateCode() {
        return stateCode;
    }

    public void setStateCode(Integer stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @XmlTransient
    public Collection<FilterRule> getFilterRuleCollection() {
        return filterRuleCollection;
    }

    public void setFilterRuleCollection(Collection<FilterRule> filterRuleCollection) {
        this.filterRuleCollection = filterRuleCollection;
    }

    @XmlTransient
    public Collection<RequestHistory> getRequestHistoryCollection() {
        return requestHistoryCollection;
    }

    public void setRequestHistoryCollection(Collection<RequestHistory> requestHistoryCollection) {
        this.requestHistoryCollection = requestHistoryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stateCode != null ? stateCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof State)) {
            return false;
        }
        State other = (State) object;
        if ((this.stateCode == null && other.stateCode != null) || (this.stateCode != null && !this.stateCode.equals(other.stateCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fundpkg.State[ stateCode=" + stateCode + " ]";
    }
    
}
