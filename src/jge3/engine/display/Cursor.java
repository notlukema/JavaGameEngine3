
package jge3.engine.display;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWImage;

import jge3.common.image.Image;
import jge3.common.interactable.Interactable;
import jge3.engine.input.Input;
import jge3.engine.input.InputValues;
import jge3.math.Vector2f;
import jge3.utils.ImageUtils;

public class Cursor {
	
	public static final int TYPE_DEFAULT = GLFW_ARROW_CURSOR;
	public static final int TYPE_ARROW = GLFW_ARROW_CURSOR;
	public static final int TYPE_CENTER = GLFW_CENTER_CURSOR;
	public static final int TYPE_CROSSHAIR = GLFW_CROSSHAIR_CURSOR;
	public static final int TYPE_HAND = GLFW_HAND_CURSOR;
	public static final int TYPE_POINTING_HAND = GLFW_POINTING_HAND_CURSOR;
	public static final int TYPE_RESIZE = GLFW_RESIZE_ALL_CURSOR;
	public static final int TYPE_H_RESIZE = GLFW_HRESIZE_CURSOR;
	public static final int TYPE_V_RESIZE = GLFW_VRESIZE_CURSOR;
	public static final int TYPE_I_BEAM = GLFW_IBEAM_CURSOR;
	public static final int TYPE_NOT_ALLOWED = GLFW_NOT_ALLOWED_CURSOR;
	public static final int TYPE_RESIZE_EW = GLFW_RESIZE_EW_CURSOR;
	public static final int TYPE_RESIZE_NS = GLFW_RESIZE_NS_CURSOR;
	public static final int TYPE_RESIZE_NE_SW = GLFW_RESIZE_NESW_CURSOR;
	public static final int TYPE_RESIZE_NW_SE = GLFW_RESIZE_NWSE_CURSOR;
	
	private static final GlobalCursor globalCursor = new GlobalCursor();
	
	public static GlobalCursor get() {
		return globalCursor;
	}
	
	public static class GlobalCursor {
		
		private Interactable touching2d;
		private Interactable potential;
		private float minZ;
		
		public GlobalCursor() {
			touching2d = null;
		}
		
		public Interactable getTouching() {
			return touching2d;
		}
		
		public void resetTouch() {
			potential = null;
		}
		
		public void resetDepth() {
			minZ = -1;
		}
		
		public void setTouching(Interactable obj, float z) {
			if (minZ < 0 || z < minZ) {
				potential = obj;
				minZ = z;
			}
		}
		
		public void overrideTouching(Interactable obj, float newZ) {
			potential = obj;
			minZ = newZ;
		}
		
		public void updateTouching() {
			if (touching2d != potential) {
				if (touching2d != null) {
					touching2d.mouseExit();
				}
				if (potential != null) {
					potential.mouseEnter();
				}
				touching2d = potential;
			}
		}
		
		public boolean onLeftDown() {
			return Input.get().onMouseDown(InputValues.LEFT_MOUSE);
		}
		
		public boolean onLeftUp() {
			return Input.get().onMouseUp(InputValues.LEFT_MOUSE);
		}
		
		public boolean isLeftDown() {
			return Input.get().isMouseDown(InputValues.LEFT_MOUSE);
		}
		
		public boolean onRightDown() {
			return Input.get().onMouseDown(InputValues.RIGHT_MOUSE);
		}
		
		public boolean onRightUp() {
			return Input.get().onMouseUp(InputValues.RIGHT_MOUSE);
		}
		
		public boolean isRightDown() {
			return Input.get().isMouseDown(InputValues.RIGHT_MOUSE);
		}
		
		public boolean onMiddleDown() {
			return Input.get().onMouseDown(InputValues.MIDDLE_MOUSE);
		}
		
		public boolean onMiddleUp() {
			return Input.get().onMouseUp(InputValues.MIDDLE_MOUSE);
		}
		
		public boolean isMiddleDown() {
			return Input.get().isMouseDown(InputValues.MIDDLE_MOUSE);
		}
		
		public boolean onMouseDown(int button) {
			return Input.get().onMouseDown(button);
		}
		
		public boolean onMouseUp(int button) {
			return Input.get().onMouseUp(button);
		}
		
		public boolean isMouseDown(int button) {
			return Input.get().isMouseDown(button);
		}
		
		public float getMouseX() {
			return (float)Input.get().getMouseX();
		}
		
		public float getMouseY() {
			return (float)Input.get().getMouseY();
		}
		
		public Vector2f getMousePos() {
			return Input.get().getMousePos();
		}
		
		public float getScroll() {
			return (float)Input.get().getScrollY();
		}
		
	}
	
	//
	
	public long id;
	
	public Cursor() {
		id = glfwCreateStandardCursor(TYPE_DEFAULT);
	}
	
	public Cursor(int standardType) {
		id = glfwCreateStandardCursor(standardType);
	}
	
	public Cursor(Image image) {
		this(ImageUtils.toGLFWImage(image), false);
	}
	
	public Cursor(Image image, boolean free) {
		this(ImageUtils.toGLFWImage(image), free);
	}
	
	public Cursor(GLFWImage image) {
		this(image, false);
	}
	
	public Cursor(GLFWImage image, boolean free) {
		id = glfwCreateCursor(image, image.width(), image.height());
		if (free) {
			image.free();
		}
	}
	
	public void setCursor(int standardType) {
		glfwDestroyCursor(id);
		id = glfwCreateStandardCursor(standardType);
	}
	
	public void setCursor(Image image) {
		setCursor(ImageUtils.toGLFWImage(image), false);
	}
	
	public void setCursor(Image image, boolean free) {
		setCursor(ImageUtils.toGLFWImage(image), free);
	}
	
	public void setCursor(GLFWImage image) {
		setCursor(image, false);
	}
	
	public void setCursor(GLFWImage image, boolean free) {
		glfwDestroyCursor(id);
		id = glfwCreateCursor(image, image.width(), image.height());
		if (free) {
			image.free();
		}
	}
	
	public long getID() {
		return id;
	}
	
	public void destroy() {
		glfwDestroyCursor(id);
		id = -1;
	}
	
}
