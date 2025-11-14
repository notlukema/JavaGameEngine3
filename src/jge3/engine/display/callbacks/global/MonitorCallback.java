
package jge3.engine.display.callbacks.global;

import org.lwjgl.glfw.GLFWMonitorCallback;

public abstract class MonitorCallback extends GLFWMonitorCallback {
	
	@Override
	public abstract void invoke(long monitor, int event);
	
}
