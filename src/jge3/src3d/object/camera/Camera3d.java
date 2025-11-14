
package jge3.src3d.object.camera;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryStack;

import jge3.math.Matrix4f;
import jge3.math.Vector3f;
import jge3.src3d.object.Object;

public class Camera3d extends Object {
	
	public static final String PROJECTION_MATRIX = "projectionMatrix";
	public static final String CAMERA_MATRIX = "cameraMatrix";
	
	//
	
	private int projUniformID;
	private int cameraUniformID;
	private Frustum3d frustum;
	
	private Matrix4f cameraMatrix;
	
	public Camera3d() {
		super();
		createFrustum();
	}
	
	public Camera3d(Vector3f position) {
		super(position);
		createFrustum();
	}
	
	public Camera3d(Vector3f position, Vector3f rotation) {
		super(position, rotation);
		createFrustum();
	}
	
	public Camera3d(int left, int top, int right, int bottom) {
		super();
		frustum = new Frustum3d(left, top, right, bottom);
	}
	
	public Camera3d(Vector3f position, int left, int top, int right, int bottom) {
		super(position);
		frustum = new Frustum3d(left, top, right, bottom);
	}
	
	public Camera3d(Vector3f position, Vector3f rotation, int left, int top, int right, int bottom) {
		super(position, rotation);
		frustum = new Frustum3d(left, top, right, bottom);
	}
	
	public Camera3d(float x, float y, float z) {
		position = new Vector3f(x, y, z);
		rotation = new Vector3f();
		createFrustum();
	}
	
	public Camera3d(float x, float y, float z, float rx, float ry, float rz) {
		position = new Vector3f(x, y, z);
		rotation = new Vector3f(rx, ry, rz);
		createFrustum();
	}
	
	private void createFrustum() {
		frustum = new Frustum3d(0, 0, 0, 0);
	}
	
	@Override
	protected void resetTranslate() {
		transform.setTranslation(-position.x, -position.y, position.z);
	}
	
	public Matrix4f getProjectionMatrix() {
		return frustum.getProjectionMatrix();
	}
	
	public void bindProjectionMatrix(int programID) {
		projUniformID = glGetUniformLocation(programID, PROJECTION_MATRIX);
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        frustum.getProjectionMatrix().get(fb);
	        glUniformMatrix4fv(projUniformID, false, fb);
		}
	}
	
	public void calcCameraMatrix() {
		cameraMatrix = transform.getFinalMatrix();
	}
	
	public Matrix4f getCameraMatrix() {
		return cameraMatrix;
	}
	
	public void bindCameraMatrix(int programID) {
		cameraUniformID = glGetUniformLocation(programID, CAMERA_MATRIX);
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        cameraMatrix.get(fb);
	        glUniformMatrix4fv(cameraUniformID, false, fb);
		}
	}
	
	public Frustum3d getFrustum() {
		return frustum;
	}
	
}
