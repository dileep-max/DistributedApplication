/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkg;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "fedfund", catalog = "fund", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fedfund.findAll", query = "SELECT f FROM Fedfund f"),
    @NamedQuery(name = "Fedfund.findByFundId", query = "SELECT f FROM Fedfund f WHERE f.fundId = :fundId"),
    @NamedQuery(name = "Fedfund.findByFundDesignation", query = "SELECT f FROM Fedfund f WHERE f.fundDesignation = :fundDesignation"),
    @NamedQuery(name = "Fedfund.findByFundAmount", query = "SELECT f FROM Fedfund f WHERE f.fundAmount = :fundAmount")})
public class Fedfund implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fund_id")
    private Integer fundId;
    @Size(max = 255)
    @Column(name = "fund_Designation")
    private String fundDesignation;
    @Column(name = "fund_Amount")
    private BigInteger fundAmount;

    public Fedfund() {
    }

    public Fedfund(Integer fundId) {
        this.fundId = fundId;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(Integer fundId) {
        this.fundId = fundId;
    }

    public String getFundDesignation() {
        return fundDesignation;
    }

    public void setFundDesignation(String fundDesignation) {
        this.fundDesignation = fundDesignation;
    }

    public BigInteger getFundAmount() {
        return fundAmount;
    }

    public void setFundAmount(BigInteger fundAmount) {
        this.fundAmount = fundAmount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fundId != null ? fundId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fedfund)) {
            return false;
        }
        Fedfund other = (Fedfund) object;
        if ((this.fundId == null && other.fundId != null) || (this.fundId != null && !this.fundId.equals(other.fundId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fundpkg.Fedfund[ fundId=" + fundId + " ]";
    }
    
}
