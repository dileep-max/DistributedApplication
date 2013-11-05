/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkgfacade;

import fundpkg.Fedfund;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dileep Kumar
 */
@Stateless
public class FedfundFacade extends AbstractFacade<Fedfund> implements FedfundFacadeLocal {
    @PersistenceContext(unitName = "FundingApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FedfundFacade() {
        super(Fedfund.class);
    }
    
}
