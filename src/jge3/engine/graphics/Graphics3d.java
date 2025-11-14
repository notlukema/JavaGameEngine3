
package jge3.engine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import jge3.common.image.Image;
import jge3.engine.display.Window;
import jge3.engine.managers.GraphicsManager;
import jge3.math.Matrix3f;
import jge3.math.Matrix4f;
import jge3.math.Vector3f;
import jge3.math.Vector4f;
import jge3.src3d.object.camera.Camera3d;
import jge3.src3d.object.camera.Frustum3d;

public class Graphics3d extends Graphics {
	
	protected Camera3d camera;
	protected Frustum3d frustum;
	
	protected Matrix4f cameraMatrix;
	
	public Graphics3d(GraphicsManager manager, Window window, Camera3d camera) {
		terminated = false;
		this.manager = manager;
		this.window = window;
		this.camera = camera;
		frustum = camera.getFrustum();
		init();
		
		setClearColor(0f, 0f, 0f, 1.0f);
		setViewPort(frustum);
	}
	
	public Graphics3d(Window window) {
		this(window, new Camera3d(0, 0, window.getWidth(), window.getHeight()));
	}
	
	public Graphics3d(Window window, Camera3d camera) {
		this(GraphicsManager.get(), window, camera);
		GraphicsManager.get().addGraphics(this);
	}
	
	@Override
	public void bind() {
		contextID = window.setGraphicsContext();
		
		glViewport(left, top, right-left, bottom-top);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		resetColor();
		updateCamera();
	}
	
	@Override
	public void updateCamera() {
		glMatrixMode(GL_PROJECTION);
		glLoadMatrixf(camera.getFrustum().getProjectionMatrix().get(floatBuffer));
		
		glMatrixMode(GL_MODELVIEW);
		camera.calcCameraMatrix();
		cameraMatrix = camera.getCameraMatrix();
		glLoadMatrixf(cameraMatrix.get(floatBuffer));
	}
	
	public void drawLine(float x1, float y1, float z1, float x2, float y2, float z2) {
		glBegin(GL_LINES);
		glVertex3f(x1, y1, -z1);
		glVertex3f(x2, y2, -z2);
		glEnd();
	}
	
	public void drawLines(float[] x, float[] y, float[] z) {
		glBegin(GL_LINE_STRIP);
		for (int i=0;i<x.length;i++) {
			glVertex3f(x[i], y[i], -z[i]);
		}
		glEnd();
	}
	
	public void drawLines(Vector3f[] positions) {
		glBegin(GL_LINE_STRIP);
		for (int i=0;i<positions.length;i++) {
			glVertex3f(positions[i].x, positions[i].y, -positions[i].z);
		}
		glEnd();
	}
	
	public void drawRect(float x, float y, float z, float l, float h, float w) {
		float hx = l/2f;
		float hy = h/2f;
		float hz = w/2f;
		
		glBegin(GL_QUADS);
		glVertex3f(x-hx, y-hy, -z-hz);
		glVertex3f(x+hx, y-hy, -z-hz);
		glVertex3f(x+hx, y+hy, -z-hz);
		glVertex3f(x-hx, y+hy, -z-hz);
		glEnd();
		glBegin(GL_QUADS);
		glVertex3f(x-hx, y-hy, -z+hz);
		glVertex3f(x+hx, y-hy, -z+hz);
		glVertex3f(x+hx, y+hy, -z+hz);
		glVertex3f(x-hx, y+hy, -z+hz);
		glEnd();
		glBegin(GL_QUADS);
		glVertex3f(x-hx, y-hy, -z-hz);
		glVertex3f(x+hx, y-hy, -z-hz);
		glVertex3f(x+hx, y-hy, -z+hz);
		glVertex3f(x-hx, y-hy, -z+hz);
		glEnd();
		glBegin(GL_QUADS);
		glVertex3f(x-hx, y+hy, -z-hz);
		glVertex3f(x+hx, y+hy, -z-hz);
		glVertex3f(x+hx, y+hy, -z+hz);
		glVertex3f(x-hx, y+hy, -z+hz);
		glEnd();
		glBegin(GL_QUADS);
		glVertex3f(x-hx, y-hy, -z-hz);
		glVertex3f(x-hx, y+hy, -z-hz);
		glVertex3f(x-hx, y+hy, -z+hz);
		glVertex3f(x-hx, y-hy, -z+hz);
		glEnd();
		glBegin(GL_QUADS);
		glVertex3f(x+hx, y-hy, -z-hz);
		glVertex3f(x+hx, y+hy, -z-hz);
		glVertex3f(x+hx, y+hy, -z+hz);
		glVertex3f(x+hx, y-hy, -z+hz);
		glEnd();
	}
	
