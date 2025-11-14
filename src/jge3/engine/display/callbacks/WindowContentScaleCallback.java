
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWWindowContentScaleCallback;

public abstract class WindowContentScaleCallback extends GLFWWindowContentScaleCallback {
	
	@Override
	public abstract void invoke(long window, float xscale, float yscale);
	
}
