
package jge3.src3d.object.camera;

import static jge3.utils.MatrixUtils.toRadians;

import jge3.math.Matrix4f;
import jge3.src2d.object.camera.Frustum2d;

public class Frustum3d extends Frustum2d {
	
	public static final float DEFAULT_FOV = 70f;
	public static final float DEFAULT_NEAR = 10f;
	public static final float DEFAULT_FAR = 50000f;
	
	/*          /\
	 *          |
	 *         top
	 *          |
	 * <- left -+- right ->
	 *          |
	 *        bottom
	 *          |
	 *          \/
	 */
	
	private float middleZ;
	private float zRatio;
	
	private float near;
	private float far;
	private float fieldOfView;
	
	public Frustum3d(int left, int top, int right, int bottom) {
		this(left, top, right, bottom, DEFAULT_NEAR, DEFAULT_FAR, DEFAULT_FOV);
	}

	public Frustum3d(int left, int top, int right, int bottom, float fov) {
		this(left, top, right, bottom, DEFAULT_NEAR, DEFAULT_FAR, fov);
	}
	
	public Frustum3d(int left, int top, int right, int bottom, float near, float far) {
		this(left, top, right, bottom, near, far, DEFAULT_FOV);
	}
	
	public Frustum3d(int left, int top, int right, int bottom, float near, float far, float fov) {
		super(left, top, right, bottom);
		this.near = near;
		this.far = far;
		z();
		fieldOfView = fov;
		resetProjectionMatrix();
	}
	
	protected void resetZ() {
		z();
		resetProjectionMatrix();
	}
	
	protected void z() {
		middleZ = (near+far)/2f;
		zRatio = 1f/(far-near);
	}
	
	@Override
	protected void resetProjectionMatrix() {
		projMatrix = new Matrix4f().perspective(fieldOfView*toRadians, (float)width/height, near, far);
	}
	
	public float getNear() {
		return near;
	}
	
	public float getFar() {
		return far;
	}
	
	public float getMiddleZ() {
		return middleZ;
	}
	
	public float getZRatio() {
		return zRatio;
	}
	
	public float getFieldOfView() {
		return fieldOfView;
	}
	
	public float getFOV() {
		return fieldOfView;
	}
	
	public void setNear(float near) {
		this.near = near;
		resetZ();
		resetProjectionMatrix();
	}
	
	public void setFar(float far) {
		this.far = far;
		resetZ();
		resetProjectionMatrix();
	}
	
	public void setFieldOfView(float fov) {
		fieldOfView = fov;
		resetProjectionMatrix();
	}
	
	public void setFOV(float fov) {
		fieldOfView = fov;
		resetProjectionMatrix();
	}
	
}
