
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWWindowMaximizeCallback;

public abstract class WindowMaximizeCallback extends GLFWWindowMaximizeCallback {
	
	@Override
	public abstract void invoke(long window, boolean maximized);
	
}
