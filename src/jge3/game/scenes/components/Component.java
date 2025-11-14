
package jge3.game.scenes.components;

import jge3.common.image.text.Font;
import jge3.engine.display.Window;
import jge3.engine.managers.AudioManager;
import jge3.engine.managers.DisplayManager;
import jge3.engine.managers.GraphicsManager;
import jge3.game.PlatformerGame;

public abstract class Component {
	
	public static final DisplayManager dpm = DisplayManager.get();
	public static final GraphicsManager glm = GraphicsManager.get();
	public static final AudioManager alm = AudioManager.get();
	public static final Font FONT = PlatformerGame.gameFont;
	
	public abstract void initialize(Window window);
	public abstract boolean update(float delta, Window window);
	
}
