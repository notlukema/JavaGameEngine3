
package jge3.src2d.object.camera;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;

import jge3.engine.display.Window;
import jge3.math.Matrix4f;

public class Frustum2d {
	
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
	
	protected int left;
	protected int top;
	protected int right;
	protected int bottom;
	
	protected int width;
	protected int height;
	protected float middleWidth;
	protected float middleHeight;
	protected float widthRatio;
	protected float heightRatio;
	
	protected Matrix4f projMatrix;
	
	public Frustum2d(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		w();
		h();
		resetProjectionMatrix();
	}
	
	protected void resetWidth() {
		w();
		resetProjectionMatrix();
	}
	
	protected void w() {
		width = right-left;
		middleWidth = (right+left)/2f;
		widthRatio = 2f/width;
	}
	
	protected void resetHeight() {
		h();
		resetProjectionMatrix();
	}
	
	protected void h() {
		height = bottom-top;
		middleHeight = (bottom+top)/2f;
		heightRatio = 2f/height;
	}
	
	protected void resetProjectionMatrix() {
		projMatrix = new Matrix4f().scale(widthRatio, heightRatio, 1f);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projMatrix;
	}
	
	public int getLeft() {
		return left;
	}
	
	public int getTop() {
		return top;
	}
	
	public int getRight() {
		return right;
	}
	
	public int getBottom() {
		return bottom;
	}
	
	public void setLeft(int left) {
		this.left = left;
		resetWidth();
	}
	
	public void setTop(int top) {
		this.top = top;
		resetHeight();
	}
	
	public void setRight(int right) {
		this.right = right;
		resetWidth();
	}
	
	public void setBottom(int bottom) {
		this.bottom = bottom;
		resetHeight();
	}
	
	public void setSize(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		resetWidth();
		resetHeight();
	}
	
	public void setSize(Window window) {
		setSize(window.getID());
	}
	
	public void setSize(long windowID) {
		int[] width = new int[1];
		int[] height = new int[1];
		glfwGetWindowSize(windowID, width, height);
		left = 0;
		top = 0;
		right = width[0];
		bottom = height[0];
		resetWidth();
		resetHeight();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public float getMiddleWidth() {
		return middleWidth;
	}
	
	public float getMiddleHeight() {
		return middleHeight;
	}
	
	public float getWidthRatio() {
		return widthRatio;
	}
	
	public float getHeightRatio() {
		return heightRatio;
	}
	
}
