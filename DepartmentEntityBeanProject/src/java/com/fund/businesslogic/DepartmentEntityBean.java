/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fund.businesslogic;

import com.fund.business.DepartmentEntity;
import com.fund.entity.Department;
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
public class DepartmentEntityBean implements DepartmentEntity {    
    @PersistenceContext (unitName = "DeptPU")
    private EntityManager entityManager;
    
    @Override
    public List<Department> getAllDepartments() {
        try{
            String queryString = "SELECT department from " + Department.class.getName() + " department";
            Query query = entityManager.createQuery(queryString);
            List<Department> users = query.getResultList();
            return users;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
