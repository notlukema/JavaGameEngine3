
package jge3.engine.display.callbacks.global;

import org.lwjgl.glfw.GLFWJoystickCallback;

public abstract class JoystickCallback extends GLFWJoystickCallback {
	
	@Override
	public abstract void invoke(int jid, int event);
	
}
