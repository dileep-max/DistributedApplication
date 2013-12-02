/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fund.business;

import com.fund.entity.Department;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author sizzler
 */
@Remote
public interface DepartmentEntity {
    public List<Department> getAllDepartments();
}
