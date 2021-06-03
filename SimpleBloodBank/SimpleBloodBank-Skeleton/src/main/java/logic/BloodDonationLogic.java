package logic;

import common.ValidationException;
import dal.BloodDonationDAL;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.function.ObjIntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chang Liu
 */
public class BloodDonationLogic extends GenericLogic<BloodDonation, BloodDonationDAL> {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat( "yyyy-MM-dd kk:mm:ss" );
    public static final String BANK_ID = "bank_id";
    public static final String MILLILITERS = "milliliters";
    public static final String BLOOD_GROUP = "blood_group";
    public static final String RHESUS_FACTOR = "rhesus_factor";
    public static final String CREATED = "created";
    public static final String ID = "id";

    BloodDonationLogic() {

        super(new BloodDonationDAL());
    }


    @Override
    public List<BloodDonation> getAll() {
        return get(() -> dal().findAll());
    }


    @Override
    public BloodDonation getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    public List<BloodDonation> getBloodDonationWithMilliliters(int milliliters) {
        return get(() -> dal().findByMilliliters(milliliters));
    }


    public List<BloodDonation> getBloodDonationWithBloodGroup(BloodGroup bloodGroup) {
        return get(() -> dal().findByBloodGroup(bloodGroup));
    }

    public List<BloodDonation> getBloodDonationWithCreated(Date created) {
        return get(() -> dal().findByCreated(created));
    }

    public List<BloodDonation> getBloodDonationWithRhd(RhesusFactor rhd) {
        return get(() -> dal().findByRhd(rhd));
    }

    public List<BloodDonation> getBloodDonationWithBloodBank(int bankId) {
        return get(() -> dal().findByBloodBank(bankId));
    }


    //  create entity

    @Override
    public BloodDonation createEntity(Map<String, String[]> parameterMap) {
        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");

        //create a new Entity object
        BloodDonation entity = new BloodDonation();

        if (parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }

        //before using the values in the map, make sure to do error checking.
        //simple lambda to validate a string, this can also be place in another
        //method to be shared amoung all logic classes.
        ObjIntConsumer<String> validator = (value, length) -> {
            
            if (value == null || value.trim().isEmpty() || value.length() > length) {
                String error = "";
                if (value == null || value.trim().isEmpty()) {
                    error = "value cannot be null or empty: " + value;
                }
                if (value.length() > length) {
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException(error);
            }
        };

        //extract the date from map first.
        //everything in the parameterMap is string so it must first be
        //converted to appropriate type. have in mind that values are
        //stored in an array of String; almost always the value is at
        //index zero unless you have used duplicated key/name somewhere.
 

        String createdDate = parameterMap.get(CREATED)[0].replace("T", " ");

        int milliliters = Integer.valueOf(parameterMap.get(MILLILITERS)[0]);    
        BloodGroup bloodGroup = BloodGroup.valueOf(parameterMap.get(BLOOD_GROUP)[0]);       
        RhesusFactor rhd = RhesusFactor.getRhesusFactor(parameterMap.get(RHESUS_FACTOR)[0]);


        //set values on entity
 
        entity.setCreated(convertStringToDate(createdDate));
        entity.setMilliliters(milliliters);
        entity.setBloodGroup(bloodGroup);
        entity.setRhd(rhd);


        return entity;
    }


    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID","BANK_ID", "MILLILITERS", "BLOOD_GROUP", "RHD", "CREATED_DATE");
 
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, BANK_ID,MILLILITERS, BLOOD_GROUP, RHESUS_FACTOR, CREATED );
 
    }

    @Override
    public List<?> extractDataAsList(BloodDonation bloodDonation) {
        return Arrays.asList(bloodDonation.getId(),bloodDonation.getBloodBank(),bloodDonation.getMilliliters(),
                bloodDonation.getBloodGroup(), bloodDonation.getRhd(), bloodDonation.getCreated());
    }
    
        @Override
    public Date convertStringToDate( String date ) {
        try {
            return FORMATTER.parse( date );
        } catch( ParseException ex ) {
            Logger.getLogger( GenericLogic.class.getName() ).log( Level.SEVERE, null, ex );
            throw new ValidationException( "failed to format String=\"" + date + "\" to a date object", ex );
        }
    }


}