
package jge3.game.scenes.components;

import static jge3.game.PlatformerGame.*;

import jge3.common.image.Texture;
import jge3.engine.display.Window;
import jge3.engine.graphics.Graphics2d;
import jge3.engine.graphics.Renderer2d;
import jge3.src2d.World2d;
import jge3.src2d.object.Object2d;
import jge3.src2d.object.Sprite;
import jge3.src2d.object.interactable.RectButton;
import jge3.src2d.object.shapes.Rect;

public class DeathScreen extends Component {
	
	public static final DeathScreen component = new DeathScreen();
	
	private World2d w2d;
	private Renderer2d r2d;
	private Graphics2d g2d;

	private Object2d blocker;
	private RectButton respawn;
	private RectButton back;
	private RectButton backToEditor;
	
	private boolean backPressed;
	private boolean editor;
	
	private DeathScreen() {
		blocker = new Object2d();
		Sprite dark = new Sprite(new Texture(100, 0, 0, 180));
		dark.addObject(new Rect(1920, 1080));
		blocker.addSprite(dark);
		
		respawn = new RectButton("Respawn", FONT, 60, 200, 50, 50);
		respawn.setPosition(0, -75);
		respawn.setLayer(1);
		back = new RectButton("Back to level select", FONT, 60, 200, 50, 50);
		back.setPosition(0, -200);
		back.setLayer(1);
		backToEditor = new RectButton("Back to Editor", FONT, 60, 200, 50, 50);
		backToEditor.setPosition(0, -200);
		backToEditor.setLayer(1);
	}
	
	@Override
	public void initialize(Window window) {
		w2d = window.createWorld2d();
		r2d = createRenderer(w2d).defaultProgram();
		g2d = createGraphics2d(window, r2d.getCamera());
		
		w2d.addObject(blocker);
		w2d.addObject(respawn);
		w2d.addObject(back);
		w2d.addObject(backToEditor);
		
		backPressed = false;
		editor = false;
	}
	
	public boolean isEditorMode() {
		return editor;
	}
	
	public void setEditor(boolean editor) {
		this.editor = editor;
	}
	
	@Override
	public boolean update(float delta, Window window) {
		
		if (editor) {
			back.setHidden(true);
			backToEditor.setHidden(false);
		} else {
			back.setHidden(false);
			backToEditor.setHidden(true);
		}
		
		resetView(r2d);
		r2d.bind();
		r2d.clearData();
		r2d.render();
		r2d.unbind();
		
		resetView(g2d);
		g2d.bind();
		g2d.clearData();
		g2d.setColor(255, 50, 50);
		g2d.setFont(FONT);
		g2d.drawString("You died lol", 0, 150, 100, 0, 0);
		g2d.unbind();
		
		if(back.onMouseUp() || backToEditor.onMouseUp()) {
			backPressed = true;
			return true;
		}
		
		if(respawn.onMouseUp()) {
			backPressed = false;
			return true;
		}
		
		return false;
	}
	
	public boolean isBackPressed() {
		return backPressed;
	}
	
}
