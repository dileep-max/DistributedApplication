/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkgfacade;

import fundpkg.FilterRule;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Dileep Kumar
 */
@Local
public interface FilterRuleFacadeLocal {

    void create(FilterRule filterRule);

    void edit(FilterRule filterRule);

    void remove(FilterRule filterRule);

    FilterRule find(Object id);

    List<FilterRule> findAll();

    List<FilterRule> findRange(int[] range);

    int count();
    
}
