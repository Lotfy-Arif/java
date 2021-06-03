package logic;

import common.ValidationException;
import dal.PersonDAL;
import entity.BloodBank;
import entity.Person;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
/**
 *
 * @author alotf
 */
public class PersonLogic extends GenericLogic<Person,PersonDAL> {
    
    public static final String ID = "id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String BIRTH = "birth";
   
    
    PersonLogic() {
        super( new PersonDAL());
    }

    @Override
    public List<Person> getAll() {
        return get( () -> dal().findAll() );
    }

    @Override
    public Person getWithId( int id ) {
        return get( () -> dal().findById( id ) );
    }

    public List<Person> getPersonWithPhone( String phone ) {
        return get( () -> dal().findByPhone( phone ) );
    }

    public List<Person> getPersonWithFirstName( String firstname ) {
        return get( () -> dal().findByFirstName( firstname ) );
    }

    public List<Person> getPersonWithLastName( String lastname ) {
        return get( () -> dal().findByLastName( lastname ) );
    }

    public List<Person> getPersonsWithAddress( String address ) {
        return get( () -> dal().findByAddress(address) );
    }
    
    public List<Person> getPersonsWithBirth(Date birth) {
        return get( () -> dal().findByBirth(birth) );
    }
    
    @Override
    public Person createEntity( Map<String, String[]> parameterMap ) {
        //do not create any logic classes in this method.

//        return new AccountBuilder().SetData( parameterMap ).build();
        Objects.requireNonNull( parameterMap, "parameterMap cannot be null" );
        //same as if condition below
//        if (parameterMap == null) {
//            throw new NullPointerException("parameterMap cannot be null");
//        }

        //create a new Entity object
        Person entity = new Person();

        //ID is generated, so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
        //the only time that we will have id is for update behaviour.
        if( parameterMap.containsKey( ID ) ){
            try {
                entity.setId( Integer.parseInt( parameterMap.get( ID )[ 0 ] ) );
            } catch( java.lang.NumberFormatException ex ) {
                throw new ValidationException( ex );
            }
        }

        //before using the values in the map, make sure to do error checking.
        //simple lambda to validate a string, this can also be place in another
        //method to be shared amoung all logic classes.
        ObjIntConsumer< String> validator = ( value, length ) -> {
            if( value == null || value.trim().isEmpty() || value.length() > length ){
                String error = "";
                if( value == null || value.trim().isEmpty() ){
                    error = "value cannot be null or empty: " + value;
                }
                if( value.length() > length ){
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException( error );
            }
        };

        //extract the date from map first.
        //everything in the parameterMap is string so it must first be
        //converted to appropriate type. have in mind that values are
        //stored in an array of String; almost always the value is at
        //index zero unless you have used duplicated key/name somewhere.      
//        String id = parameterMap.get( ID )[ 0 ];
        String firstname = parameterMap.get( FIRST_NAME )[ 0 ];
        String lastname = parameterMap.get( LAST_NAME )[ 0 ];
        String phone = parameterMap.get( PHONE )[ 0 ];
        String address = parameterMap.get( ADDRESS )[ 0 ];
        String birth = parameterMap.get( BIRTH )[ 0 ];
        
        //validate the data
              
        //validator.accept( ID,100 );       
        validator.accept( FIRST_NAME, 50 );
        validator.accept( LAST_NAME, 50 );
        validator.accept( PHONE, 15 );
        validator.accept( ADDRESS, 100 );
        
        

        //set values on entity
        //entity.setId(Integer.parseInt(id));
        entity.setFirstName( firstname );
        entity.setLastName( lastname );
        entity.setPhone( phone );
        entity.setAddress( address );    
        birth=birth.replace("T", " ");
        entity.setBirth(convertStringToDate(birth));
        //entity.setBloodBank( bloodBank );

        return entity;
    }

    /**
     * this method is used to send a list of all names to be used form table column headers. by having all names in one
     * location there is less chance of mistakes.
     *
     * this list must be in the same order as getColumnCodes and extractDataAsList
     *
     * @return list of all column names to be displayed.
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList( "id", "firstName", "lastName", "phone","address","birth" );
    }

    /**
     * this method returns a list of column names that match the official column names in the db. by having all names in
     * one location there is less chance of mistakes.
     *
     * this list must be in the same order as getColumnNames and extractDataAsList
     *
     * @return list of all column names in DB.
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList( ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS,BIRTH );
    }

    /**
     * return the list of values of all columns (variables) in given entity.
     *
     * this list must be in the same order as getColumnNames and getColumnCodes
     *
     * @param e - given Entity to extract data from.
     *
     * @return list of extracted values
     */
    @Override
    public List<?> extractDataAsList( Person e ) {
        return Arrays.asList( e.getId(), e.getFirstName(), e.getLastName(), e.getPhone(), e.getAddress(),convertDateToString(e.getBirth()));
    }
}
