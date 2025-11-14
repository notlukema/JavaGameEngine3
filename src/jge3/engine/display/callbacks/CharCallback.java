
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWCharCallback;

public abstract class CharCallback extends GLFWCharCallback {
	
	@Override
	public abstract void invoke(long window, int codepoint);
	
}
