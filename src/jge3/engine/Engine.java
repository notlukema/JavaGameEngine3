
package jge3.engine;

import jge3.engine.input.Input;
import jge3.engine.managers.AudioManager;
import jge3.engine.managers.DisplayManager;
import jge3.engine.managers.GraphicsManager;

public class Engine {
	
	public static final Engine engine = new Engine();
	
	public static Engine get() {
		return engine;
	}
	
	//
	
	private final DisplayManager dm;
	private final GraphicsManager gm;
	private final AudioManager am;
	private final Input i;
	
	private Engine() {
		dm = DisplayManager.get();
		gm = GraphicsManager.get();
		am = AudioManager.get();
		i = Input.get();
	}
	
	public DisplayManager getDisplayManager() {
		return dm;
	}
	
	public GraphicsManager getGraphicsManager() {
		return gm;
	}
	
	public AudioManager getAudioManager() {
		return am;
	}
	
	public Input getInputManager() {
		return i;
	}
	
	public void init() {
		dm.initialize();
		gm.initialize();
		am.initialize();
	}
	
	public void pre() {
		dm.pre();
	}
	
	public void post() {
		post(true);
	}
	
	public void post(boolean destroyCloseRequstedWindows) {
		dm.post();
		if (destroyCloseRequstedWindows) {
			dm.destroyCloseRequestedWindows();
		}
	}
	
	public void terminate() {
		dm.terminate();
		gm.terminate();
		am.terminate();
	}
	
}
