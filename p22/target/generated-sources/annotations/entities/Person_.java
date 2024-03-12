package entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2024-03-12T09:10:29")
@StaticMetamodel(Person.class)
public class Person_ { 

    public static volatile SingularAttribute<Person, Date> born;
    public static volatile SingularAttribute<Person, String> name;
    public static volatile SingularAttribute<Person, Long> id;
    public static volatile SingularAttribute<Person, Double> salary;
    public static volatile SingularAttribute<Person, Boolean> married;

}