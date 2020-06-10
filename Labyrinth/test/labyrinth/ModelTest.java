package labyrinth;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModelTest {
    
    public ModelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * A Model getMaxPlayers metódusát teszteli.
     */
    @Test
    public void testGetMaxPlayers() {
        Model instance = new Model(2);
        int expResult = 2;
        int result = instance.getMaxPlayers();
        assertEquals(expResult, result);
    }

    /**
     * A Model getMissing metódusát teszteli.
     */
    @Test
    public void testGetMissing() {
        Model instance = new Model(1);
        Field expResult = instance.getField(6,1);
        instance.insert(0, 1);
        Field result = instance.getMissing();
        assertEquals(expResult, result);
    }

    /**
     * A Model getWinner metódusát teszteli.
     */
    @Test
    public void testGetWinner() {
        Model instance = new Model(4);
        int expResult = -1;
        int result = instance.getWinner();
        assertEquals(expResult, result);
        instance.treasureP0.clear();
        instance.treasureP0.add(1);
        instance.getField(0,1).setLeft(true);
        instance.getField(0,1).setTreasure(1);
        expResult = 0;
        instance.movePlayer(1);
        result = instance.getWinner();
        assertEquals(expResult, result);
    }

    /**
     * A Model (paraméter nélküli) getField metódusát teszteli.
     */
    @Test
    public void testGetField_0args() {
        Model instance = new Model(1);
        Field expResult = instance.getField(0,0);
        Field result = instance.getField();
        assertEquals(expResult, result);
    }

    /**
     * A Model getPlayer metódusát teszteli.
     */
    @Test
    public void testGetPlayer() {
        Model instance = new Model(2);
        int expResult = 0;
        int result = instance.getPlayer();
        assertEquals(expResult, result);
    }

    /**
     * A fix sorba történő (érvénytelen) beszúrási kísérletet teszteli.
     */
    @Test
    public void testInvalidInsert() {
        Model instance = new Model(1);
        assertFalse(instance.insert(0,0));
        assertFalse(instance.insert(0,2));
    }
    
    /**
     * Az előzővel ellentétes írányú (érvénytelen) beszúrási kísérletet teszteli.
     */
    @Test
    public void testReverseInsert() {
        Model instance = new Model(1);
        assertTrue(instance.insert(0,1));
        assertFalse(instance.insert(2,1));
    }

    /**
     * A Model insertFromTop metódusát teszteli.
     */
    @Test
    public void testInsertFromTop() {
        Model instance = new Model(1);
        ArrayList<Field> fields = new ArrayList<>();
        Field f = instance.getMissing();
        fields.add(f);
        for(int i = 0; i < 7; i++){
           fields.add(instance.getField(i,1));
        }
        assertTrue(instance.insert(0,1));
        for(int i = 0; i < 7; i++){
            assertEquals(fields.get(i),instance.getField(i,1));
        }
       assertEquals(fields.get(7),instance.getMissing());
    }

    /**
     * A Model insertFromBottom metódusát teszteli.
     */
    @Test
    public void testInsertFromBottom() {
        Model instance = new Model(1);
        ArrayList<Field> fields = new ArrayList<>();
        Field f = instance.getMissing();
        for(int i = 0; i < 7; i++){
           fields.add(instance.getField(i,1));
        }
        fields.add(f);
        assertTrue(instance.insert(2,1));
        for(int i = 0; i < 7; i++){
            assertEquals(fields.get(i+1),instance.getField(i,1));
        }
       assertEquals(fields.get(0),instance.getMissing());
    }

    /**
     * A Model insertFromLeft metódusát teszteli.
     */
    @Test
    public void testInsertFromLeft() {
        Model instance = new Model(1);
        ArrayList<Field> fields = new ArrayList<>();
        Field f = instance.getMissing();
        fields.add(f);
        for(int i = 0; i < 7; i++){
           fields.add(instance.getField(1,i));
        }
        assertTrue(instance.insert(3,1));
        for(int i = 0; i < 7; i++){
            assertEquals(fields.get(i),instance.getField(1,i));
        }
       assertEquals(fields.get(7),instance.getMissing());
    }

    /**
     * A Model insertFromRight metódusát teszteli.
     */
    @Test
    public void testInsertFromRight() {
        Model instance = new Model(1);
        ArrayList<Field> fields = new ArrayList<>();
        Field f = instance.getMissing();
        for(int i = 0; i < 7; i++){
           fields.add(instance.getField(1,i));
        }
        fields.add(f);
        assertTrue(instance.insert(1,1));
        for(int i = 0; i < 7; i++){
            assertEquals(fields.get(i+1),instance.getField(1,i));
        }
       assertEquals(fields.get(0),instance.getMissing());
    }

    /**
     * A Model rotate metódusát teszteli.
     */
    @Test
    public void testRotate() {
        Model instance = new Model(1);
        boolean up  = instance.getMissing().isUp();
        boolean down  = instance.getMissing().isDown();
        boolean left  = instance.getMissing().isLeft();
        boolean right  = instance.getMissing().isRight();
        instance.rotate(1);
        assertEquals(up,instance.getMissing().isRight());
        assertEquals(right,instance.getMissing().isDown());
        assertEquals(down,instance.getMissing().isLeft());
        assertEquals(left,instance.getMissing().isUp());
    }

    /**
     * A Model movePlayer metódusát teszteli.
     */
    @Test
    public void testMovePlayer() {
        Model instance = new Model(1);
        instance.getField(0, 1).setLeft(true);
        instance.movePlayer(1);
        assertEquals(instance.getField(),instance.getField(0, 1));
        instance.getField(0, 1).setDown(true);
        instance.getField(1, 1).setUp(true);
        instance.movePlayer(2);
        assertEquals(instance.getField(),instance.getField(1, 1));
        instance.getField(1, 1).setLeft(true);
        instance.getField(1, 0).setRight(true);
        instance.movePlayer(3);
        assertEquals(instance.getField(),instance.getField(1, 0));
        instance.getField(1, 0).setUp(true);
        instance.movePlayer(0);
        assertEquals(instance.getField(),instance.getField(0, 0));
        instance.getField(1, 0).setUp(false);
        instance.movePlayer(2);
        assertEquals(instance.getField(),instance.getField(0, 0));
    }

    /**
     * A Model endTurn metódusát teszteli.
     */
    @Test
    public void testEndTurn() {
        Model instance = new Model(2);
        instance.endTurn();
        assertEquals(1,instance.getPlayer());
    }

    /**
     * A Model hasFoundTreasure metódusát teszteli.
     */
    @Test
    public void testHasFoundTreasure() {
        Model instance = new Model(1);
        instance.getField(1, 0).setUp(true);
        instance.getField(0, 1).setLeft(true);
        instance.getField(0, 1).setTreasure(instance.getNextTreasure(0));
        instance.getField(1, 0).setTreasure(-1);
        instance.posI.set(0, 1); instance.posJ.set(0, 0);
        assertFalse(instance.hasFoundTreasure());
        instance.posI.set(0, 0); instance.posJ.set(0, 1);
        assertTrue(instance.hasFoundTreasure());
    }
}