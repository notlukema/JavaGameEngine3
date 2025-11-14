
package jge3.game.scenes;

import static jge3.game.PlatformerGame.*;

import jge3.engine.display.Window;
import jge3.engine.graphics.Graphics2d;
import jge3.engine.graphics.Renderer2d;
import jge3.game.scenes.components.Settings;
import jge3.src2d.World2d;
import jge3.src2d.object.interactable.RectButton;

public class TitleScreen extends Scene {
	
	public static final TitleScreen scene = new TitleScreen();
	
	private World2d w2d;
	private Renderer2d r2d;
	private Graphics2d g2d;
	
	private RectButton startButton;
	private RectButton settingsButton;
	
	private boolean settings;
	
	private TitleScreen() {
		//startButton = new RectButton(250, 100, new Vector3f(200, 0, 0));
		startButton = new RectButton("Start", FONT, 75, 50, 50, 200);
		settingsButton = new RectButton("Settings", FONT, 75, 50, 50, 200);
	}
	
	@Override
	public void initialize(Window window) {
		w2d = window.createWorld2d();
		r2d = createRenderer(w2d).defaultProgram();
		g2d = createGraphics2d(window, r2d.getCamera());
		
		r2d.setClearColor(0.6f, 0.7f, 0.8f);
		
		settings = false;
		
		startButton.setPosition(0, -50);
		w2d.addObject(startButton);
		settingsButton.setPosition(0, -200);
		w2d.addObject(settingsButton);
	}
	
	@Override
	public Scene update(float delta, Window window) {
		
		resetView(r2d);
		r2d.bind();
		r2d.clear();
		r2d.render();
		r2d.unbind();
		
		resetView(g2d);
		g2d.bind();
		g2d.setColor(255, 255, 255);
		g2d.setFont(FONT);
		g2d.drawString("Cool Title", 0, 170, 120, 0, 0);
		g2d.unbind();
		
		if (settingsButton.onMouseDown()) {
			Settings.component.resetVars();
			settings = true;
		}
		
		if (settings) {
			settings = !Settings.component.update(delta, window);
		}
		
		if (startButton.onMouseUp()) {
			return SelectScreen.scene;
		}
		
		return null;
	}
	
}
