
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

public abstract class WindowSizeCallback extends GLFWWindowSizeCallback {
	
	@Override
	public abstract void invoke(long window, int width, int height);
	
}
