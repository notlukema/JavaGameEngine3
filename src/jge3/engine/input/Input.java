
package jge3.engine.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import jge3.common.interactable.Interactable;
import jge3.engine.display.Cursor;
import jge3.engine.display.Window;
import jge3.math.Vector2f;

public class Input {

	private static Input input = new Input();
	
	public static Input get() {
		return input;
	}
	
	//
	
	private List<Window> windows;
	
	/*
	private HashMap<Integer, Boolean> onPressed;
	private HashMap<Integer, Boolean> onReleased;
	private HashMap<Integer, Boolean> keyPressed;
	
	private HashMap<Integer, Boolean> onMouseDown;
	private HashMap<Integer, Boolean> onMouseUp;
	private HashMap<Integer, Boolean> mouseDown;
	*/
	
	private List<Integer> onPressed;
	private List<Integer> onReleased;
	private List<Integer> keyPressed;
	
	private List<Integer> onMouseDown;
	private List<Integer> onMouseUp;
	private List<Integer> mouseDown;
	
	private GLFWKeyCallback keyCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWScrollCallback scrollCallback;
	
	private double mouseX;
	private double mouseY;
	
	private double scrollX;
	private double scrollY;
	
	private Input() {
		windows = new ArrayList<Window>();
		
		/*
		onPressed = new HashMap<Integer, Boolean>();
		onReleased = new HashMap<Integer, Boolean>();
		keyPressed = new HashMap<Integer, Boolean>();
		
		onMouseDown = new HashMap<Integer, Boolean>();
		onMouseUp = new HashMap<Integer, Boolean>();
		mouseDown = new HashMap<Integer, Boolean>();
		*/
		
		onPressed = new ArrayList<Integer>();
		onReleased = new ArrayList<Integer>();
		keyPressed = new ArrayList<Integer>();
		
		onMouseDown = new ArrayList<Integer>();
		onMouseUp = new ArrayList<Integer>();
		mouseDown = new ArrayList<Integer>();
	}
	
	public void initialize() {
		InputValues.initialize();
		
		mouseX = 0;
		mouseY = 0;
		
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW_PRESS) {
					if (!keyPressed.contains(key)) {
						onPressed.add(key);
						keyPressed.add(key);
					}
				}
				if (action == GLFW_RELEASE) {
					onReleased.add(key);
					keyPressed.remove(keyPressed.indexOf(key));
				}
			}
		};
		cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				mouseX = xpos;
				mouseY = ypos;
			}
		};
		mouseButtonCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				if (action == GLFW_PRESS) {
					if (!mouseDown.contains(button)) {
						onMouseDown.add(button);
						mouseDown.add(button);
					}
					
					Interactable interactable = Cursor.get().getTouching();
					if (interactable != null) {
						interactable.mouseDown(button);
					}
				}
				if (action == GLFW_RELEASE) {
					onMouseUp.add(button);
					mouseDown.remove(mouseDown.indexOf(button));
					
					Interactable interactable = Cursor.get().getTouching();
					if (interactable != null) {
						interactable.mouseUp(button);
					}
				}
			}
		};
		scrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				scrollX = xoffset;
				scrollY = yoffset;
			}
		};
	}
	
	public boolean addWindow(Window window) {
		boolean s = windows.add(window);
		if (s) {
			long id = window.getID();
			glfwSetKeyCallback(id, keyCallback);
			glfwSetCursorPosCallback(id, cursorPosCallback);
			glfwSetMouseButtonCallback(id, mouseButtonCallback);
			glfwSetScrollCallback(id, scrollCallback);
		}
		return s;
	}
	
	public boolean removeWindow(Window window) {
		boolean s = windows.remove(window);
		if (s) {
			long id = window.getID();
			glfwSetKeyCallback(id, null);
			glfwSetCursorPosCallback(id, null);
			glfwSetMouseButtonCallback(id, null);
			glfwSetScrollCallback(id, null);
		}
		return s;
	}
	
	public void pre() {
		scrollX = 0;
		scrollY = 0;
		onPressed.clear();
		onReleased.clear();
		onMouseDown.clear();
		onMouseUp.clear();
	}
	
	public int[] toArray(List<Integer> list) {
		int[] array = new int[list.size()];
		for (int i=0;i<array.length;i++) {
			array[i] = list.get(i);
		}
		return array;
	}
	
	public boolean onKeyPressed(int key) {
		return onPressed.contains(key);
	}
	
	public boolean onKeyReleased(int key) {
		return onReleased.contains(key);
	}
	
	public boolean isKeyPressed(int key) {
		return keyPressed.contains(key);
	}
	
	public int[] getOnKeysPressed() {
		return toArray(onPressed);
	}
	
	public int[] getOnKeysReleased() {
		return toArray(onReleased);
	}
	
	public int[] getIsKeysPressed() {
		return toArray(keyPressed);
	}
	
	public boolean onMouseDown(int button) {
		return onMouseDown.contains(button);
	}
	
	public boolean onMouseUp(int button) {
		return onMouseUp.contains(button);
	}
	
	public boolean isMouseDown(int button) {
		return mouseDown.contains(button);
	}
	
	public int[] getOnMousesDown() {
		return toArray(onMouseDown);
	}
	
	public int[] getOnMousesUp() {
		return toArray(onMouseUp);
	}
	
	public int[] getIsMousesDown() {
		return toArray(mouseDown);
	}
	
	public double getMouseX() {
		return mouseX;
	}
	
	public double getMouseY() {
		return mouseY;
	}
	
	public Vector2f getMousePos() {
		return new Vector2f((float)mouseX, (float)mouseY);
	}
	
	public double getScrollX() {
		return scrollX;
	}
	
	public double getScrollY() {
		return scrollY;
	}
	
	public Vector2f getScroll() {
		return new Vector2f((float)scrollX, (float)scrollY);
	}
	
	public void terminate() {
		keyCallback.free();
		cursorPosCallback.free();
		mouseButtonCallback.free();
		scrollCallback.free();
	}
	
}
