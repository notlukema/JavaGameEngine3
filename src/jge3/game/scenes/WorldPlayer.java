package jge3.game.scenes;

import static jge3.game.PlatformerGame.*;
import static jge3.engine.input.InputValues.*;

import jge3.engine.display.Window;
import jge3.engine.graphics.Graphics3d;
import jge3.engine.graphics.Program;
import jge3.engine.graphics.Renderer2d;
import jge3.engine.graphics.Renderer3d;
import jge3.engine.graphics.custom.FullbrightRenderer3d;
import jge3.game.scenes.components.DeathScreen;
import jge3.game.scenes.components.InfoDisplay;
import jge3.game.scenes.components.Settings;
import jge3.game.world.GameWorld;
import jge3.game.world.Player;
import jge3.game.world.objects.Object;
import jge3.game.world.objects.Text;
import jge3.game.world.objects.custom.FullbrightObject3d;
import jge3.math.Vector3f;
import jge3.src2d.World2d;
import jge3.src2d.object.Object2d;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.camera.Camera3d;
import jge3.utils.MatrixUtils;

public class WorldPlayer extends Scene {
	
	public static final WorldPlayer scene = new WorldPlayer();
	
	private Window window;
	
	private World2d w2d;
	private GameWorld world;
	private Renderer2d r2d;
	private Renderer3d r3d;
	private Renderer3d b3d;
	private Graphics3d g3d;
	
	private Player player;
	private float airTime;
	
	private boolean mouseLock;
	private boolean settings;
	private int mouseCooldown;
	
	private boolean showData;
	
	private boolean editor;
	
	private WorldPlayer() {
	}
	
	public void game(GameWorld world) {
		game(world, false);
	}
	
	public void game(GameWorld world, boolean editor) {
		this.world = world;
		this.editor = editor;
		r3d = createRenderer(world.getWorld()).defaultProgram();
		g3d = createGraphics3d(window, r3d.getCamera());
		r3d.getCamera().setPosition(world.getSpawnpoint().clone());
		r3d.getProgram(0).setFilter(new Program.Filter() {
			@Override
			public boolean block(Object2d object) {
				return false;
			}
			@Override
			public boolean block(Object3d object) {
				return object instanceof FullbrightObject3d;
			}
		});
		b3d = new FullbrightRenderer3d(world.getWorld(), r3d.getCamera()).defaultProgram();
		b3d.getProgram(0).setFilter(new Program.Filter() {
			@Override
			public boolean block(Object2d object) {
				return false;
			}
			@Override
			public boolean block(Object3d object) {
				return !(object instanceof FullbrightObject3d);
			}
		});
		
		r3d.setClearColor(0.6f, 0.7f, 0.8f);
		g3d.setClearColor(0.6f, 0.7f, 0.8f);
		g3d.setFont(FONT);
		
		player = new Player(world.getSpawnpoint().clone(), new Vector3f(30, 100, 30));
		airTime = 0f;
		
		window.hideCursor();
		mouseCooldown = 10;
		mouseLock = true;
		settings = false;
		
		showData = false;
	}
	
	@Override
	public void initialize(Window window) {
		this.window = window;
		w2d = window.createWorld2d();
		r2d = createRenderer(w2d).defaultProgram();
		
		r2d.setClearColor(0.6f, 0.7f, 0.8f);
	}
	
