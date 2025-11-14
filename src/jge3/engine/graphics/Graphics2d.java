
package jge3.engine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import jge3.common.image.Image;
import jge3.engine.display.Window;
import jge3.engine.managers.GraphicsManager;
import jge3.math.Matrix4f;
import jge3.math.Vector2f;
import jge3.src2d.object.camera.Camera2d;
import jge3.src2d.object.camera.Frustum2d;
import jge3.utils.ImageUtils;

public class Graphics2d extends Graphics {
	
	protected Camera2d camera;
	protected Frustum2d frustum;
	
	protected float mouseX;
	protected float mouseY;
	
	public Graphics2d(GraphicsManager manager, Window window, Camera2d camera) {
		terminated = false;
		this.manager = manager;
		this.window = window;
		this.camera = camera;
		frustum = camera.getFrustum();
		init();
		
		setClearColor(0f, 0f, 0f, 1.0f);
		setViewPort(frustum);
	}
	
	public Graphics2d(Window window) {
		this(window, new Camera2d(0, 0, window.getWidth(), window.getHeight()));
	}
	
	public Graphics2d(Window window, Camera2d camera) {
		this(GraphicsManager.get(), window, camera);
		GraphicsManager.get().addGraphics(this);
	}
	
	@Override
	public void bind() {
		contextID = window.setGraphicsContext();
		
		glViewport(left, top, right-left, bottom-top);
		glDisable(GL_DEPTH_TEST);
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
		Matrix4f cameraMatrix = camera.getCameraMatrix();
		glLoadMatrixf(cameraMatrix.get(floatBuffer));
	}
	
	public void drawLine(float x1, float y1, float x2, float y2) {
		glBegin(GL_LINES);
		glVertex2f(x1, y1);
		glVertex2f(x2, y2);
		glEnd();
	}
	
	public void drawLines(float[] x, float[] y) {
		glBegin(GL_LINE_STRIP);
		for (int i=0;i<x.length;i++) {
			glVertex2f(x[i], y[i]);
		}
		glEnd();
	}
	
	public void drawLines(Vector2f[] positions) {
		glBegin(GL_LINE_STRIP);
		for (int i=0;i<positions.length;i++) {
			glVertex2f(positions[i].x, positions[i].y);
		}
		glEnd();
	}
	
	public void drawRect(float x1, float y1, float x2, float y2) {
		glBegin(GL_LINE_STRIP);
		glVertex2f(x1, y1);
		glVertex2f(x2, y1);
		glVertex2f(x2, y2);
		glVertex2f(x1, y2);
		glVertex2f(x1, y1);
		glEnd();
	}
	
	public void fillRect(float x1, float y1, float x2, float y2) {
		glBegin(GL_QUADS);
		glVertex2f(x1, y1);
		glVertex2f(x2, y1);
		glVertex2f(x2, y2);
		glVertex2f(x1, y2);
		glEnd();
	}
	
	public void drawCircle(float x, float y, float size) {
		drawOval(x-size, y-size, x+size, y+size);
	}
	
	public void drawOval(float x1, float y1, float x2, float y2) {
		float w = Math.abs(x2-x1);
		float h = Math.abs(y2-y1);
		int points = (int)(Math.max(w, h)*Math.PI/5);
		float delta = (float)(Math.PI*2)/points;
		float midX = (x1+x2)/2;
		float midY = (y1+y2)/2;
		glBegin(GL_LINE_STRIP);
		for (int i=0;i<points+1;i++) {
			glVertex2f(midX+(float)Math.sin(i*delta)*w/2, midY+(float)Math.cos(i*delta)*h/2);
		}
		glEnd();
	}
	
	public void fillCircle(float x, float y, float size) {
		fillOval(x-size, y-size, x+size, y+size);
	}
	
	public void fillOval(float x1, float y1, float x2, float y2) {
		float w = Math.abs(x2-x1);
		float h = Math.abs(y2-y1);
		int points = (int)(Math.max(w, h)*Math.PI/5);
		float delta = (float)(Math.PI*2)/points;
		float midX = (x1+x2)/2;
		float midY = (y1+y2)/2;
		glBegin(GL_POLYGON);
		for (int i=0;i<points+1;i++) {
			glVertex2f(midX+(float)Math.sin(i*delta)*w/2, midY+(float)Math.cos(i*delta)*h/2);
		}
		glEnd();
	}
	
	public void drawPolygon(float[] x, float[] y) {
		glBegin(GL_LINE_STRIP);
		for (int i=0;i<x.length;i++) {
			glVertex2f(x[i], y[i]);
		}
		glVertex2f(x[0], y[0]);
		glEnd();
	}
	
