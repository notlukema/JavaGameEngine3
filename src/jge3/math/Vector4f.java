
package jge3.math;

import static jge3.utils.VectorUtils.minFloat;

public class Vector4f {
	
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Vector4f() {
		x = 0f;
		y = 0f;
		z = 0f;
		w = 0f;
	}
	
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f(Vector4f vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
		w = vec.w;
	}
	
	public Vector4f clone() {
		return new Vector4f(x, y, z, w);
	}
	
	public void multiply(float d) {
		x *= d;
		y *= d;
		z *= d;
		w *= d;
	}
	
	public void divide(float d) {
		x /= d;
		y /= d;
		z /= d;
		w /= d;
	}
	
	public float distance() {
		return (float)Math.sqrt(x*x + y*y + z*z + w*w);
	}
	
	public Vector4f normalize() {
		float d = 1f/distance();
		x *= d;
		y *= d;
		z *= d;
		w *= d;
		return this;
	}
	
	public Vector4f safeNormalize() {
		float d = distance();
		if (d >= minFloat) {
			d = 1f/d;
			x *= d;
			y *= d;
			z *= d;
			w *= d;
		} else {
			x = 1;
			y = 0;
			z = 0;
			w = 0;
		}
		return this;
	}
	
	public Vector4f scale(Vector4f vec, float percent) {
		float i = 1-percent;
		x = x*i + vec.x*percent;
		y = y*i + vec.y*percent;
		z = z*i + vec.z*percent;
		w = w*i + vec.w*percent;
		return this;
	}
	
	public Vector4f mul(Matrix4f mat) {
		org.joml.Vector4f vec = new org.joml.Vector4f(x, y, z, w);
		vec.mul(mat);
		x = vec.x;
		y = vec.y;
		z = vec.z;
		w = vec.w;
		return this;
	}
	
	public Vector4f rotateX(float angle) {
		org.joml.Vector4f vec = new org.joml.Vector4f(x, y, z, w);
		vec.rotateX(angle);
		x = vec.x;
		y = vec.y;
		z = vec.z;
		w = vec.w;
		return this;
	}
	
	public Vector4f rotateY(float angle) {
		org.joml.Vector4f vec = new org.joml.Vector4f(x, y, z, w);
		vec.rotateY(angle);
		x = vec.x;
		y = vec.y;
		z = vec.z;
		w = vec.w;
		return this;
	}
	
	public Vector4f rotateZ(float angle) {
		org.joml.Vector4f vec = new org.joml.Vector4f(x, y, z, w);
		vec.rotateZ(angle);
		x = vec.x;
		y = vec.y;
		z = vec.z;
		w = vec.w;
		return this;
	}
	
	public Vector4f rotate(Quaternionf quaternion) {
		org.joml.Vector4f vec = new org.joml.Vector4f(x, y, z, w);
		vec.rotate(quaternion);
		x = vec.x;
		y = vec.y;
		z = vec.z;
		w = vec.w;
		return this;
	}
	
}
