package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWKeyCallback;

public abstract class KeyCallback extends GLFWKeyCallback {
	
	@Override
	public abstract void invoke(long window, int key, int scancode, int action, int mods);
	
}
