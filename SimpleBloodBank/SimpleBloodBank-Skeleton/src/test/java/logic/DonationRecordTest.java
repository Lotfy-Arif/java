package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.DonationRecord;
import entity.Person;
import entity.RhesusFactor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

/**
 *
 * @author Wontaek Oh
 */
@Disabled
public class DonationRecordTest {
    
    private DonationRecord expectedEntity;
    private DonationRecord expectedEntity2;
    private DonationRecordLogic logic;
    private static Object TomcatStartUp;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp TomcatStartUp= new TomcatStartUp();
        TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );

    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp TomcatStartUp= new TomcatStartUp();
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {

        logic = LogicFactory.getFor( "DonationRecord" );
        /* **********************************
         * ***********IMPORTANT**************
         * **********************************/
        //we only do this for the test.
        //always create Entity using logic.
        //we manually make the account to not rely on any logic functionality , just for testing

        //get an instance of EntityManager
        EntityManager em1 = EMFactory.getEMF().createEntityManager();
        EntityManager em2 = EMFactory.getEMF().createEntityManager();
        //start a Transaction
        em1.getTransaction().begin();
        em2.getTransaction().begin();
        
        //check if the depdendecy exists on DB already
        //em.find takes two arguments, the class type of return result and the primery key.
        Person person = em1.find( Person.class, 1 );
        //if result is null create the entity and persist it
        if( person == null ){
            //cearet object
            person = new Person();
            person.setFirstName("firstname");
            person.setLastName("lastname");
            person.setPhone("4445556666");
            person.setAddress("address");
            person.setBirth(logic.convertStringToDate( "1111-11-11 11:11:11" ) );
            //persist the dependency first
            em1.persist( person );
        }
        
        BloodDonation bDonation = em2.find( BloodDonation.class, 1 );
        //if result is null create the entity and persist it
        if( bDonation == null ){
            //cearet object
            bDonation = new BloodDonation();
            bDonation.setMilliliters(100);
            bDonation.setBloodGroup(BloodGroup.B);
            bDonation.setRhd(RhesusFactor.Positive);
            bDonation.setCreated(logic.convertStringToDate( "1111-11-11 11:11:11" ) );
            //persist the dependency first
            em2.persist( bDonation );
        }
        

        //create the desired entity
        DonationRecord entity1 = new DonationRecord();
        entity1.setTested(true);
        entity1.setAdministrator("admin");
        entity1.setHospital("hospital");
        entity1.setCreated(logic.convertStringToDate( "1111-11-11 11:11:11" ) );
        //add dependency to the desired entity
        entity1.setPerson(person);
        
        //create the desired entity
        DonationRecord entity2 = new DonationRecord();
        entity2.setTested(true);
        entity2.setAdministrator("admin");
        entity2.setHospital("hospital");
        entity2.setCreated(logic.convertStringToDate( "1111-11-11 11:11:11" ) );
        //add dependency to the desired entity
        entity2.setBloodDonation(bDonation);

        //add desired entity to hibernate, entity is now managed.
        //we use merge instead of add so we can get the managed entity.
        expectedEntity = em1.merge( entity1 );
        //commit the changes
        em1.getTransaction().commit();
        //close EntityManager
        em1.close();
        
        //add desired entity to hibernate, entity is now managed.
        //we use merge instead of add so we can get the managed entity.
        expectedEntity2 = em2.merge( entity2 );
        //commit the changes
        em2.getTransaction().commit();
        //close EntityManager
        em2.close();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if( expectedEntity != null ){
            logic.delete( expectedEntity );
        }
    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<DonationRecord> list = logic.getAll();
        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();

        //make sure account was created successfully
        assertNotNull( expectedEntity );
        //delete the new account
        logic.delete( expectedEntity );

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be one less
        assertEquals( originalSize - 1, list.size() );
    }


     /**
     * helper method for testing all bloodDonation fields
     *
     * @param expected
     * @param actual
     */
    private void assertDonationRecordEquals(DonationRecord expected, DonationRecord actual) {
        //  call assertDonationRecordEqualsBasic to compare 
        assertDonationRecordEqualsBasic(expected, actual, true);
    }

    /**
     * helper method for testing all account fields
     *
     * @param expected
     * @param actual
     */
    private void assertDonationRecordEqualsBasic(DonationRecord expected, DonationRecord actual, boolean dependency) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        if (dependency) {
            assertEquals(expected.getPerson().getId(), actual.getPerson().getId());
            assertEquals(expected.getBloodDonation().getId(), actual.getBloodDonation().getId());
        }  
   
        assertEquals(expected.getTested(), actual.getTested());
        assertEquals(expected.getAdministrator(), actual.getAdministrator());
        assertEquals(expected.getHospital(), actual.getHospital());
        assertEquals(expected.getCreated(), actual.getCreated());
    }

    
    @Test
    final void testGetWithId() {
        //using the id of test account get another account from logic
        DonationRecord returnedRecord = logic.getWithId( expectedEntity.getId() );

        //the two accounts (testAcounts and returnedAccounts) must be the same
        assertDonationRecordEquals( expectedEntity, returnedRecord);
    }
    

    
    @Test
    final void testGetBloodDonationWithTested() {
        List<DonationRecord> returnedRecords = logic.getDonationRecordWithTested(expectedEntity.getTested());
        
        //the two accounts (testAcounts and returnedAccounts) must be the same
        for (DonationRecord record: returnedRecords) {
            //all accounts must have the same password
            assertEquals( expectedEntity.getTested(), record.getTested());
        }
    }
 
            
    @Test
    final void testGetDonationRecordWithAdministrator() {
        List<DonationRecord> returnedRecords = logic.getDonationRecordWithAdministrator(expectedEntity.getAdministrator());
        
        //the two accounts (testAcounts and returnedAccounts) must be the same
        for (DonationRecord record: returnedRecords) {
            //all accounts must have the same password
            assertEquals( expectedEntity.getAdministrator(), record.getAdministrator());
        }
    }
    
    @Test
    final void testGetDonationRecordWithHospital() {
        List<DonationRecord> returnedRecords = logic.getDonationRecordWithHospital(expectedEntity.getHospital());
        
        //the two accounts (testAcounts and returnedAccounts) must be the same
        for (DonationRecord record: returnedRecords) {
            //all accounts must have the same password
            assertEquals( expectedEntity.getHospital(), record.getHospital());
        }
    }

    @Test
    final void testGetDonationRecordWithCreated() {
        List<DonationRecord> returnedRecords = logic.getDonationRecordWithCreated(expectedEntity.getCreated());
        
        //the two accounts (testAcounts and returnedAccounts) must be the same
        for (DonationRecord record: returnedRecords) {
            //all accounts must have the same password
            assertEquals( expectedEntity.getCreated(), record.getCreated());
        }
    }
    
    
    @Test
    final void testGetDonationRecordWithPerson() {
        List<DonationRecord> returnedRecords = logic.getDonationRecordWithPerson(expectedEntity.getPerson().getId());

        //the two accounts (testAcounts and returnedAccounts) must be the same
        for (DonationRecord record : returnedRecords) {
            //all accounts must have the same password
            assertEquals(expectedEntity.getPerson(), record.getPerson());
        }
    }
    
    @Test
    final void testGetDonationRecordWithBloodDonation() {
        List<DonationRecord> returnedRecords = logic.getDonationRecordWithDonation(expectedEntity2.getBloodDonation().getId());

        //the two accounts (testAcounts and returnedAccounts) must be the same
        for (DonationRecord record : returnedRecords) {
            //all accounts must have the same password
            assertEquals(expectedEntity2.getBloodDonation(), record.getBloodDonation());
        }
    }
    
    
    
    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
           assertEquals(Arrays.asList("ID", "PERSON_ID", "DONATION_ID", "TESTED", "ADMINISTRATOR", "HOSPITAL", "CREATED"), list);
    }
 

    
    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(DonationRecordLogic.ID, DonationRecordLogic.PERSON_ID, DonationRecordLogic.DONATION_ID,
                DonationRecordLogic.TESTED, DonationRecordLogic.ADMINISTRATOR, DonationRecordLogic.HOSPITAL, DonationRecordLogic.CREATED), list);
    }


    @Test
    // normal case
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList(expectedEntity);
        assertEquals(expectedEntity.getId(), list.get(0));
        assertEquals(expectedEntity.getPerson(), list.get(1));
        assertEquals(expectedEntity.getBloodDonation(), list.get(2));
        assertEquals(expectedEntity.getTested(), list.get(3));
        assertEquals(expectedEntity.getAdministrator(), list.get(4));
        assertEquals(expectedEntity.getHospital(), list.get(5));
        assertEquals(expectedEntity.getCreated(), list.get(6));
    }

    @Test
    // edge / error case
    final void testExtractDataAsListOutofBound() {
        List<?> list = logic.extractDataAsList(expectedEntity);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(8));
    }
    
    
    @Test
    final void testCreateEntity() {
        Map<String, String[]> testMap = new HashMap<>();
        testMap.put( DonationRecordLogic.ID, new String[]{ Integer.toString(expectedEntity.getId() ) } );
        testMap.put( DonationRecordLogic.HOSPITAL, new String[]{String.valueOf(expectedEntity.getHospital()) } );
        testMap.put( DonationRecordLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});
        testMap.put( DonationRecordLogic.TESTED, new String[]{ Boolean.toString(expectedEntity.getTested() ) } );
        testMap.put( DonationRecordLogic.ADMINISTRATOR, new String[]{String.valueOf(expectedEntity.getAdministrator())} );

        DonationRecord returnedRecord= logic.createEntity( testMap );
        assertDonationRecordEqualsBasic(expectedEntity, returnedRecord, false);
    }
 
    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( DonationRecordLogic.ADMINISTRATOR, new String[]{ "Admin" } );
        sampleMap.put( DonationRecordLogic.TESTED, new String[]{ "true" } );
        sampleMap.put( DonationRecordLogic.HOSPITAL, new String[]{ "hospital"} );
        sampleMap.put( DonationRecordLogic.CREATED, new String[]{ "2000-04-01 12:12:12.0" } );

        DonationRecord returnedRecord = logic.createEntity( sampleMap );
        logic.add( returnedRecord );
        returnedRecord = logic.getWithId(returnedRecord.getId());

        assertEquals( sampleMap.get( DonationRecordLogic.ADMINISTRATOR )[ 0 ], returnedRecord.getAdministrator() );
        assertEquals( sampleMap.get( DonationRecordLogic.CREATED )[ 0 ], returnedRecord.getCreated().toString() ); //use the method convertDateToString from logic
        assertEquals( sampleMap.get( DonationRecordLogic.TESTED )[ 0 ], String.valueOf(returnedRecord.getTested()));
        assertEquals( sampleMap.get( DonationRecordLogic.HOSPITAL )[ 0 ], returnedRecord.getHospital());

        logic.delete( returnedRecord );
    }
    
    
    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> testMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String,String[]>map) -> {
            map.clear();
            testMap.put( DonationRecordLogic.ID, new String[]{ Integer.toString(expectedEntity.getId() ) } );
            testMap.put( DonationRecordLogic.TESTED, new String[]{ Boolean.toString(expectedEntity.getTested() ) } );
            testMap.put( DonationRecordLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});
            testMap.put( DonationRecordLogic.ADMINISTRATOR, new String[]{String.valueOf(expectedEntity.getAdministrator()) } );
            testMap.put( DonationRecordLogic.HOSPITAL, new String[]{String.valueOf(expectedEntity.getHospital())} );
        };
        
        fillMap.accept(testMap);
        testMap.replace(DonationRecordLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(testMap));
        testMap.replace(DonationRecordLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(testMap));

        testMap.replace(DonationRecordLogic.ADMINISTRATOR, null);
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(testMap));

        testMap.replace(DonationRecordLogic.TESTED, null);
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(testMap));

        testMap.replace(DonationRecordLogic.HOSPITAL, null);
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(testMap));

        testMap.replace(DonationRecordLogic.CREATED, null);
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(testMap));

    }
    
    
    
        @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> testMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put( DonationRecordLogic.ID, new String[]{ Integer.toString( expectedEntity.getId() ) } );
            map.put( DonationRecordLogic.ADMINISTRATOR, new String[]{String.valueOf(expectedEntity.getAdministrator()) } );
            testMap.put( DonationRecordLogic.TESTED, new String[]{ Boolean.toString(expectedEntity.getTested() ) } );
            testMap.put( DonationRecordLogic.HOSPITAL, new String[]{String.valueOf(expectedEntity.getHospital())} );
            testMap.put( DonationRecordLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});
        };

        IntFunction<String> generateString = (int length ) -> {
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append ).toString();
        };

        //idealy every test should be in its own method
        fillMap.accept( testMap );

        testMap.replace( DonationRecordLogic.ID, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( testMap ) );
        testMap.replace( DonationRecordLogic.ID, new String[]{ "1fs" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( testMap ) );

        testMap.replace(DonationRecordLogic.TESTED, new String[]{generateString.apply(1)});
        assertThrows(ValidationException.class, () -> logic.createEntity(testMap));


        testMap.replace(DonationRecordLogic.CREATED, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(testMap));
        fillMap.accept(testMap);
        testMap.replace(DonationRecordLogic.CREATED, new String[]{"1122-11-112af0:11a"});
        assertThrows(ValidationException.class, () -> logic.createEntity(testMap));
    }
    
    
   @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = ( int length ) -> {
          
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };

        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( DonationRecordLogic.ID, new String[]{ Integer.toString( 1 ) } );
        sampleMap.put( DonationRecordLogic.ADMINISTRATOR, new String[]{"admin" } );
        sampleMap.put( DonationRecordLogic.TESTED, new String[]{ "true" } );
        sampleMap.put( DonationRecordLogic.HOSPITAL, new String[]{"hospital"} );
        sampleMap.put( DonationRecordLogic.CREATED, new String[]{"0001-01-01 01:00:00"});

        //ideally every test should be in its own method
        DonationRecord returnedRecord = logic.createEntity( sampleMap );
        assertEquals(Integer.parseInt(sampleMap.get(DonationRecordLogic .ID)[0]), returnedRecord.getId());
        assertEquals( sampleMap.get( DonationRecordLogic.ADMINISTRATOR )[ 0 ], returnedRecord.getAdministrator() );
        assertEquals( sampleMap.get( DonationRecordLogic.TESTED )[0],  String.valueOf(returnedRecord.getTested() ));
        assertEquals( sampleMap.get( DonationRecordLogic.HOSPITAL )[ 0 ], returnedRecord.getHospital());
        assertEquals( sampleMap.get( DonationRecordLogic.CREATED )[0],logic.convertDateToString(returnedRecord.getCreated()));

        sampleMap = new HashMap<>();
        sampleMap.put( DonationRecordLogic.ID, new String[]{ Integer.toString( 1 ) } );
        sampleMap.put( DonationRecordLogic.ADMINISTRATOR, new String[]{"Admin" } );
        sampleMap.put( DonationRecordLogic.TESTED, new String[]{ "true" } );
        sampleMap.put( DonationRecordLogic.HOSPITAL, new String[]{"hospital"} );
        sampleMap.put( DonationRecordLogic.CREATED, new String[]{"3123-12-24 12:12:12"});

        //idealy every test should be in its own method
        returnedRecord = logic.createEntity( sampleMap );
        assertEquals( Integer.parseInt( sampleMap.get( DonationRecordLogic.ID )[ 0 ] ), returnedRecord.getId() );
        assertEquals( sampleMap.get( DonationRecordLogic.ADMINISTRATOR )[ 0 ], returnedRecord.getAdministrator() );
        assertEquals( sampleMap.get( DonationRecordLogic.TESTED )[0],  String.valueOf(returnedRecord.getTested() ));
        assertEquals( sampleMap.get( DonationRecordLogic.HOSPITAL )[ 0 ], returnedRecord.getHospital());
        assertEquals( sampleMap.get( DonationRecordLogic.CREATED )[0], logic.convertDateToString(returnedRecord.getCreated()));

    }
}
