package pt.upskills.projeto.rogue.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.upskills.projeto.rogue.utils.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Vector2DTest {
	
	private Vector2D vector = new Vector2D(2, 3);
	
	@BeforeEach
	public void prepare() {
		
	}
	
	@Test
	public void testConstructor() {
		assertEquals(2, vector.getX());
		assertEquals(3, vector.getY());
	}

	@Test
	public void testPlus() {		
		assertEquals(new Vector2D(5, 5), vector.plus(new Vector2D(3, 2)));
		assertEquals(new Vector2D(0, 0), vector.plus(new Vector2D(-2, -3)));
	}

	@Test
	public void testMinus() {		
		assertEquals(new Vector2D(-1, 1), vector.minus(new Vector2D(3, 2)));
		assertEquals(new Vector2D(4, 6), vector.minus(new Vector2D(-2, -3)));
	}

}
