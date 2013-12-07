package fundpkg;

import fundpkg.FilterRule;
import fundpkg.RequestHistory;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-12-07T00:39:18")
@StaticMetamodel(State.class)
public class State_ { 

    public static volatile CollectionAttribute<State, FilterRule> filterRuleCollection;
    public static volatile CollectionAttribute<State, RequestHistory> requestHistoryCollection;
    public static volatile SingularAttribute<State, String> stateName;
    public static volatile SingularAttribute<State, Integer> stateCode;

}