//package logic;
//
//import common.EMFactory;
//import common.TomcatStartUp;
//import entity.BloodBank;
//import java.util.List;
//import javax.persistence.EntityManager;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//
///**
// *
// * @author Khalid
// */
//@Disabled
//public class BloodBankLogicTest {
//    
//    private BloodBankLogic logic;
//    private BloodBank expectedEntity;
//    
//    
//    @BeforeAll
//    final static void setUpBeforeClass() throws Exception {
//        TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );
//    }//end of setUpBeforeClass method.
//    
//    @AfterAll
//    final static void tearDownAfterClass() throws Exception {
//        TomcatStartUp.stopAndDestroyTomcat();
//    }//end of tearDownAfterClass method.
//    
//    @BeforeEach
//    final void setUp() throws Exception {
//        logic = LogicFactory.getFor( "BloodBank" );
//        EntityManager em = EMFactory.getEMF().createEntityManager();
//        
//        em.getTransaction().begin();
//        BloodBank entity = new BloodBank();
//        
//        entity.setName( "tim" );
//        entity.setEmplyeeCount(115);
//        entity.setPrivatelyOwned(true);
//        entity.setEstablished( logic.convertStringToDate( "1111-11-11 11:11:11" ) );
//        //COME BACK TO THIS ONE!!!
//        entity.setOwner(Person);
//        
//        expectedEntity = em.merge( entity );
//        em.getTransaction().commit();
//        em.close(); 
//    }//end of setUp method.
//    
//    
//    @AfterEach
//    final void tearDown(){
//        if( expectedEntity != null ){
//            logic.delete( expectedEntity );
//        }   
//    }//end of tearDown method.
//    
//    
//    @Test
//    final  void testGetAll(){
//        List<BloodBank> list = logic.getAll ();
//        int originalSize = list.size();
//        assertNotNull( expectedEntity );
//        logic.delete( expectedEntity );
//        list = logic.getAll();
//        //the new size of person must be one less
//        assertEquals( originalSize - 1, list.size());
//    }//end of testGetAll method.
//    
//    
//    private void assertBloodBankEquals( BloodBank expected, BloodBank actual ) {
//        //assert all field to guarantee they are the same
//        assertEquals( expected.getId(), actual.getId() );
//        assertEquals( expected.getOwner(), actual.getOwner() );
//        assertEquals( expected.getName(), actual.getName() );
//        assertEquals( expected.getPrivatelyOwned(), actual.getPrivatelyOwned() );
//        assertEquals( expected.getEstablished(), actual.getEstablished() );
//        assertEquals( expected.getEmplyeeCount(), actual.getEmplyeeCount() );
//    }//end of assertAccountEquals method.
//    
//    
//    @Test
//    final void testGetWithId() {
//        //using the id of test BloodBank get another BloodBank from logic
//        BloodBank returnedBloodBank = logic.getWithId( expectedEntity.getId() );
//
//        assertBloodBankEquals( expectedEntity, returnedBloodBank );
//    }//end of testGetWithId method.
//    
//    @Test
//    final void testGetBloodBankWithName() {
//        BloodBank returnedBloodBank = logic.getBloodBankWithName( expectedEntity.getName() );
//
//        assertBloodBankEquals( expectedEntity, returnedBloodBank );
//    }//end of testGetBloodBankWithName method.
//    
//    @Test
//    final void testGetBloodBankWithPrivatelyOwned() {
//        
//        //COME BACK TO THIS ONE!!!
//
//    }//end of testGetBloodBankWithPrivatelyOwned method.
//    
//    
//    @Test
//    final void testGetBloodBankWithEmplyeeCount() {
//        BloodBank returnedBloodBank = logic.getBloodBankWithEmployeeCount( expectedEntity.getEmplyeeCount() );
//
//        assertBloodBankEquals( expectedEntity, returnedBloodBank );
//        
//        
//    }//end of testGetBloodBankWithEmplyeeCount method.
//    
//    
//    
//    
//    
//    
//    
//}//end of BloodBankLogicTest class.