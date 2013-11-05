package fundpkg;

import fundpkg.State;
import fundpkg.Status;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-11-05T01:11:50")
@StaticMetamodel(RequestHistory.class)
public class RequestHistory_ { 

    public static volatile SingularAttribute<RequestHistory, Double> allocatedFund;
    public static volatile SingularAttribute<RequestHistory, Status> statusId;
    public static volatile SingularAttribute<RequestHistory, Integer> requestId;
    public static volatile SingularAttribute<RequestHistory, State> stateId;
    public static volatile SingularAttribute<RequestHistory, Double> requestedFund;
    public static volatile SingularAttribute<RequestHistory, Integer> departmentId;

}