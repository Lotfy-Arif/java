package logic;

import common.ValidationException;
import dal.BloodBankDAL;
import entity.BloodBank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Khalid
 */
public class BloodBankLogic extends GenericLogic<BloodBank, BloodBankDAL> {
    
    public static final String OWNER_ID = "owner_id";
    public static final String PRIVATELY_OWNED = "privately_owned";
    public static final String ESTABLISHED = "established";
    public static final String NAME = "name";
    public static final String EMPLOYEE_COUNT = "employee_count";
    public static final String ID = "id";
    
    BloodBankLogic() {
        super(new BloodBankDAL());
    }
    
    @Override
    public List<BloodBank> getAll() {
        return get( () -> dal().findAll() );
    }
    
    @Override
    public BloodBank getWithId(int id) {
        return get( () -> dal().findById(id) );
    }
    
    public BloodBank getBloodBankWithName(String name) {
        return get( () -> dal().findByName(name) );
    }
    
    public List<BloodBank> getBloodBankWithPrivatelyOwned(boolean privatelyOwned) {
        return get( () -> dal().findByPrivatelyOwned(privatelyOwned) );
    }
    
    public List<BloodBank> getBloodBankWithEstablished(Date established) {
        return get( () -> dal().findByEstablished(established) );
    }
    
    public BloodBank getBloodBankWithOwner(int ownerID) {
        return get( () -> dal().findByOwner(ownerID) );
    }
    
    public List<BloodBank> getBloodBankWithEmployeeCount(int count) {
        return get( () -> dal().findByEmplyeeCount(count) );
    }
    
    
    
    @Override
    public BloodBank createEntity(Map<String, String[]> parameterMap) {
        
        Objects.requireNonNull( parameterMap, "parameterMap cannot be null" );
        
        //Creating Entity Object.
        BloodBank entity = new BloodBank();
        
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
        
//        int owner_Id = Integer.valueOf(parameterMap.get( OWNER_ID )[0]);
        String name = parameterMap.get( NAME )[ 0 ];
        int employee_Count = Integer.valueOf(parameterMap.get( EMPLOYEE_COUNT )[0]);
        
        Date established = null;
        try {
            established = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(parameterMap.get( ESTABLISHED )[0]);
        } catch (ParseException ex) {
            Logger.getLogger(BloodBankLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        Date established = this.convertStringToDate(parameterMap.get( ESTABLISHED )[0]);
        boolean privately_Owned = Boolean.parseBoolean(parameterMap.get( PRIVATELY_OWNED )[0]);
        
        
        //validate the data
        validator.accept( name, 100 );
        
        
        //set values on entity
        entity.setName( name );
//        entity.setId( owner_Id );
        entity.setEmplyeeCount( employee_Count );
        entity.setEstablished( established );
        entity.setPrivatelyOwned( privately_Owned );
        
        return entity;
        
    }//end of createEntity
    
    
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("owner_id", "privately_owned", "established", "name", "employee_count", "id");
    }
    
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(OWNER_ID, PRIVATELY_OWNED, ESTABLISHED, NAME, EMPLOYEE_COUNT, ID);
    }
    
    @Override
    public List<?> extractDataAsList(BloodBank e) {
        return Arrays.asList(e.getOwner(), e.getPrivatelyOwned(), e.getEstablished(), e.getName(), e.getEmplyeeCount(), e.getId());
    }
    
    
}//end of BloodBankLogic class.
