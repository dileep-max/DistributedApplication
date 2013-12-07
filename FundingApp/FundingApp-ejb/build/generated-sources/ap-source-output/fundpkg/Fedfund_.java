package fundpkg;

import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-12-07T00:39:18")
@StaticMetamodel(Fedfund.class)
public class Fedfund_ { 

    public static volatile SingularAttribute<Fedfund, Integer> fundId;
    public static volatile SingularAttribute<Fedfund, String> fundDesignation;
    public static volatile SingularAttribute<Fedfund, BigInteger> fundAmount;

}