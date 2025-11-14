
package jge3.engine.display.callbacks.global;

import org.lwjgl.glfw.GLFWErrorCallback;

public abstract class ErrorCallback extends GLFWErrorCallback {
	
	@Override
	public abstract void invoke(int error, long description);
	
}
