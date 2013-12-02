package fundpkg;

import fundpkg.FilterRule;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-11-05T21:42:14")
@StaticMetamodel(Department.class)
public class Department_ { 

    public static volatile CollectionAttribute<Department, FilterRule> filterRuleCollection;
    public static volatile SingularAttribute<Department, Integer> departmentId;
    public static volatile SingularAttribute<Department, String> departmentCode;
    public static volatile SingularAttribute<Department, String> departmentName;

}