
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public abstract class CursorPosCallback extends GLFWCursorPosCallback {
	
	@Override
	public abstract void invoke(long window, double x, double y);
	
}
