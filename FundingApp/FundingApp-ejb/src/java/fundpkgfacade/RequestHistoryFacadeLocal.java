/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkgfacade;

import fundpkg.RequestHistory;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Dileep Kumar
 */
@Local
public interface RequestHistoryFacadeLocal {

    void create(RequestHistory requestHistory);

    void edit(RequestHistory requestHistory);

    void remove(RequestHistory requestHistory);

    RequestHistory find(Object id);

    List<RequestHistory> findAll();

    List<RequestHistory> findRange(int[] range);

    int count();
    
}
