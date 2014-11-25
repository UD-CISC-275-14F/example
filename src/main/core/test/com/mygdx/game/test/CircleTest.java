package com.mygdx.game.test;

import edu.udel.jatlas.gdxexample.Circle;
import junit.framework.TestCase;

public class CircleTest extends TestCase {
	public void test_Circle() {
		Circle c1 = new Circle(0, 0, 1);
		Circle c2 = new Circle(1, 1, 1);

		assertEquals(Math.PI, c1.getArea());
		
		assertEquals(Math.sqrt(2), c1.distanceTo(c2));

	}
	public void test_Circle_mutation() {
		Circle c1 = new Circle(0, 0, 1);
		
		Circle c2 = new Circle(1, 1, 1);
		assertTrue(c1.overlaps(c2));
		
		c2.moveTo(5, 5);
		assertFalse(c1.overlaps(c2));
	}
}
