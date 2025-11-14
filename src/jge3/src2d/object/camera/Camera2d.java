
package jge3.src2d.object.camera;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;

import jge3.math.Matrix4f;
import jge3.math.Vector2f;
import jge3.src2d.object.Object;

public class Camera2d extends Object {
	
	public static final String PROJECTION_MATRIX = "projectionMatrix";
	public static final String CAMERA_MATRIX = "cameraMatrix";
	
	//
	
	private int projUniformID;
	private int cameraUniformID;
	private Frustum2d frustum;
	
	private Matrix4f cameraMatrix;
	
	public Camera2d() {
		super();
		createFrustum();
	}
	
	public Camera2d(Vector2f position) {
		super(position);
		createFrustum();
	}
	
	public Camera2d(Vector2f position, float rotation) {
		super(position, rotation);
		createFrustum();
	}
	
	public Camera2d(int left, int top, int right, int bottom) {
		super();
		frustum = new Frustum2d(left, top, right, bottom);
	}
	
	public Camera2d(Vector2f position, int left, int top, int right, int bottom) {
		super(position);
		frustum = new Frustum2d(left, top, right, bottom);
	}
	
	public Camera2d(Vector2f position, float rotation, int left, int top, int right, int bottom) {
		super(position, rotation);
		frustum = new Frustum2d(left, top, right, bottom);
	}
	
	public Camera2d(float x, float y) {
		position = new Vector2f(x, y);
		rotation = 0;
		createFrustum();
	}
	
	public Camera2d(float x, float y, float rot) {
		position = new Vector2f(x, y);
		rotation = rot;
		createFrustum();
	}
	
	private void createFrustum() {
		frustum = new Frustum2d(0, 0, 0, 0);
	}
	
	@Override
	protected void resetTranslate() {
		transform.setTranslation(-position.x, -position.y);
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
	        transform.getFinalMatrix().get(fb);
	        glUniformMatrix4fv(cameraUniformID, false, fb);
		}
	}
	
	public Frustum2d getFrustum() {
		return frustum;
	}
	
}
