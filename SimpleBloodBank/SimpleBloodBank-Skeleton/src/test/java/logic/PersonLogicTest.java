package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import entity.Person;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author alotf
 */
@Disabled
class PersonLogicTest {
    
    private PersonLogic logic;
    private Person expectedEntity;
    
    
    @BeforeAll
    final static void setUpBeforeClass()throws Exception{
         TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );
    }
    
    @AfterAll
    final static void tearDownAfterClass()throws Exception{
        TomcatStartUp.stopAndDestroyTomcat();
    }
    
    @BeforeEach
    final void setUp()throws Exception{
        logic = LogicFactory.getFor ("Person");
        EntityManager em = EMFactory.getEMF().createEntityManager();
        
        em.getTransaction().begin();
        Person entity = new Person ();
        entity.setFirstName("first_Name");
        entity.setLastName("last_Name");
        entity.setAddress("Address");
        entity.setBirth(logic.convertStringToDate("1989-03-20 06:53:55"));
        entity.setPhone("Phone");
        expectedEntity = em.merge( entity );
        em.getTransaction().commit();
        em.close(); 
    }
    
    @AfterEach
    final void tearDown(){
        if( expectedEntity != null ){
            logic.delete( expectedEntity );
        }   
    }

    
     //Test of getAll method, of class PersonLogic.
   
    @Test
    final  void testGetAll(){
        List<Person> list = logic.getAll ();
        int originalSize = list.size();
        assertNotNull( expectedEntity );
        logic.delete( expectedEntity );
        list = logic.getAll();
        //the new size of person must be one less
        assertEquals( originalSize - 1, list.size());
 
    
    }
    private void assertPersonEquals(Person expected,Person actual) {
        //assert all field to guarantee they are the same
        assertEquals( expected.getId(), actual.getId() );
        assertEquals( expected.getFirstName(),actual.getFirstName());
        assertEquals( expected.getLastName(),actual.getLastName() );
        assertEquals( expected.getPhone(), actual.getPhone() );
        assertEquals( expected.getAddress(), actual.getAddress() );
        assertEquals ( expected.getBirth (), actual.getBirth ()); 
    }
    
    @Test
    final void testGetWithId() {
        //using the id of test person get another person from logic
        Person returnedPerson = logic.getWithId(expectedEntity.getId ());
        
        assertPersonEquals (expectedEntity, returnedPerson );
    }
    
    
    @Test
    final void testGetPersonWithFirstName() {
        int foundFull = 0;
        List<Person> returnedPerson = logic.getPersonWithFirstName (expectedEntity.getFirstName());
        for (Person person: returnedPerson){
            assertEquals (expectedEntity.getFirstName(),person.getFirstName ());
            if(person.getId().equals(expectedEntity.getId())){
                assertPersonEquals ( expectedEntity, person);
                foundFull++;
            }
        }
        assertEquals (1, foundFull,"if zero means not found, if more than one means duplicate");
    }
 
    
    @Test
    final void testGetPersonWithLastName() {
        int foundFull = 0;
        List<Person> returnedPerson = logic.getPersonWithLastName (expectedEntity.getLastName());
        for (Person person: returnedPerson){
            assertEquals (expectedEntity.getLastName(),person.getLastName ());
            if(person.getId().equals(expectedEntity.getId())){
                assertPersonEquals ( expectedEntity, person);
                foundFull++;
            }
        }
        assertEquals (1, foundFull, "if zero means not found, if more than one means duplicate");
    }
    
    
    @Test
    final void testGetPersonWithPhone() {
        int foundFull = 0;
        List<Person> returnedPerson = logic.getPersonWithPhone (expectedEntity.getPhone());
        for (Person person: returnedPerson){
            assertEquals (expectedEntity.getPhone(),person.getPhone ());
            if(person.getId().equals(expectedEntity.getId())){
                assertPersonEquals ( expectedEntity, person);
                foundFull++;
            }
        }
        assertEquals (1, foundFull, "if zero means not found, if more than one means duplicate");
    }

  

    @Test
    final void testGetPersonWithAddress() {
       int foundFull = 0;
        List<Person> returnedPerson = logic.getPersonsWithAddress(expectedEntity.getAddress());
        for (Person person: returnedPerson){
            assertEquals (expectedEntity.getAddress(),person.getAddress ());
            if(person.getId().equals(expectedEntity.getId())){
                assertPersonEquals ( expectedEntity, person);
                foundFull++;
            }
        }
        assertEquals (1, foundFull, "if zero means not found, if more than one means duplicate");
    }
    
    @Test
    final void testGetPersonWithBirth() {
   int foundFull = 0;
        List<Person> returnedPerson = logic.getPersonsWithBirth(expectedEntity.getBirth());
        for (Person person: returnedPerson){
            assertEquals (expectedEntity.getBirth(),person.getBirth ());
            if(person.getId().equals(expectedEntity.getId())){
                assertPersonEquals ( expectedEntity, person);
                foundFull++;
            }
        }
        assertEquals (1, foundFull, "if zero means not found, if more than one means duplicate");
    }
@Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(PersonLogic.FIRST_NAME, new String[]{ "Test Create Entity" } );
        sampleMap.put(PersonLogic.LAST_NAME, new String[]{ "Test Create Entity" } );
        sampleMap.put(PersonLogic.PHONE, new String[]{ "Test" } );
        sampleMap.put(PersonLogic.ADDRESS, new String[]{ "Test Create Entity" } );
        sampleMap.put(PersonLogic.BIRTH, new String[]{"1989-03-20 06:53:55" } );
        Person returnedPerson = logic.createEntity( sampleMap );
        logic.add( returnedPerson );

        List<Person> returnedPersons = logic.getPersonWithFirstName( returnedPerson.getFirstName() );
        returnedPerson=returnedPersons.get(returnedPersons.size()-1);
        assertEquals (sampleMap.get( PersonLogic.FIRST_NAME)[0], returnedPerson.getFirstName());
        assertEquals (sampleMap.get( PersonLogic.LAST_NAME)[0], returnedPerson.getLastName());       
        assertEquals (sampleMap.get( PersonLogic.PHONE)[0], returnedPerson.getPhone());
        assertEquals (sampleMap.get( PersonLogic.ADDRESS)[0], returnedPerson.getAddress());
        assertEquals (sampleMap.get( PersonLogic.BIRTH)[0],logic.convertDateToString(returnedPerson.getBirth()) );
        logic.delete( returnedPerson );
    }
    
    
    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( PersonLogic.ID, new String[]{ Integer.toString( expectedEntity.getId() ) } );
        sampleMap.put( PersonLogic.FIRST_NAME, new String[]{ expectedEntity.getFirstName()  } );
        sampleMap.put( PersonLogic.LAST_NAME, new String[]{ expectedEntity.getLastName()  } );
        sampleMap.put( PersonLogic.PHONE, new String[]{ expectedEntity.getPhone()  } );
        sampleMap.put( PersonLogic.ADDRESS, new String[]{ expectedEntity.getAddress()  } );
        sampleMap.put( PersonLogic.BIRTH, new String[]{ logic.convertDateToString(expectedEntity.getBirth())  } );
        
        Person returnedPerson = logic.createEntity( sampleMap );

        assertPersonEquals( expectedEntity, returnedPerson );
    }
 @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put(PersonLogic.ID, new String[]{Integer.toString (expectedEntity.getId())});
            map.put(PersonLogic.FIRST_NAME, new String[]{ expectedEntity.getFirstName()});
            map.put(PersonLogic.LAST_NAME, new String[]{ expectedEntity.getLastName()});
            map.put(PersonLogic.PHONE, new String[]{ expectedEntity.getPhone()});
            map.put(PersonLogic.ADDRESS, new String[]{ expectedEntity.getAddress()});
            map.put(PersonLogic.BIRTH, new String[]{ logic.convertDateToString(expectedEntity.getBirth())});
        };
        fillMap.accept(sampleMap);
        sampleMap.replace(PersonLogic.ID, null);
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.ID, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        fillMap.accept( sampleMap );
        sampleMap.replace(PersonLogic.FIRST_NAME, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.FIRST_NAME, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        fillMap.accept( sampleMap );
        sampleMap.replace(PersonLogic.LAST_NAME, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.LAST_NAME, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        fillMap.accept( sampleMap );
        sampleMap.replace(PersonLogic.PHONE, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.PHONE, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        fillMap.accept( sampleMap );
        sampleMap.replace(PersonLogic.ADDRESS, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.ADDRESS, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        fillMap.accept( sampleMap );
        sampleMap.replace(PersonLogic.BIRTH, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.BIRTH, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );     
    
    }
   
     @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put( PersonLogic.ID, new String[]{ Integer.toString( expectedEntity.getId() ) } );
            map.put( PersonLogic.FIRST_NAME, new String[]{ expectedEntity.getFirstName() } );
            map.put( PersonLogic.LAST_NAME, new String[]{ expectedEntity.getLastName() } );
            map.put( PersonLogic.PHONE, new String[]{ expectedEntity.getPhone() } );
            map.put( PersonLogic.ADDRESS, new String[]{ expectedEntity.getAddress() } );
        };    
        IntFunction<String> generateString = ( int length ) -> {
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };
        
        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.ID, null );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
//        sampleMap.replace( PersonLogic.ID, null );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.ID, new String[]{ "12b" } );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.FIRST_NAME, null );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.FIRST_NAME, new String[]{ generateString.apply( 51 ) } );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
      
        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.LAST_NAME, null );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.LAST_NAME, new String[]{ generateString.apply( 51 ) } );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.ADDRESS, null);
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.ADDRESS, new String[]{ generateString.apply( 101 ) } );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.PHONE, null );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.PHONE, new String[]{ generateString.apply( 16 ) } );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.BIRTH, null );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.BIRTH, new String[]{ "12ba" } );
        assertThrows( RuntimeException.class, () -> logic.createEntity( sampleMap ) );
    
    }   
    @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = ( int length ) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( PersonLogic.ID, new String[]{ Integer.toString( 1 ) } );
        sampleMap.put( PersonLogic.FIRST_NAME, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.LAST_NAME, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.PHONE, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.ADDRESS, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.BIRTH, new String[]{ "1989-03-20 06:53:55" } );
        
        Person returnedPerson = logic.createEntity(sampleMap);
        assertEquals (Integer.parseInt(sampleMap.get(PersonLogic.ID)[0]), returnedPerson.getId());
        assertEquals (sampleMap.get(PersonLogic.FIRST_NAME)[0], returnedPerson.getFirstName());
        assertEquals (sampleMap.get(PersonLogic.LAST_NAME)[0], returnedPerson.getLastName());
        assertEquals (sampleMap.get(PersonLogic.PHONE)[0], returnedPerson.getPhone());
        assertEquals (sampleMap.get(PersonLogic.ADDRESS)[0], returnedPerson.getAddress());
        assertEquals (sampleMap.get(PersonLogic.BIRTH)[0], logic.convertDateToString(returnedPerson.getBirth()));
    
         
        sampleMap = new HashMap<>();
        sampleMap.put( PersonLogic.ID, new String[]{ Integer.toString( 1 ) } );
        sampleMap.put( PersonLogic.FIRST_NAME, new String[]{ generateString.apply( 50 )} );
        sampleMap.put( PersonLogic.LAST_NAME, new String[]{ generateString.apply( 50 )} );
        sampleMap.put( PersonLogic.PHONE, new String[]{ generateString.apply( 15 )} );
        sampleMap.put( PersonLogic.ADDRESS, new String[]{ generateString.apply( 100 )} );
        sampleMap.put( PersonLogic.BIRTH, new String[]{ "1989-03-20 06:53:55"} );
        
        returnedPerson = logic.createEntity( sampleMap );
        assertEquals( Integer.parseInt( sampleMap.get( PersonLogic.ID )[ 0 ] ), returnedPerson.getId() );
        assertEquals( sampleMap.get( PersonLogic.FIRST_NAME )[ 0 ] , returnedPerson.getFirstName() );
        assertEquals( sampleMap.get( PersonLogic.LAST_NAME )[ 0 ] , returnedPerson.getLastName() );
        assertEquals( sampleMap.get( PersonLogic.PHONE )[ 0 ] , returnedPerson.getPhone() );
        assertEquals( sampleMap.get( PersonLogic.ADDRESS )[ 0 ] , returnedPerson.getAddress() );
        assertEquals( sampleMap.get( PersonLogic.BIRTH )[ 0 ] , logic.convertDateToString(returnedPerson.getBirth() ));
        
    }
    /**
     * Test of getColumnNames method, of class PersonLogic.
     */
    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals( Arrays.asList(  "id", "firstName", "lastName", "phone", "address","birth"), list );
    }

    /**
     * Test of getColumnCodes method, of class PersonLogic.
     */
    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals( Arrays.asList( PersonLogic.ID, PersonLogic.FIRST_NAME, PersonLogic.LAST_NAME,PersonLogic.PHONE,PersonLogic.ADDRESS,PersonLogic.BIRTH ), list );
    }

    /**
     * Test of extractDataAsList method, of class PersonLogic.
     */
    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList( expectedEntity );
        assertEquals( expectedEntity.getId(), list.get( 0 ) );
        assertEquals( expectedEntity.getFirstName(), list.get( 1 ) );
        assertEquals( expectedEntity.getLastName(), list.get( 2 ) );
        assertEquals( expectedEntity.getPhone(), list.get( 3 ) );
        assertEquals( expectedEntity.getAddress(), list.get( 4 ) );
        assertEquals (logic.convertDateToString(expectedEntity.getBirth()), list.get(5));
    }
    
}
