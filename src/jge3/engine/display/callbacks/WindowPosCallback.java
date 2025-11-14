
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWWindowPosCallback;

public abstract class WindowPosCallback extends GLFWWindowPosCallback {
	
	@Override
	public abstract void invoke(long window, int x, int y);
	
}
