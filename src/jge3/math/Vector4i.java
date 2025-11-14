
package jge3.math;

public class Vector4i {
	
	public int x;
	public int y;
	public int z;
	public int w;
	
	public Vector4i() {
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}
	
	public Vector4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4i(Vector4i vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
		w = vec.w;
	}
	
	public Vector4i clone() {
		return new Vector4i(x, y, z, w);
	}
	
	public void multiply(int d) {
		x *= d;
		y *= d;
		z *= d;
		w *= d;
	}
	
	public void divide(int d) {
		x /= d;
		y /= d;
		z /= d;
		w /= d;
	}
	
	public float distance() {
		return (float)Math.sqrt(x*x + y*y + z*z + w*w);
	}
	
}
