
package jge3.math;

public class Quaternionf extends org.joml.Quaternionf {
	
	public Quaternionf() {
		super();
	}
	
	public Quaternionf(float x, float y, float z, float w) {
		super(x, y, z, w);
	}
	
	public Quaternionf(Quaternionf quaternion) {
		super(quaternion);
	}
	
	public Quaternionf clone() {
		return new Quaternionf(this);
	}
	
	@Override
	public Quaternionf identity() {
		super.identity();
		return this;
	}
	
	@Override
	public Quaternionf invert() {
		super.invert();
		return this;
	}
	
	@Override
	public Quaternionf conjugate() {
		super.conjugate();
		return this;
	}
	
	@Override
	public Quaternionf normalize() {
		super.normalize();
		return this;
	}
	
	public Quaternionf slerp(Quaternionf quaternion, float alpha) {
		super.slerp(quaternion, alpha);
		return this;
	}
	
	public Matrix4f get(Matrix4f mat) {
		super.get(mat);
		return mat;
	}
	
	public Matrix3f get(Matrix3f mat) {
		super.get(mat);
		return mat;
	}
	
}
