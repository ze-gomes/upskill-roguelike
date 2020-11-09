package pt.upskills.projeto.rogue.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.upskills.projeto.rogue.utils.Position;
import pt.upskills.projeto.rogue.utils.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {

	private Position position = new Position(2, 3);
	
	@BeforeEach
	public void prepare() {
		
	}
	
	@Test
	public void testConstructor() {
		assertEquals(2, position.getX());
		assertEquals(3, position.getY());
	}

	@Test
	public void testPlus() {
		
		assertEquals(new Position(5, 5), position.plus(new Vector2D(3, 2)));
		assertEquals(new Position(0, 0), position.plus(new Vector2D(-2, -3)));
	}

}