	@Override
	public Scene update(float delta, Window window) {

		Camera3d camera = r3d.getCamera();
		if (!settings) {
			world.updateWorld(delta);
			player.update(delta, world);
			
			movement(delta, camera);
		}
		camera.setPosition(player.getCameraPosition());
		
		resetView(r3d);
		r3d.bind();
		r3d.clear();
		r3d.render();
		r3d.unbind();
		resetView(b3d);
		b3d.bind();
		b3d.render();
		b3d.unbind();
		
		resetView(g3d);
		g3d.bind();
		
		g3d.setColor(0, 255, 0);
		Vector3f p = world.getGoal();
		g3d.setColor(0, 255, 0);
		g3d.drawRect(p.x, p.y, p.z, 20, 20, 20);
		
		Object[] objects = world.getObjects();
		g3d.drawRect(p.x, p.y, p.z, 20, 20, 20);
		for (int i=0;i<objects.length;i++) {
			if (objects[i] instanceof Text) {
				Text text = (Text)objects[i];
				Vector3f pos = text.getPosition();
				g3d.setColor(220, 220, 220);
				g3d.drawString(text.getText(), (int)pos.x, (int)pos.y, (int)pos.z, (int)text.getSize(), 0);
			}
		}
		
		g3d.clearData();
		g3d.unbind();
		
		if (player.isDead()) {
			DeathScreen.component.setEditor(editor);
			boolean pressed = DeathScreen.component.update(delta, window);
			mouseLock = false;
			settings = false;
			window.showCursor();
			
			if (pressed) {
				if (DeathScreen.component.isBackPressed()) {
					if (editor) {
						WorldEditor.scene.resetVars();
						return WorldEditor.scene;
					} else {
						return SelectScreen.scene;
					}
				} else {
					Vector3f playerPos = player.getPosition();
					Vector3f spawnPos = world.getSpawnpoint();
					playerPos.x = spawnPos.x;
					playerPos.y = spawnPos.y;
					playerPos.z = spawnPos.z;
					
					Vector3f velocity = player.getVelocity();
					velocity.x = 0;
					velocity.y = 0;
					velocity.z = 0;
					
					player.setDead(false);
					window.hideCursor();
					mouseCooldown = 10;
					mouseLock = true;
					settings = false;
				}
			}
		} else {
			if (i.onKeyReleased(ESCAPE)) {
				mouseLock = !mouseLock;
				if (mouseLock) {
					window.hideCursor();
					mouseCooldown = 10;
					settings = false;
				} else {
					window.showCursor();
					settings = true;
				}
			}
		}
		
		if (settings) {
			Settings.component.resetVars();
			Settings.component.setBackEditorButton(editor);
			Settings.component.setBackSelectButton(!editor);
			settings = !Settings.component.update(delta, window);
			if (!settings) {
				switch (Settings.component.getButtonPressed()) {
				case Settings.EXIT:
					window.hideCursor();
					mouseLock = true;
					mouseCooldown = 10;
					break;
				case Settings.BACK:
					if (editor) {
						WorldEditor.scene.resetVars();
						WorldEditor.scene.getCamera().setPosition(player.getCameraPosition());
						WorldEditor.scene.getCamera().setRotation(camera.getRotation());
						return WorldEditor.scene;
					} else {
						return SelectScreen.scene;
					}
				}
			}
		}
		
		if (i.onKeyReleased(F3)) {
			showData = !showData;
		}
		
		if (showData) {
			InfoDisplay.component.update(delta, window);
		}
		
		return null;
	}
	
	public Camera3d getCamera() {
		return r3d.getCamera();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void movement(float delta, Camera3d camera) {
		float dx = 0;
		float dz = 0;
		float speed = 6000f;
		
		if (i.isKeyPressed(keyValueOf("w"))) {
			dz += speed*delta;
		}
		if (i.isKeyPressed(keyValueOf("s"))) {
			dz -= speed*delta;
		}
		if (i.isKeyPressed(keyValueOf("a"))) {
			dx -= speed*delta;
		}
		if (i.isKeyPressed(keyValueOf("d"))) {
			dx += speed*delta;
		}
		/*
		float rotx = camera.getRotX()%360;
		if (rotx > 180) {
			rotx -= 360;
		}
		
		if (rotx > 90) {
			camera.setRotX(90);
		}
		if (rotx < -90) {
			camera.setRotX(-90);
		}
		*/
		
		Vector3f d = MatrixUtils.rotate(dx, 0, dz, 0, camera.getRotY(), camera.getRotZ());
		
		Vector3f velocity = player.getVelocity();
		if (player.onGround()) {
			airTime = 0f;
			if (i.onKeyPressed(keyValueOf("space"))) {
				velocity.y = 400;
			}
			velocity.x += d.x;
			velocity.z += d.z;
			velocity.x *= 0.91f;
			velocity.z *= 0.91f;
		} else {
			airTime += delta;
			velocity.x += d.x/(10+airTime*15);
			velocity.z += d.z/(10+airTime*15);
			velocity.x *= 0.9999f;
			velocity.z *= 0.9999f;
		}
		
		if (mouseLock) {
			int x = window.getWidth()/2;
			int y = window.getHeight()/2;
			if (mouseCooldown <= 0) {
				camera.changeRotY((float)(i.getMouseX()-x)/6);
				camera.changeRotX((float)(i.getMouseY()-y)/6);
			}
			window.setCursorPosition(x, y);
			mouseCooldown--;
		}
	}
	
}
