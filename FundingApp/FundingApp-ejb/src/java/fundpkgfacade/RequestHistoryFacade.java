/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundpkgfacade;

import fundpkg.RequestHistory;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dileep Kumar
 */
@Stateless
public class RequestHistoryFacade extends AbstractFacade<RequestHistory> implements RequestHistoryFacadeLocal {
    @PersistenceContext(unitName = "FundingApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RequestHistoryFacade() {
        super(RequestHistory.class);
    }
    
}
