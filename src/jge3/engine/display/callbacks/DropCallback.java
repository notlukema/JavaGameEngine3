
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWDropCallback;

public abstract class DropCallback extends GLFWDropCallback {
	
	@Override
	public abstract void invoke(long window, int count, long names);
	
}
