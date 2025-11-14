
package jge3.engine.display;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWVidMode;

import jge3.math.Vector2i;
import jge3.math.Vector3i;

public class Monitor {
	
	private long id;
	private GLFWVidMode videoMode;
	
	private int width;
	private int height;
	
	public Monitor(long id) {
		this.id = id;
		videoMode = glfwGetVideoMode(id);
		
		width = videoMode.width();
		height = videoMode.height();
	}
	
	public long getID() {
		return id;
	}
	
	public GLFWVidMode getVideoMode(long monitor) {
		return videoMode;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Vector2i getSize() {
		return new Vector2i(width, height);
	}
	
	public Vector3i getMonitorRGBBits() {
		return new Vector3i(videoMode.redBits(), videoMode.greenBits(), videoMode.blueBits());
	}
	
	public int getMonitorRefreshRate() {
		return videoMode.refreshRate();
	}
	
}
