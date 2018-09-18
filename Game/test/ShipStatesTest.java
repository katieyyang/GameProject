import static org.junit.Assert.*;
import org.junit.*;

public class ShipStatesTest{
	/**
	 * Test 1 ship
	 */
	@Test
	public void ShipStatesTest1Ships() {
		ShipStates ships = new ShipStates();
		assertEquals(0, ships.getScore());
		ships.addShip(0, 0, 3, true, true);
		ships.addHit(0, 6, 1);
		assertEquals(0, ships.getScore());
		ships.addHit(0, 3, 2);
		assertEquals(0, ships.getScore());
		
	}
	/**
	 * Test two ships
	 */
	@Test
	public void testShipStates2Ships() {
		ShipStates ships = new ShipStates();
		assertEquals(0, ships.getScore());
		ships.addShip(0, 0, 3, true, true);
		ships.addShip(1, 0, 3, true, true);
		ships.addHit(0, 6, 1);
		assertEquals(0, ships.getScore());
		ships.addHit(0, 3, 2);
		assertEquals(0, ships.getScore());
		ships.addHit(0, 2, 3);
		assertEquals(1, ships.getScore());
		ships.addHit(1, 2, 5);
		assertEquals(2, ships.getScore());
		ships.addHit(0, 2, 6);
		assertEquals(5, ships.getScore());
	}
	
	/**
	 * Test two ships with reset.
	 */
	@Test
	public void testShipStatesReset() {
		ShipStates ships = new ShipStates();
		assertEquals(0, ships.getScore());
		ships.addShip(0, 0, 3, true, true);
		ships.addShip(6, 0, 3, true, true);
		ships.addHit(0, 6, 1);
		
		ships.reset();
		
		ships.addShip(0, 0, 3, true, true);
		ships.addShip(5, 0, 3, true, true);
		ships.addHit(0, 6, 1);
		assertEquals(0, ships.getScore());
		ships.addHit(0, 3, 2);
		assertEquals(0, ships.getScore());
		ships.addHit(5, 1, 3);
		assertEquals(1, ships.getScore());
		ships.addHit(5, 0, 5);
		assertEquals(3, ships.getScore());
	}
	
	/**
	 * Test 3 ships.
	 */
	@Test
	public void testShipState3() {
		ShipStates ships = new ShipStates();
		ships.addShip(0, 0, 3, true, true);
		ships.addShip(5, 0, 3, true, true);
		ships.addShip(3, 3, 4, true, true);
		ships.addHit(0, 6, 1);
		assertEquals(0, ships.getScore());
		ships.addHit(0, 3, 2);
		assertEquals(0, ships.getScore());
		ships.addHit(5, 1, 3);
		assertEquals(1, ships.getScore());
		ships.addHit(5, 0, 5);
		assertEquals(3, ships.getScore());
		ships.addHit(3, 3, 7);
		assertEquals(4, ships.getScore());
	}
}