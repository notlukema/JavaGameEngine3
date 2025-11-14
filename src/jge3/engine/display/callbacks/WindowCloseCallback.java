
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWWindowCloseCallback;

public abstract class WindowCloseCallback extends GLFWWindowCloseCallback {
	
	@Override
	public abstract void invoke(long window);
	
}
