/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkgfacade;

import fundpkg.Status;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dileep Kumar
 */
@Stateless
public class StatusFacade extends AbstractFacade<Status> implements StatusFacadeLocal {
    @PersistenceContext(unitName = "FundingApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StatusFacade() {
        super(Status.class);
    }
    
}
