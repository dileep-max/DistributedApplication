package fundpkg;

import fundpkg.RequestHistory;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-12-07T00:39:18")
@StaticMetamodel(Status.class)
public class Status_ { 

    public static volatile SingularAttribute<Status, Integer> statusId;
    public static volatile CollectionAttribute<Status, RequestHistory> requestHistoryCollection;
    public static volatile SingularAttribute<Status, String> statusValue;

}