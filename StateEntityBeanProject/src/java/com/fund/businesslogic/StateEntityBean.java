/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fund.businesslogic;

import com.fund.business.StateEntity;
import com.fund.entity.State;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author sizzler
 */
@Stateless
public class StateEntityBean implements StateEntity {
    @PersistenceContext (unitName = "StatePU")
    private EntityManager entityManager;
    
    @Override
    public List<State> getAllStates() {
        try{
            String queryString = "SELECT state from " + State.class.getName() + " state";
            Query query = entityManager.createQuery(queryString);
            List<State> states = query.getResultList();
            return states;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    
}
