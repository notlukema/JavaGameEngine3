
package jge3.math;

public class Vector3i {
	
	public int x;
	public int y;
	public int z;
	
	public Vector3i() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3i(Vector3i vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}
	
	public Vector3i clone() {
		return new Vector3i(x, y, z);
	}
	
	public void multiply(int d) {
		x *= d;
		y *= d;
		z *= d;
	}
	
	public void divide(int d) {
		x /= d;
		y /= d;
		z /= d;
	}
	
	public float distance() {
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
}
