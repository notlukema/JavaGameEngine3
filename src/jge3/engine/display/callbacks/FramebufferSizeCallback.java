
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

public abstract class FramebufferSizeCallback extends GLFWFramebufferSizeCallback {
	
	@Override
	public abstract void invoke(long window, int width, int height);
	
}
