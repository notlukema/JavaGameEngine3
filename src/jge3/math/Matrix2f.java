
package jge3.math;

public class Matrix2f extends org.joml.Matrix2f {
	
	public Matrix2f() {
		super();
	}
	
	public Matrix2f(Matrix3f mat) {
		super(mat);
	}
	
	public Matrix2f(Matrix2f mat) {
		super(mat);
	}
	
	public Matrix2f(float m00, float m01,
					float m10, float m11) {
		super(m00, m01,
				m10, m11);
	}
	
	public Matrix2f clone() {
		return new Matrix2f(this);
	}
	
	@Override
	public Matrix2f identity() {
		super.identity();
		return this;
	}
	
	@Override
	public Matrix2f invert() {
		super.invert();
		return this;
	}
	
	@Override
	public Matrix2f transpose() {
		super.transpose();
		return this;
	}
	
	public Matrix2f mul(Matrix2f mat) {
		super.mul(mat);
		return this;
	}
	
	@Override
	public Matrix2f scale(float x, float y) {
		super.scale(x, y);
		return this;
	}
	
}
