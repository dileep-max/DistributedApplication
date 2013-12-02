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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sizzler
 */
@Entity
@Table(name = "state")
public class State implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id   
    @Basic(optional = false)
    @Column(name = "state_code")
    private Integer stateCode;
    @Column(name = "state_name")
    private String stateName;

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

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (stateCode != null ? stateCode.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof State)) {
//            return false;
//        }
//        State other = (State) object;
//        if ((this.stateCode == null && other.stateCode != null) || (this.stateCode != null && !this.stateCode.equals(other.stateCode))) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public String toString() {
        return "com.fund.entity.State[ stateCode=" + stateCode + " ]";
    }
    
}
