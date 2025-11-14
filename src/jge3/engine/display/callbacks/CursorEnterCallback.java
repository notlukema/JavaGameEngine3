
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWCursorEnterCallback;

public abstract class CursorEnterCallback extends GLFWCursorEnterCallback {
	
	@Override
	public abstract void invoke(long window, boolean entered);
	
}
