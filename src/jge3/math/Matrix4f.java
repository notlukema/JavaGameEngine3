
package jge3.math;

public class Matrix4f extends org.joml.Matrix4f {
	
	public Matrix4f() {
		super();
	}
	
	public Matrix4f(Matrix4f mat) {
		super(mat);
	}
	
	public Matrix4f(Matrix3f mat) {
		super(mat);
	}
	
	public Matrix4f(float m00, float m01, float m02, float m03,
					float m10, float m11, float m12, float m13,
					float m20, float m21, float m22, float m23,
					float m30, float m31, float m32, float m33) {
		super(m00, m01, m02, m03,
				m10, m11, m12, m13,
				m20, m21, m22, m23,
				m30, m31, m32, m33);
	}
	
	public Matrix4f clone() {
		return new Matrix4f(this);
	}
	
	@Override
	public Matrix4f identity() {
		super.identity();
		return this;
	}
	
	@Override
	public Matrix4f invert() {
		super.invert();
		return this;
	}
	
	@Override
	public Matrix4f transpose() {
		super.transpose();
		return this;
	}
	
	public Matrix4f mul(Matrix4f mat) {
		super.mul(mat);
		return this;
	}
	
	@Override
	public Matrix4f translate(float x, float y, float z) {
		super.translate(x, y, z);
		return this;
	}
	
	@Override
	public Matrix4f rotateX(float angle) {
		super.rotateX(angle);
		return this;
	}
	
	@Override
	public Matrix4f rotateY(float angle) {
		super.rotateY(angle);
		return this;
	}
	
	@Override
	public Matrix4f rotateZ(float angle) {
		super.rotateZ(angle);
		return this;
	}
	
	public Matrix4f rotate(Quaternionf quaternion) {
		super.rotate(quaternion);
		return this;
	}
	
	@Override
	public Matrix4f scale(float x, float y, float z) {
		super.scale(x, y, z);
		return this;
	}
	
	@Override
	public Matrix4f perspective(float fov, float aspect, float near, float far) {
		super.perspective(fov, aspect, near, far);
		return this;
	}
	
}
