
package jge3.math;

import static jge3.utils.VectorUtils.minFloat;

public class Vector2f {
	
	public float x;
	public float y;
	
	public Vector2f() {
		x = 0f;
		y = 0f;
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(Vector2f vec) {
		x = vec.x;
		y = vec.y;
	}
	
	public Vector2f clone() {
		return new Vector2f(x, y);
	}
	
	public void multiply(float d) {
		x *= d;
		y *= d;
	}
	
	public void divide(float d) {
		x /= d;
		y /= d;
	}
	
	public float distance() {
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public Vector2f normalize() {
		float d = 1f/distance();
		x *= d;
		y *= d;
		return this;
	}
	
	public Vector2f safeNormalize() {
		float d = distance();
		if (d >= minFloat) {
			d = 1f/d;
			x *= d;
			y *= d;
		} else {
			x = 1;
			y = 0;
		}
		return this;
	}
	
	public Vector2f scale(Vector2f vec, float percent) {
		float i = 1-percent;
		x = x*i + vec.x*percent;
		y = y*i + vec.y*percent;
		return this;
	}
	
	public Vector2f mul(Matrix2f mat) {
		org.joml.Vector2f vec = new org.joml.Vector2f(x, y);
		vec.mul(mat);
		x = vec.x;
		y = vec.y;
		return this;
	}
	
}
