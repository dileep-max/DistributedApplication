/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkgfacade;

import fundpkg.State;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Dileep Kumar
 */
@Local
public interface StateFacadeLocal {

    void create(State state);

    void edit(State state);

    void remove(State state);

    State find(Object id);

    List<State> findAll();

    List<State> findRange(int[] range);

    int count();
    
}
