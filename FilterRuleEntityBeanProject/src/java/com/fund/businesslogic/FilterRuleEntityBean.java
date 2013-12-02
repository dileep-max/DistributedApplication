/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fund.businesslogic;

import com.fund.business.FilterRuleEntity;
import com.fund.entity.Department;
import com.fund.entity.FilterRule;
import com.fund.entity.RequestHistory;
import com.fund.entity.State;
import com.fund.entity.Status;
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
public class FilterRuleEntityBean implements FilterRuleEntity {
    @PersistenceContext (unitName = "FilterPU")
    private EntityManager entityManager;
    
    public void processRequest(int stateId, int departmentId, double requestedFundAmount){
        State state = new State();
        Department dept = new Department();
        state.setStateCode(stateId);
        dept.setDepartmentId(departmentId);
        FilterRule filterRule = new FilterRule();
        filterRule.setStateId(state);
        filterRule.setDepartmentId(dept);
        //filterRule.setDepartmentId(null);
        List<FilterRule> filterRules = getFilterRules(filterRule);
        
        //for now i'm statically checking what rule we need and checking if it is satisfied
        if(filterRules.size()<=0){
            System.out.println("No rows found!\n");
            return;
        }
        System.out.println("filterRules is "+filterRules.size()+ "\n");
        int i=0;
        for(i=0;i<filterRules.size();i++){
            //this means that we have to check for reqs/month
            if(filterRules.get(i).getRuleCode().equals("Max_req_per_month")){                
                FilterRule currentRule = filterRules.get(i);
                if(filterRules.get(i)==null){
                    System.out.println("ITS NULLLLLL\n");
                    break;
                }    
                int currentReqCount = filterRules.get(i).getActualValue();
                int ruleReqCount = currentRule.getRuleValue();
                System.out.println("FilterId: "+currentRule.getFilterId()+"CurrentReqCount: "
                        +currentReqCount+" RuleReqCount: "+ruleReqCount);
                RequestHistory reqHistory = new RequestHistory();
                Status status = new Status();
                reqHistory.setRequestedFund(requestedFundAmount);
                reqHistory.setDepartmentId(departmentId);
                reqHistory.setStateId(state);
                reqHistory.setAllocatedFund(0.0);
                //filter fail
                if(currentReqCount+1>ruleReqCount){            
                    status.setStatusId(5);
                    reqHistory.setStatusId(status);
                    if(insertIntoRequestHistory(reqHistory)){
                        System.out.println("SUCCESS: Updated request_history for filter FAIL");
                    }
                }
                //filter pass
                else{
                    //pass code
                    status.setStatusId(4);                        
                    reqHistory.setStatusId(status);
                    if(insertIntoRequestHistory(reqHistory)){ 
                        System.out.println("SUCCESS: Inserted into request_history for filter PASS");                
                        currentRule.setActualValue(currentRule.getActualValue()+1);  
                        if(updateFilterRule(currentRule)){
                            System.out.println("SUCCESS: Updated both request_history and filter_rule");
                        }
                    }
                }
                break;
            }
            else
                i++;
        }
        
        if(i>filterRules.size()){
            System.out.println("No proper rule_id found, quitting\n");
            return;
        }                
    }
    
    private boolean insertIntoRequestHistory(RequestHistory reqHistory){
        try{
            entityManager.merge(reqHistory);
            entityManager.flush();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean updateFilterRule(FilterRule filterRule){
        try{
            entityManager.merge(filterRule);
            entityManager.flush();
           /*Query query = entityManager.createQuery("UPDATE FilterRule f SET f.actualValue = :actVal WHERE f.filterId = :filterId");
            query.setParameter("filterId", filterRule.getFilterId());           
            query.setParameter("actVal", filterRule.getActualValue());*/
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    public List<FilterRule> getFilterRules(FilterRule filterRule){
        try{
            //String queryString = "SELECT filter_rule from " + FilterRule.class.getName() + " filter_rule";
            Query query = entityManager.createQuery("SELECT f FROM FilterRule f WHERE f.stateId = :stateId"
                +" AND f.departmentId = :deptId");
            query.setParameter("stateId", filterRule.getStateId());
            query.setParameter("deptId", filterRule.getDepartmentId());
            List<FilterRule> filterRules = query.getResultList();
            return filterRules;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
