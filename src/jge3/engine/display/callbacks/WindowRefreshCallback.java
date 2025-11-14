
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWWindowRefreshCallback;

public abstract class WindowRefreshCallback extends GLFWWindowRefreshCallback {
	
	@Override
	public abstract void invoke(long window);
	
}
