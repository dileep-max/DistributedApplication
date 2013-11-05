/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkgfacade;

import fundpkg.Status;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Dileep Kumar
 */
@Local
public interface StatusFacadeLocal {

    void create(Status status);

    void edit(Status status);

    void remove(Status status);

    Status find(Object id);

    List<Status> findAll();

    List<Status> findRange(int[] range);

    int count();
    
}
