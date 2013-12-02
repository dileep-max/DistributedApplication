/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fund.entity;

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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sizzler
 */
@Entity
@Table(name = "request_history")
//@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "RequestHistory.findAll", query = "SELECT r FROM RequestHistory r"),
//    @NamedQuery(name = "RequestHistory.findByRequestId", query = "SELECT r FROM RequestHistory r WHERE r.requestId = :requestId"),
//    @NamedQuery(name = "RequestHistory.findByRequestedFund", query = "SELECT r FROM RequestHistory r WHERE r.requestedFund = :requestedFund"),
//    @NamedQuery(name = "RequestHistory.findByAllocatedFund", query = "SELECT r FROM RequestHistory r WHERE r.allocatedFund = :allocatedFund"),
//    @NamedQuery(name = "RequestHistory.findByDepartmentId", query = "SELECT r FROM RequestHistory r WHERE r.departmentId = :departmentId")})
public class RequestHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "request_id")
    private Integer requestId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "requested_fund")
    private double requestedFund;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "allocated_fund")
    private Double allocatedFund;
    @Column(name = "department_id")
    private Integer departmentId;
    @JoinColumn(name = "state_id", referencedColumnName = "state_code")
    @ManyToOne
    private State stateId;
    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne
    private Status statusId;

    public RequestHistory() {
    }

    public RequestHistory(Integer requestId) {
        this.requestId = requestId;
    }

    public RequestHistory(Integer requestId, double requestedFund) {
        this.requestId = requestId;
        this.requestedFund = requestedFund;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public double getRequestedFund() {
        return requestedFund;
    }

    public void setRequestedFund(double requestedFund) {
        this.requestedFund = requestedFund;
    }

    public Double getAllocatedFund() {
        return allocatedFund;
    }

    public void setAllocatedFund(Double allocatedFund) {
        this.allocatedFund = allocatedFund;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public State getStateId() {
        return stateId;
    }

    public void setStateId(State stateId) {
        this.stateId = stateId;
    }

    public Status getStatusId() {
        return statusId;
    }

    public void setStatusId(Status statusId) {
        this.statusId = statusId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requestId != null ? requestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestHistory)) {
            return false;
        }
        RequestHistory other = (RequestHistory) object;
        if ((this.requestId == null && other.requestId != null) || (this.requestId != null && !this.requestId.equals(other.requestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fund.entity.RequestHistory[ requestId=" + requestId + " ]";
    }
    
}
