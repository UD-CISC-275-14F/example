package com.mygdx.game;

public class Circle {
	private double x;
	private double y;
	private double radius;
	public Circle(double x, double y, double radius) {
		super();
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public double getArea() {
		return this.radius * this.radius * Math.PI;
	}
	
	public void moveTo(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double distanceTo(Circle other) {
		return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
	}
	
	public boolean overlaps(Circle other) {
		return distanceTo(other) <= (radius + other.radius);
	}
	
	public int complexMethod() {
		if (radius < x) {
			if (x < y) {
				return 5;
			}
			return -1;
		}
		else {
			if (y > x) {
				return 2;
			}
			return 1;
		}
	}
}
