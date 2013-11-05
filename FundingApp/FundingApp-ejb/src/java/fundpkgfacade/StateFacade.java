/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkgfacade;

import fundpkg.State;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dileep Kumar
 */
@Stateless
public class StateFacade extends AbstractFacade<State> implements StateFacadeLocal {
    @PersistenceContext(unitName = "FundingApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StateFacade() {
        super(State.class);
    }
    
}
