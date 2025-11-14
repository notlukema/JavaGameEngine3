
package jge3.engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import jge3.engine.display.Window;
import jge3.engine.managers.GraphicsManager;
import jge3.math.Vector3f;
import jge3.math.Vector3i;
import jge3.math.Vector4f;
import jge3.math.Vector4i;
import jge3.utils.ColorUtils;

public abstract class Renderable {
	
	protected boolean terminated;
	protected GraphicsManager manager;
	
	protected long contextID;
	
	protected int left;
	protected int top;
	protected int right;
	protected int bottom;
	
	protected float r, g, b, a;
	
	public void freeUsing() {
		terminated = true;
	}
	
	public abstract void bind();
	
	public void unbind() {
		//glfwMakeContextCurrent(0);
		glUseProgram(0);
	}
	
	public void clear() {
		clearScreen();
		clearData();
	}
	
	public void clearScreen() {
		glClearColor(r, g, b, a);
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	public void clearData() {
		glClear(GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	}
	
	public Vector4f getClearColor() {
		return new Vector4f(r, g, b, a);
	}
	
	public void setClearColor(int color) {
		setClearColor(ColorUtils.getRed(color), ColorUtils.getGreen(color), ColorUtils.getBlue(color), ColorUtils.getAlpha(color));
	}
	
	public void setClearColor(Vector3i color) {
		setClearColor(color.x/255f, color.y/255f, color.z/255f, 1f);
	}
	
	public void setClearColor(Vector4i color) {
		setClearColor(color.x/255f, color.y/255f, color.z/255f, color.w/255f);
	}
	
	public void setClearColor(Vector3f color) {
		setClearColor(color.x, color.y, color.z, 1f);
	}
	
	public void setClearColor(Vector4f color) {
		setClearColor(color.x, color.y, color.z, color.w);
	}
	
	public void setClearColor(int r, int g, int b) {
		setClearColor(r/255f, g/255f, b/255f);
	}
	
	public void setClearColor(int r, int g, int b, int a) {
		setClearColor(r/255f, g/255f, b/255f, a/255f);
	}
	
	public void setClearColor(float r, float g, float b) {
		setClearColor(r, g, b, 1.0f);
	}
	
	public void setClearColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public void setViewPort(Window window) {
		setViewPort(window.getID());
	}
	
	public void setViewPort(long windowID) {
		int[] width = new int[1];
		int[] height = new int[1];
		glfwGetWindowSize(windowID, width, height);
		left = 0;
		top = 0;
		right = width[0];
		bottom = height[0];
	}
	
	public void setViewPort(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public boolean terminated() {
		return terminated;
	}
	
	public abstract void destroy();
	
}
