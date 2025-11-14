
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWScrollCallback;

public abstract class ScrollCallback extends GLFWScrollCallback {
	
	@Override
	public abstract void invoke(long window, double xoffset, double yoffset);
	
}
