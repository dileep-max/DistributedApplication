/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkgfacade;

import fundpkg.AllocationRule;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Dileep Kumar
 */
@Local
public interface AllocationRuleFacadeLocal {

    void create(AllocationRule allocationRule);

    void edit(AllocationRule allocationRule);

    void remove(AllocationRule allocationRule);

    AllocationRule find(Object id);

    List<AllocationRule> findAll();

    List<AllocationRule> findRange(int[] range);

    int count();
    
}
