package logic;

import common.ValidationException;
import dal.DonationRecordDAL;
import entity.BloodDonation;
import entity.DonationRecord;
import entity.Person;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wontaek Oh
 */
public class DonationRecordLogic extends GenericLogic<DonationRecord, DonationRecordDAL>{
    
    public static final String PERSON_ID = "person_id";
    public static final String DONATION_ID = "donation_id";
    public static final String TESTED = "tested";
    public static final String ADMINISTRATOR = "administrator";
    public static final String HOSPITAL = "hospital";
    public static final String CREATED = "created";
    public static final String ID = "record_id";
    
    DonationRecordLogic() {
        super(new DonationRecordDAL());
    }
    
    @Override
    public List<DonationRecord> getAll() {
        return get( () -> dal().findAll() );
    }

    @Override
    public DonationRecord getWithId(int id) {
        return get( () -> dal().findById(id) );
    }
    
    public List<DonationRecord> getDonationRecordWithTested(boolean tested) {
        return get( () -> dal().findByTested(tested) );
    }
    
    public List<DonationRecord> getDonationRecordWithAdministrator(String administrator) {
        return get( () -> dal().findByAdministrator(administrator) );
    }
    
    public List<DonationRecord> getDonationRecordWithHospital(String hospital) {
        return get( () -> dal().findByHospital(hospital) );
    }
    
    public List<DonationRecord> getDonationRecordWithCreated(Date created) {
        return get( () -> dal().findByCreated(created) );
    }
    
    public List<DonationRecord> getDonationRecordWithPerson(int personId) {
        return get( () -> dal().findByPerson(personId) );
    }
    
    public List<DonationRecord> getDonationRecordWithDonation(int donationId) {
        return get( () -> dal().findByDonation(donationId) );
    }
    
    
    @Override
    public DonationRecord createEntity(Map<String, String[]> parameterMap) {

        Objects.requireNonNull( parameterMap, "parameterMap cannot be null" );

        //create a new Entity object
        DonationRecord entity = new DonationRecord();

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

        boolean tested = Boolean.parseBoolean(parameterMap.get( TESTED )[0]);
        String admin = parameterMap.get( ADMINISTRATOR )[0];
        String hospital = parameterMap.get( HOSPITAL )[0];
        
        Date created = null;
        try {
            created = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(parameterMap.get( CREATED )[0]);
        } catch (ParseException ex) {
            Logger.getLogger(DonationRecordLogic.class.getName()).log(Level.SEVERE, null, ex);
        }

        //validate the data
        validator.accept( admin, 100 );
        validator.accept( hospital, 100 );

        //set values on entity
        entity.setTested( tested );
        entity.setAdministrator( admin );
        entity.setHospital( hospital );
        entity.setCreated( created );

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
        return Arrays.asList("record_id", "person_id", "donation_id", "tested", "administrator", "hospital", "created");
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
        return Arrays.asList(ID, PERSON_ID, DONATION_ID, TESTED, ADMINISTRATOR, HOSPITAL, CREATED);
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
    public List<?> extractDataAsList(DonationRecord e) {
        return Arrays.asList(e.getId(), e.getPerson(), e.getBloodDonation(), e.getTested(), e.getAdministrator(), e.getHospital(), e.getCreated());
    }

}
