
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public abstract class MouseButtonCallback extends GLFWMouseButtonCallback {
	
	@Override
	public abstract void invoke(long window, int button, int action, int mods);
	
}
