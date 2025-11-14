
package jge3.engine.display;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

public class WindowHints {
	
	public static final int TRUE = GLFW_TRUE,
						FALSE = GLFW_FALSE,
						RESIZABLE = GLFW_RESIZABLE,
						VISIBLE = GLFW_VISIBLE,
						DECORATED = GLFW_DECORATED,
						FOCUSED = GLFW_FOCUSED,
						FLOATING = GLFW_FLOATING,
						MAXIMIZED = GLFW_MAXIMIZED,
						CENTER_CURSOR = GLFW_CENTER_CURSOR,
						TRANSPARENT = GLFW_TRANSPARENT_FRAMEBUFFER,
						FOCUS_ON_SHOW = GLFW_FOCUS_ON_SHOW,
						SCALE_TO_MONITOR = GLFW_SCALE_TO_MONITOR;
	
	//
	
	private List<Integer> hints;
	private List<Integer> values;
	
	public WindowHints() {
		hints = new ArrayList<Integer>();
		values = new ArrayList<Integer>();
	}
	
	public WindowHints addHint(int hint, int value) {
		hints.add(hint);
		values.add(value);
		return this;
	}
	
	public WindowHints removeHint(int hint) {
		int i = hints.indexOf(hint);
		hints.remove(i);
		values.remove(i);
		return this;
	}
	
	public int[] getHints() {
		int[] hints2 = new int[hints.size()];
		for (int i=0;i<hints2.length;i++) {
			hints2[i] = hints.get(i);
		}
		return hints2;
	}
	
	public int[] getHintValues() {
		int[] values2 = new int[values.size()];
		for (int i=0;i<values2.length;i++) {
			values2[i] = values.get(i);
		}
		return values2;
	}
	
}
