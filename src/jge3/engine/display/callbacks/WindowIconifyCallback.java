
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWWindowIconifyCallback;

public abstract class WindowIconifyCallback extends GLFWWindowIconifyCallback {
	
	@Override
	public abstract void invoke(long window, boolean iconified);
	
}
