/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fund.business;

import com.fund.entity.FilterRule;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author sizzler
 */
@Remote
public interface FilterRuleEntity {
    public void processRequest(int stateId, int departmentId, double requestedFundAmount);
    public List<FilterRule> getFilterRules(FilterRule filterRule);
}
