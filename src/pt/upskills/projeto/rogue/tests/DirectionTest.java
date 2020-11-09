package pt.upskills.projeto.rogue.tests;

import org.junit.jupiter.api.Test;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionTest {

	@Test
	public void testAsVector() {
		assertEquals(Direction.UP.asVector(), new Vector2D(0, -1));
		assertEquals(Direction.DOWN.asVector(), new Vector2D(0, 1));
		assertEquals(Direction.LEFT.asVector(), new Vector2D(-1, 0));
		assertEquals(Direction.RIGHT.asVector(), new Vector2D(1, 0));
	}

}