	public void drawBillboardImage(Image image, float x, float y, float z) {
		drawBillboardImage(image, x, y, z, 1f);
	}
	
	public void drawFixedSizeBillboardImage(Image image, float x, float y, float z, float scale) {
		Vector4f p = new Vector4f(0, 0, 1, 1).mul(camera.getCameraMatrix()).mul(camera.getProjectionMatrix());
		drawBillboardImage(image, x, y, z, 1/p.z*scale);
	}
	
	public void drawBillboardImage(Image image, float x, float y, float z, float scale) {
		int textureID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, imageDrawMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, imageDrawMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.getBuffer());
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE); 
		
		glColor4f(1f, 1f, 1f, 1f);
		glEnable(GL_TEXTURE_2D);
		
		Matrix3f rot = new Matrix3f(camera.getCameraMatrix()).transpose();
		
		float hw = image.getWidth()/2f*scale;
		float hh = image.getHeight()/2f*scale;
		
		glBegin(GL_QUADS);
		glTexCoord2f(0, 1);
		billboardVertex3f(x, y, -z, -hw, -hh, 0f, rot);
		glTexCoord2f(1, 1);
		billboardVertex3f(x, y, -z, hw, -hh, 0f, rot);
		glTexCoord2f(1, 0);
		billboardVertex3f(x, y, -z, hw, hh, 0f, rot);
		glTexCoord2f(0, 0);
		billboardVertex3f(x, y, -z, -hw, hh, 0f, rot);
		
		glEnd();
		
		glDisable(GL_TEXTURE_2D);
		glActiveTexture(0);
		glDeleteTextures(textureID);
		resetColor();
	}
	
	public void drawString(String string, int x, int y, int z, int size) {
		drawString(string, x, y, z, size, 0, false);
	}
	
	public void drawString(String string, int x, int y, int z, int size, int alignX) {
		drawString(string, x, y, z, size, alignX, false);
	}
	
	public void drawString(String string, int x, int y, int z, int size, boolean alias) {
		drawString(string, x, y, z, size, 0, alias);
	}
	
	public void drawString(String string, int x, int y, int z, int size, int alignX, boolean alias) {
		Image image = font.createTextImage(string, color, size, alignX, alias);
		float hw = image.getWidth()/2f;
		float hh = image.getHeight()/2f;
		
		int textureID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, imageDrawMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, imageDrawMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.getBuffer());
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE); 
		
		glColor4f(1f, 1f, 1f, 1f);
		glEnable(GL_TEXTURE_2D);
		
		Matrix3f rot = new Matrix3f(camera.getCameraMatrix()).transpose();
		
		glBegin(GL_QUADS);
		glTexCoord2f(0, 1);
		billboardVertex3f(x, y, -z, -hw, -hh, 0f, rot);
		glTexCoord2f(1, 1);
		billboardVertex3f(x, y, -z, hw, -hh, 0f, rot);
		glTexCoord2f(1, 0);
		billboardVertex3f(x, y, -z, hw, hh, 0f, rot);
		glTexCoord2f(0, 0);
		billboardVertex3f(x, y, -z, -hw, hh, 0f, rot);

		resetColor();
		glEnd();
		
		glDisable(GL_TEXTURE_2D);
		glActiveTexture(0);
		glDeleteTextures(textureID);
	}
	
	private Vector3f billboardVertex3f(float x, float y, float z, float dx, float dy, float dz, Matrix3f rot) {
		Vector3f d = new Vector3f(dx, dy, -dz).mul(rot);
		Vector3f pos = new Vector3f(x+d.x, y+d.y, z+d.z);
		glVertex3f(pos.x, pos.y, pos.z);
		return pos;
	}
	
	public void setViewPort(Frustum3d frustum) {
		left = frustum.getLeft();
		top = frustum.getTop();
		right = frustum.getRight();
		bottom = frustum.getBottom();
	}
	
	public void resetView() {
		long id = window.getID();
		setViewPort(id);
		camera.getFrustum().setSize(id);
	}
	
	public Camera3d getCamera() {
		return camera;
	}
	
	public void setCamera(Camera3d camera) {
		this.camera = camera;
	}
	
	@Override
	public Window getWindow() {
		return window;
	}
	
	@Override
	public void destroy() {
		manager.destroy(this);
	}
	
}
