package dal;

import entity.Person;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alotf
 */
public class PersonDAL extends GenericDAL<Person> {
    
     public PersonDAL() {
        super( Person.class );
    }

    @Override
    public List<Person> findAll() {
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        return findResults( "Person.findAll", null );
    }

    @Override
    public Person findById( int id ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "id", id );
        return findResult( "Person.findById", map );
    }

    public List<Person> findByFirstName( String firstName ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "firstName", firstName );
        return findResults( "Person.findByFirstName", map );
    }

    public List<Person> findByLastName( String lastName ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "lastName", lastName );
        return findResults( "Person.findByLastName", map );
    }

    public List<Person> findByPhone( String phone ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "phone", phone );
        return findResults( "Person.findByPhone", map );
    }

    public List<Person> findByAddress( String address ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "address", address );
        return findResults( "Person.findByAddress", map );
    }

    public List<Person> findByBirth( Date birth ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "birth", birth );
        return findResults( "Person.findByBirth", map );
    }

}
