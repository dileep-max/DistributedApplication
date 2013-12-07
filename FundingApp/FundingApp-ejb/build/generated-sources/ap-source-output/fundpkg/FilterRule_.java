package fundpkg;

import fundpkg.Department;
import fundpkg.State;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-12-07T00:39:18")
@StaticMetamodel(FilterRule.class)
public class FilterRule_ { 

    public static volatile SingularAttribute<FilterRule, Integer> filterId;
    public static volatile SingularAttribute<FilterRule, Integer> ruleValue;
    public static volatile SingularAttribute<FilterRule, State> stateId;
    public static volatile SingularAttribute<FilterRule, String> ruleCode;
    public static volatile SingularAttribute<FilterRule, Department> departmentId;

}