
package jge3.engine.managers;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWJoystickCallback;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import jge3.engine.display.Cursor;
import jge3.engine.display.Monitor;
import jge3.engine.display.Window;
import jge3.engine.display.WindowHints;
import jge3.engine.input.Input;

public class DisplayManager {
	
	private static DisplayManager manager = new DisplayManager();
	
	public static DisplayManager get() {
		return manager;
	}
	
	//
	
	private GLCapabilities glCaps;
	
	private List<Window> windows;
	
	private DisplayManager() {
		windows = new ArrayList<Window>();
	}
	
	public void initialize() {
		if (!glfwInit()) {
			throw new RuntimeException("Couldn't initialize GLFW");
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		long id = glfwCreateWindow(1, 1, "", NULL, NULL);
        glfwMakeContextCurrent(id);
		GL.createCapabilities();
		glCaps = GL.getCapabilities();
		glfwDestroyWindow(id);
		
		Input.get().initialize();
	}
	
	public void setContext(long windowID) {
		glfwMakeContextCurrent(windowID);
	}
	
	public GLCapabilities getGLCapabilities() {
		return glCaps;
	}
	
	public void destroy(Window window) {
		window.freeUsing();
		glfwDestroyWindow(window.getID());
		windows.remove(window);
	}
	
	public Window createWindow(int width, int height, String name) {
		return createWindow(width, height, name, NULL, new WindowHints());
	}
	
	public Window createWindow(int width, int height, String name, WindowHints extraHints) {
		return createWindow(width, height, name, NULL, extraHints);
	}
	
	public Window createWindow(int width, int height, String name, long monitor) {
		return createWindow(width, height, name, monitor, new WindowHints());
	}
	
	public Window createWindow(int width, int height, String name, long monitor, WindowHints extraHints) {
		Window window = new Window(this, width, height, name, monitor, extraHints);
		windows.add(window);
		return window;
	}
	
	public void addWindow(Window window) {
		windows.add(window);
	}
	
	public Window[] getWindows() {
		Window[] windows = new Window[this.windows.size()];
		for (int i=0;i<windows.length;i++) {
			windows[i] = this.windows.get(i);
		}
		return windows;
	}
	
	public int getWindowCount() {
		return windows.size();
	}
	
	public Window getWindow(int i) {
		return windows.get(i);
	}
	
	public long getWindowID(Window window) {
		return window.getID();
	}
	
	public Window getWindow(long id) {
		int length = windows.size();
		for(int i=0;i<length;i++) {
			if (windows.get(i).getID() == id) {
				return windows.get(i);
			}
		}
		return null;
	}
	
	public void destroyCloseRequestedWindows() {
		for (int i=0;i<windows.size();i++) {
			if (windows.get(i).closeRequested()) {
				destroy(windows.get(i));
				i--;
			}
		}
	}
	
	public boolean isVSync() {
		return Window.isVSync();
	}
	
	public void setVSync(boolean vSync) {
		Window.setVSync(vSync);
	}
	
	public Monitor getPrimaryMonitor() {
		return new Monitor(glfwGetPrimaryMonitor());
	}
	
	public Monitor[] getMonitors() {
		PointerBuffer monitorsBuffer = glfwGetMonitors();
		Monitor[] monitors = new Monitor[monitorsBuffer.limit()];
		for (int i=0;i<monitors.length;i++) {
			monitors[i] = new Monitor(monitorsBuffer.get(i));
		}
		return monitors;
	}
	
	public void pre() {
		Input.get().pre();
		glfwPollEvents();
		Cursor.get().resetTouch();
	}
	
	public void post() {
		Cursor.get().updateTouching();
		renderToScreens();
	}
	
	public void renderToScreens() {
		for (int i=0;i<windows.size();i++) {
			windows.get(i).renderToScreen();
		}
	}
	
	public void terminate() {
		while(windows.size()>0) {
			destroy(windows.get(0));
		}
		glfwTerminate();
		Input.get().terminate();
	}
	
	public void setErrorCallback(GLFWErrorCallback callback) {
		glfwSetErrorCallback(callback);
	}
	
	public void setJoystickCallback(GLFWJoystickCallback callback) {
		glfwSetJoystickCallback(callback);
	}
	
	public void setMonitorCallback(GLFWMonitorCallback callback) {
		glfwSetMonitorCallback(callback);
	}
	
}
