
package jge3.game.scenes.components;

import static jge3.game.PlatformerGame.*;

import jge3.common.image.Texture;
import jge3.engine.display.Window;
import jge3.engine.graphics.Renderer2d;
import jge3.src2d.World2d;
import jge3.src2d.object.Object2d;
import jge3.src2d.object.Sprite;
import jge3.src2d.object.interactable.RectButton;
import jge3.src2d.object.shapes.Rect;

public class Settings extends Component {
	
	public static final Settings component = new Settings();
	
	public static final int EXIT = 1,
							BACK = 2;
	
	private World2d w2d;
	private Renderer2d r2d;

	private Object2d blocker;
	private RectButton exit;

	private RectButton backLevelSelect;
	private RectButton backEditor;
	private boolean backToLevelSelect;
	private boolean backToEditor;
	
	private int buttonPressed;
	
	private Settings() {
		blocker = new Object2d();
		Sprite dark = new Sprite(new Texture(0, 0, 0, 180));
		dark.addObject(new Rect(1920, 1080));
		blocker.addSprite(dark);
		
		exit = new RectButton("Back", FONT, 65, 30, 30, 30);
		exit.setPosition(-500, -320);
		exit.setLayer(1);
		
		backLevelSelect = new RectButton("Back to level select", FONT, 40, 40, 40, 160);
		backLevelSelect.setPosition(0, -320);
		backLevelSelect.setLayer(1);
		
		backEditor = new RectButton("Back to editor", FONT, 40, 70, 70, 140);
		backEditor.setPosition(0, -320);
		backEditor.setLayer(1);
		
		resetVars();
	}
	
	@Override
	public void initialize(Window window) {
		w2d = window.createWorld2d();
		r2d = createRenderer(w2d).defaultProgram();
		
		w2d.addObject(blocker);
		w2d.addObject(exit);
		
		w2d.addObject(backLevelSelect);
		w2d.addObject(backEditor);
	}
	
	public void resetVars() {
		backToLevelSelect = false;
		backToEditor = false;
	}
	
	public void setBackSelectButton(boolean back) {
		backToLevelSelect = back;
	}
	
	public void setBackEditorButton(boolean back) {
		backToEditor = back;
	}
	
	@Override
	public boolean update(float delta, Window window) {
		
		backLevelSelect.setHidden(!backToLevelSelect);
		backEditor.setHidden(!backToEditor);
		
		resetView(r2d);
		r2d.bind();
		r2d.clearData();
		r2d.render();
		r2d.unbind();
		
		if (exit.onMouseDown()) {
			buttonPressed = EXIT;
			return true;
		}
		
		if (backLevelSelect.onMouseUp() || backEditor.onMouseUp()) {
			buttonPressed = BACK;
			return true;
		}
		
		return false;
	}
	
	public int getButtonPressed() {
		return buttonPressed;
	}
	
}
