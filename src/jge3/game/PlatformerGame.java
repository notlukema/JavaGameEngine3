
package jge3.game;

import jge3.common.image.Image;
import jge3.common.image.text.Font;
import jge3.engine.display.*;
import jge3.engine.display.Window;
import jge3.engine.graphics.Graphics2d;
import jge3.engine.graphics.Graphics3d;
import jge3.engine.graphics.Renderer2d;
import jge3.engine.graphics.Renderer3d;
import jge3.engine.graphics.custom.InteractiveRenderer2d;
import jge3.engine.graphics.custom.InteractiveRenderer3d;
import jge3.engine.input.*;
import jge3.engine.managers.*;
import jge3.engine.input.Input;
import jge3.engine.input.InputValues;
import jge3.engine.managers.AudioManager;
import jge3.engine.managers.DisplayManager;
import jge3.engine.managers.GraphicsManager;
import jge3.game.scenes.Scene;
import jge3.game.scenes.SelectScreen;
import jge3.game.scenes.TitleScreen;
import jge3.game.scenes.WorldEditor;
import jge3.game.scenes.WorldPlayer;
import jge3.game.scenes.components.DeathScreen;
import jge3.game.scenes.components.InfoDisplay;
import jge3.game.scenes.components.Settings;
import jge3.src2d.World2d;
import jge3.src2d.object.camera.Camera2d;
import jge3.src3d.World3d;
import jge3.src3d.object.camera.Camera3d;
import jge3.utils.FontUtils;
import jge3.utils.ImageUtils;

public class PlatformerGame {
	
	public static Scene currentScene;
	public static Font gameFont = FontUtils.loadTrueType(ResourcePaths.R_FONTS+"/ostrich-bold.ttf", 100);
	
	public static void resetView(Renderer2d renderer) {
		renderer.setViewPort(x1, y1, x2, y2);
	}
	public static void resetView(Renderer3d renderer) {
		renderer.setViewPort(x1, y1, x2, y2);
	}
	public static void resetView(Graphics2d graphics) {
		graphics.setViewPort(x1, y1, x2, y2);
	}
	public static void resetView(Graphics3d graphics) {
		graphics.setViewPort(x1, y1, x2, y2);
	}
	
	public static Renderer2d createRenderer(World2d world) {
		return new InteractiveRenderer2d(world);
	}
	
	public static Renderer2d createRenderer(World2d world, Camera2d camera) {
		return new InteractiveRenderer2d(world, camera);
	}
	
	public static Renderer3d createRenderer(World3d world) {
		return new InteractiveRenderer3d(world);
	}
	
	public static Renderer3d createRenderer(World3d world, Camera3d camera) {
		return new InteractiveRenderer3d(world, camera);
	}
	
	public static Graphics2d createGraphics2d(Window window) {
		return GraphicsManager.get().createGraphics2d(window);
	}
	
	public static Graphics2d createGraphics2d(Window window, Camera2d camera) {
		return GraphicsManager.get().createGraphics2d(window, camera);
	}
	
	public static Graphics3d createGraphics3d(Window window) {
		return GraphicsManager.get().createGraphics3d(window);
	}
	
	public static Graphics3d createGraphics3d(Window window, Camera3d camera) {
		return GraphicsManager.get().createGraphics3d(window, camera);
	}
	
	private static int width, height;
	private static int x1, y1, x2, y2;
	
	private static int fps;
	
	public static int getFPS() {
		return fps;
	}
	
	public static void main(String[] args) {
		
		// initialize
		DisplayManager dpm = DisplayManager.get();
		GraphicsManager glm = GraphicsManager.get();
		AudioManager alm = AudioManager.get();
		Input input = Input.get();
		dpm.initialize();
		glm.initialize();
		alm.initialize();
		
		width = 1200;
		height = 750;
		Window window = dpm.createWindow(width, height, "Game");
		Image icon = ImageUtils.createImage(1, 1, 50, 50, 255);
		window.setIcon(icon);
		
		input.addWindow(window);
		
		TitleScreen.scene.initialize(window);
		SelectScreen.scene.initialize(window);
		WorldPlayer.scene.initialize(window);
		WorldEditor.scene.initialize(window);
		Settings.component.initialize(window);
		DeathScreen.component.initialize(window);
		InfoDisplay.component.initialize(window);
		
		currentScene = TitleScreen.scene;
		
		fps = 0;
		int loops = 0;
		long thisTime = System.nanoTime();
		long lastTime = System.nanoTime();
		long lastSecond = System.nanoTime();
		while (dpm.getWindows().length > 0) {
			thisTime = System.nanoTime();
			float delta = (thisTime-lastTime)/1000000000f;
			lastTime = thisTime;
			input.pre();
			dpm.pre();
			
			int ww = window.getWidth();
			int wh = window.getHeight();
			float r = Math.min((float)ww/width, (float)wh/height);
			int nw = (int)(width*r);
			int nh = (int)(height*r);
			x1 = (ww-nw)/2;
			y1 = (wh-nh)/2;
			x2 = x1 + nw;
			y2 = y1 + nh;
			
			if (input.onKeyPressed(InputValues.ENTER)) {
				if (input.isKeyPressed(InputValues.LEFT_ALT)) {
					window.toggleFullscreen();
				}
			}
			
			Scene newScene = currentScene.update(delta, window);
			if (newScene != null) {
				currentScene = newScene;
			}
			
			dpm.post();
			dpm.destroyCloseRequestedWindows();
			
			loops++;
			
			if (thisTime-lastSecond >= 1000000000) {
				lastSecond = thisTime;
				fps = loops;
				loops = 0;
			}
		}
		
		dpm.terminate();
		glm.terminate();
		alm.terminate();
	}
	
}
