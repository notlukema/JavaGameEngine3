
package jge3.math;

import static jge3.utils.VectorUtils.minFloat;

public class Vector3f {
	
	public float x;
	public float y;
	public float z;
	
	public Vector3f() {
		x = 0f;
		y = 0f;
		z = 0f;
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector3f vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}
	
	public Vector3f clone() {
		return new Vector3f(x, y, z);
	}
	
	public void multiply(float d) {
		x *= d;
		y *= d;
		z *= d;
	}
	
	public void divide(float d) {
		x /= d;
		y /= d;
		z /= d;
	}
	
	public float distance() {
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vector3f normalize() {
		float d = 1f/distance();
		x *= d;
		y *= d;
		z *= d;
		return this;
	}
	
	public Vector3f safeNormalize() {
		float d = distance();
		if (d >= minFloat) {
			d = 1f/d;
			x *= d;
			y *= d;
			z *= d;
		} else {
			x = 1;
			y = 0;
			z = 0;
		}
		return this;
	}
	
	public Vector3f scale(Vector3f vec, float percent) {
		float i = 1-percent;
		x = x*i + vec.x*percent;
		y = y*i + vec.y*percent;
		z = z*i + vec.z*percent;
		return this;
	}
	
	public Vector3f mul(Matrix3f mat) {
		org.joml.Vector3f vec = new org.joml.Vector3f(x, y, z);
		vec.mul(mat);
		x = vec.x;
		y = vec.y;
		z = vec.z;
		return this;
	}
	
	public Vector3f mul(Matrix4f mat, float w) {
		org.joml.Vector4f vec = new org.joml.Vector4f(x, y, z, w);
		vec.mul(mat);
		x = vec.x;
		y = vec.y;
		z = vec.z;
		return this;
	}
	
	public Vector3f rotateX(float angle) {
		org.joml.Vector3f vec = new org.joml.Vector3f(x, y, z);
		vec.rotateX(angle);
		x = vec.x;
		y = vec.y;
		z = vec.z;
		return this;
	}
	
	public Vector3f rotateY(float angle) {
		org.joml.Vector3f vec = new org.joml.Vector3f(x, y, z);
		vec.rotateY(angle);
		x = vec.x;
		y = vec.y;
		z = vec.z;
		return this;
	}
	
	public Vector3f rotateZ(float angle) {
		org.joml.Vector3f vec = new org.joml.Vector3f(x, y, z);
		vec.rotateZ(angle);
		x = vec.x;
		y = vec.y;
		z = vec.z;
		return this;
	}
	
	public Vector3f rotate(Quaternionf quaternion) {
		org.joml.Vector3f vec = new org.joml.Vector3f(x, y, z);
		vec.rotate(quaternion);
		x = vec.x;
		y = vec.y;
		z = vec.z;
		return this;
	}
	
}
