/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkgfacade;

import fundpkg.Fedfund;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Dileep Kumar
 */
@Local
public interface FedfundFacadeLocal {

    void create(Fedfund fedfund);

    void edit(Fedfund fedfund);

    void remove(Fedfund fedfund);

    Fedfund find(Object id);

    List<Fedfund> findAll();

    List<Fedfund> findRange(int[] range);

    int count();
    
}