	public void drawPolygon(Vector2f[] positions) {
		glBegin(GL_LINE_STRIP);
		for (int i=0;i<positions.length;i++) {
			glVertex2f(positions[i].x, positions[i].y);
		}
		glVertex2f(positions[0].x, positions[0].y);
		glEnd();
	}
	
	public void fillPolygon(float[] x, float[] y) {
		glBegin(GL_POLYGON);
		for (int i=0;i<x.length;i++) {
			glVertex2f(x[i], y[i]);
		}
		glEnd();
	}
	
	public void fillPolygon(Vector2f[] positions) {
		glBegin(GL_POLYGON);
		for (int i=0;i<positions.length;i++) {
			glVertex2f(positions[i].x, positions[i].y);
		}
		glEnd();
	}
	
	public void drawImage(Image image, float x1, float y1, float x2, float y2) {
		int textureID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, imageDrawMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, imageDrawMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.getBuffer());
		
		glColor4f(1f, 1f, 1f, 1f);
		glEnable(GL_TEXTURE_2D);
		
		glBegin(GL_QUADS);
		
		glTexCoord2f(0, 1);
		glVertex2f(x1, y1);
		glTexCoord2f(1, 1);
		glVertex2f(x2, y1);
		glTexCoord2f(1, 0);
		glVertex2f(x2, y2);
		glTexCoord2f(0, 0);
		glVertex2f(x1, y2);
		
		glEnd();
		
		glDisable(GL_TEXTURE_2D);
		glDeleteTextures(textureID);
		resetColor();
	}
	
	public void drawImage(Image image, float x, float y) {
		drawImage(image, x, y, 1f);
	}
	
	public void drawImage(Image image, float x, float y, float scale) {
		int textureID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, imageDrawMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, imageDrawMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.getBuffer());
		
		glColor4f(1f, 1f, 1f, 1f);
		glEnable(GL_TEXTURE_2D);
		
		glBegin(GL_QUADS);
		
		float hw = image.getWidth()/2f*scale;
		float hh = image.getHeight()/2f*scale;
		glTexCoord2f(0, 1);
		glVertex2f(x-hw, y-hh);
		glTexCoord2f(1, 1);
		glVertex2f(x+hw, y-hh);
		glTexCoord2f(1, 0);
		glVertex2f(x+hw, y+hh);
		glTexCoord2f(0, 0);
		glVertex2f(x-hw, y+hh);
		
		glEnd();
		
		glDisable(GL_TEXTURE_2D);
		glDeleteTextures(textureID);
		resetColor();
	}
	
	public void drawString(String string, int x, int y, int size) {
		drawString(string, x, y, size, -1, 1, false);
	}
	
	public void drawString(String string, int x, int y, int size, int alignX) {
		drawString(string, x, y, size, alignX, 1, false);
	}
	
	public void drawString(String string, int x, int y, int size, int alignX, int alignY) {
		drawString(string, x, y, size, alignX, alignY, false);
	}
	
	public void drawString(String string, int x, int y, int size, boolean alias) {
		drawString(string, x, y, size, -1, 1, alias);
	}
	
	public void drawString(String string, int x, int y, int size, int alignX, int alignY, boolean alias) {
		int w = font.getStringWidth(string, size);
		int h = font.getStringHeight(string, size);
		float hw = w/2f;
		float hh = h/2f;
		Image image = ImageUtils.createImage(w, h, 0, 0, 0, 0);
		font.drawString(string, image, (int)(hw+hw*alignX), 0, size, alignX, -1, (int)(color.x*255), (int)(color.y*255), (int)(color.z*255), alias);
		
		int textureID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, imageDrawMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, imageDrawMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.getBuffer());
		
		glColor4f(1f, 1f, 1f, 1f);
		glEnable(GL_TEXTURE_2D);
		glActiveTexture(GL_TEXTURE0);
		
		
		int nx = x-(int)(hw*alignX);
		int ny = y-(int)(hh*alignY);
		
		glBegin(GL_QUADS);
		glTexCoord2f(0, 1);
		glVertex2f(nx-hw, ny-hh);
		glTexCoord2f(1, 1);
		glVertex2f(nx+hw, ny-hh);
		glTexCoord2f(1, 0);
		glVertex2f(nx+hw, ny+hh);
		glTexCoord2f(0, 0);
		glVertex2f(nx-hw, ny+hh);
		
		glEnd();
		
		glDisable(GL_TEXTURE_2D);
		glDeleteTextures(textureID);
		resetColor();
	}
	
	public void setViewPort(Frustum2d frustum) {
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
	
	public Camera2d getCamera() {
		return camera;
	}
	
	public void setCamera(Camera2d camera) {
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
