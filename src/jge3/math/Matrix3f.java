
package jge3.math;

public class Matrix3f extends org.joml.Matrix3f {
	
	public Matrix3f() {
		super();
	}
	
	public Matrix3f(Matrix4f mat) {
		super(mat);
	}
	
	public Matrix3f(Matrix3f mat) {
		super(mat);
	}
	
	public Matrix3f(Matrix2f mat) {
		super(mat);
	}
	
	public Matrix3f(float m00, float m01, float m02,
					float m10, float m11, float m12,
					float m20, float m21, float m22) {
		super(m00, m01, m02,
				m10, m11, m12,
				m20, m21, m22);
	}
	
	public Matrix3f clone() {
		return new Matrix3f(this);
	}
	
	@Override
	public Matrix3f identity() {
		super.identity();
		return this;
	}
	
	@Override
	public Matrix3f invert() {
		super.invert();
		return this;
	}
	
	@Override
	public Matrix3f transpose() {
		super.transpose();
		return this;
	}
	
	public Matrix3f mul(Matrix3f mat) {
		super.mul(mat);
		return this;
	}
	
	@Override
	public Matrix3f rotateX(float angle) {
		super.rotateX(angle);
		return this;
	}
	
	@Override
	public Matrix3f rotateY(float angle) {
		super.rotateY(angle);
		return this;
	}
	
	@Override
	public Matrix3f rotateZ(float angle) {
		super.rotateZ(angle);
		return this;
	}
	
	public Matrix3f rotate(Quaternionf quaternion) {
		super.rotate(quaternion);
		return this;
	}
	
	@Override
	public Matrix3f scale(float x, float y, float z) {
		super.scale(x, y, z);
		return this;
	}
	
}
