package fundpkg;

import fundpkg.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-11-05T01:11:50")
@StaticMetamodel(Role.class)
public class Role_ { 

    public static volatile CollectionAttribute<Role, Users> usersCollection;
    public static volatile SingularAttribute<Role, String> roleType;
    public static volatile SingularAttribute<Role, Integer> roleId;

}