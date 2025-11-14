
package jge3.game.scenes;

import static jge3.game.PlatformerGame.*;
import static jge3.game.ResourcePaths.*;

import jge3.common.image.Image;
import jge3.common.image.Texture;
import jge3.engine.display.Window;
import jge3.engine.graphics.Renderer2d;
import jge3.game.scenes.components.Settings;
import jge3.game.world.GameWorld;
import jge3.src2d.World2d;
import jge3.src2d.object.Object2d;
import jge3.src2d.object.Sprite;
import jge3.src2d.object.Vertex2d;
import jge3.src2d.object.interactable.RectButton;
import jge3.src2d.object.interactable.PolyButton;
import jge3.src2d.object.interactable.RoundButton;
import jge3.utils.ImageUtils;

public class SelectScreen extends Scene {
	
	public static final SelectScreen scene = new SelectScreen();
	
	private World2d w2d;
	private Renderer2d r2d;
	
	private int unlocked;
	
	private int levelCount;
	private RectButton[] levelButtons;
	private Object2d[] blockers;
	private float targetX;
	private float maxX;
	
	private PolyButton rightButton;
	private PolyButton leftButton;
	private RoundButton backButton;
	private RoundButton settingsButton;
	
	private boolean settings;
	
	private SelectScreen() {
		unlocked = 1;
		
		levelCount = 20;
		levelButtons = new RectButton[levelCount];
		blockers = new Object2d[levelCount];
		
		int maxPerScreen = 6*4;
		for (int i=0;i<levelCount;i++) {
			Image image = ImageUtils.createImage(80, 80, 50, 50, 200);
			FONT.drawString(""+(i+1), image, 40, 35, 50, 0, 0, 255, 255, 255);
			levelButtons[i] = new RectButton(image);
			int local = i%maxPerScreen;
			float x = local%6 * 160 - 400;
			float y = 260 - (local/6 * 160);
			x += i/maxPerScreen * 1200;
			levelButtons[i].setPosition(x, y);
			
			Image blocker = ImageUtils.createImage(80, 80, 0, 0, 0, 100);
			blockers[i] = new Object2d();
			blockers[i].addSprite(new Sprite(new Texture(blocker)));
			blockers[i].setPosition(x, y);
			blockers[i].setLayer(1);
		}
		
		targetX = 0f;
		maxX = (levelCount-1)/maxPerScreen * 1200;
		
		Image rightArrow = ImageUtils.loadImage(R_COMMON+"/right_arrow.png");
		Image leftArrow = ImageUtils.loadImage(R_COMMON+"/left_arrow.png");
		
		rightButton = new PolyButton(rightArrow);
		leftButton = new PolyButton(leftArrow);
		
		rightButton.setVertices(arrow(rightArrow.getWidth()/5, rightArrow.getHeight()/5, false));
		leftButton.setVertices(arrow(rightArrow.getWidth()/5, leftArrow.getHeight()/5, true));
		
		backButton = new RoundButton(ImageUtils.loadImage(R_COMMON+"/back_arrow.png"));
		backButton.setRadius(35);
		
		settingsButton = new RoundButton(ImageUtils.loadImage(R_SETTINGS+"/settings_button.png"));
		settingsButton.setRadius(35);
	}
	
	private Vertex2d[] arrow(float width, float height, boolean flip) {
		if (flip) {
			return new Vertex2d[] {
					new Vertex2d(width, height, 1, 1),
					new Vertex2d(width*0.7f, height, 0.85f, 1),
					new Vertex2d(-width, height*0.1f, 0, 0.55f),
					new Vertex2d(-width, -height*0.1f, 0, 0.45f),
					new Vertex2d(width*0.7f, -height, 0.85f, 0),
					new Vertex2d(width, -height, 1, 0)
			};
		} else {
			return new Vertex2d[] {
					new Vertex2d(-width, height, 0, 1),
					new Vertex2d(-width*0.7f, height, 0.15f, 1),
					new Vertex2d(width, height*0.1f, 1, 0.55f),
					new Vertex2d(width, -height*0.1f, 1, 0.45f),
					new Vertex2d(-width*0.7f, -height, 0.15f, 0),
					new Vertex2d(-width, -height, 0, 0)
			};
		}
	}
	
	@Override
	public void initialize(Window window) {
		w2d = window.createWorld2d();
		r2d = createRenderer(w2d).defaultProgram();
		
		r2d.setClearColor(0.6f, 0.7f, 0.8f);
		
		settings = false;
		
		w2d.addObjects(levelButtons);
		w2d.addObjects(blockers);
		
		w2d.addObject(rightButton);
		w2d.addObject(leftButton);
		w2d.addObject(backButton);
		w2d.addObject(settingsButton);
	}
	
	@Override
	public Scene update(float delta, Window window) {
		
		if (rightButton.onMouseUp()) {
			targetX += 1200;
		}
		
		if (leftButton.onMouseUp()) {
			targetX -= 1200;
		}
		
		r2d.getCamera().changeX((targetX-r2d.getCamera().getX())/15);
		float x = r2d.getCamera().getX();
		rightButton.setPosition(x+525, 0);
		leftButton.setPosition(x-525, 0);
		backButton.setPosition(x-530, -320);
		settingsButton.setPosition(x-440, -320);
		
		if (Math.abs(targetX-x) < 0.1f) {
			rightButton.setHidden(targetX >= maxX);
			leftButton.setHidden(targetX <= 0f);
		} else {
			rightButton.setHidden(true);
			leftButton.setHidden(true);
		}
		
		for (int i=0;i<blockers.length;i++) {
			blockers[i].setHidden(i<unlocked);
		}
		
		resetView(r2d);
		r2d.bind();
		r2d.clear();
		r2d.render();
		r2d.unbind();
		
		if (settingsButton.onMouseDown()) {
			Settings.component.resetVars();
			settings = true;
		}
		
		if (settings) {
			settings = !Settings.component.update(delta, window);
		}
		
		if (backButton.onMouseUp()) {
			return TitleScreen.scene;
		}
		
		boolean editor = true;
		for (int i=0;i<levelButtons.length;i++) {
			if (levelButtons[i].onMouseDown()) {
				if (editor) {
					GameWorld world = new GameWorld(window.createWorld3d());
					String path = R_LEVELS+"/level_"+(i+1)+".lvl";
					world.loadWorld(path);
					WorldEditor.scene.editor(world, path);
					return WorldEditor.scene;
				} else {
					GameWorld world = new GameWorld(window.createWorld3d());
					String path = R_LEVELS+"/level_"+(i+1)+".lvl";
					world.loadWorld(path);
					WorldPlayer.scene.game(world);
					return WorldPlayer.scene;
				}
			}
		}
		
		return null;
	}
	
}
