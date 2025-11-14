
package jge3.math;

public class Vector2i {
	
	public int x;
	public int y;
	
	public Vector2i() {
		x = 0;
		y = 0;
	}
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i(Vector2i vec) {
		x = vec.x;
		y = vec.y;
	}
	
	public Vector2i clone() {
		return new Vector2i(x, y);
	}
	
	public void multiply(int d) {
		x *= d;
		y *= d;
	}
	
	public void divide(int d) {
		x /= d;
		y /= d;
	}
	
	public float distance() {
		return (float)Math.sqrt(x*x + y*y);
	}
	
}
