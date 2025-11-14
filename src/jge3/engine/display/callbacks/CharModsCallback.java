
package jge3.engine.display.callbacks;

import org.lwjgl.glfw.GLFWCharModsCallback;

public abstract class CharModsCallback extends GLFWCharModsCallback {
	
	@Override
	public abstract void invoke(long window, int codepoint, int mods);
	
}
