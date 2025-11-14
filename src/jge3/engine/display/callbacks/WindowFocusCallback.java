
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWWindowFocusCallback;

public abstract class WindowFocusCallback extends GLFWWindowFocusCallback {
	
	@Override
	public abstract void invoke(long window, boolean focused);
	
}
