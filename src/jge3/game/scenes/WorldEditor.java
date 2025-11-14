package jge3.game.scenes;

import static jge3.game.PlatformerGame.*;
import static jge3.engine.input.InputValues.*;
import static jge3.game.ResourcePaths.*;

import jge3.common.image.Image;
import jge3.engine.display.Window;
import jge3.engine.graphics.Graphics2d;
import jge3.engine.graphics.Graphics3d;
import jge3.engine.graphics.Program;
import jge3.engine.graphics.Renderer2d;
import jge3.engine.graphics.Renderer3d;
import jge3.engine.graphics.custom.FullbrightRenderer3d;
import jge3.engine.input.InputValues;
import jge3.game.world.GameWorld;
import jge3.game.world.WorldLoader;
import jge3.game.world.objects.Danger;
import jge3.game.world.objects.Object;
import jge3.game.world.objects.Platform;
import jge3.game.world.objects.Text;
import jge3.game.world.objects.custom.FullbrightObject3d;
import jge3.math.Vector2f;
import jge3.math.Vector3f;
import jge3.src2d.World2d;
import jge3.src2d.object.Object2d;
import jge3.src2d.object.interactable.RectButton;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.camera.Camera3d;
import jge3.utils.ImageUtils;
import jge3.utils.MatrixUtils;
import jge3.utils.VectorUtils;

public class WorldEditor extends Scene {
	
	public static final WorldEditor scene = new WorldEditor();
	
	private Window window;
	
	private String filePath;
	
	private World2d w2d;
	private GameWorld world;
	private Renderer2d r2d;
	private Renderer3d r3d;
	private Renderer3d b3d;
	private Graphics2d g2d;
	private Graphics3d g3d;
	
	private Vector2f mouseAnchor;
	private Vector3f rotationAnchor;
	
	private RectButton saveButton;
	private RectButton setSpawnpoint;
	private RectButton setMilkPos;
	private RectButton setGoal;
	private RectButton setDeathHeight;
	private RectButton newPlatform;
	private RectButton newDanger;
	private RectButton newText;
	
	// temporary because out of time
	private RectButton select;
	private RectButton move;
	private RectButton scale;
	private RectButton delete;
	private RectButton edit;
	private int editMode;
	private final int MOVE = 1;
	private final int SCALE = 2;
	
	private RectButton playFromSpot;
	private RectButton playFromSpawn;
	
	private Object selectedObject;
	private String text;
	private boolean editingText;
	
	private Image xPoint;
	private Image yPoint;
	private Image zPoint;
	private Image point;
	private Image selected;
	
	private WorldEditor() {
		xPoint = ImageUtils.loadImage(R_EDITOR+"/x_point.png");
		yPoint = ImageUtils.loadImage(R_EDITOR+"/y_point.png");
		zPoint = ImageUtils.loadImage(R_EDITOR+"/z_point.png");
		point = ImageUtils.loadImage(R_EDITOR+"/point.png");
		selected = ImageUtils.loadImage(R_EDITOR+"/selected.png");
		
		saveButton = new RectButton("Save", FONT, 45, 60, 60, 170);
		saveButton.setPosition(-520, 330);
		
		setSpawnpoint = new RectButton("Set Spawnpoint", FONT, 30, 60, 60, 170);
		setSpawnpoint.setPosition(-280, 350);
		
		setMilkPos = new RectButton("Set Milk Position", FONT, 30, 60, 60, 170);
		setMilkPos.setPosition(-280, 300);
		
		setGoal = new RectButton("Set Goal", FONT, 30, 60, 60, 170);
		setGoal.setPosition(-280, 250);
		
		setDeathHeight = new RectButton("Set Death Height", FONT, 30, 60, 60, 170);
		setDeathHeight.setPosition(-280, 200);
		
		newPlatform = new RectButton("Platform", FONT, 40, 60, 60, 170);
		newPlatform.setPosition(450, 330);
		
		newDanger = new RectButton("Danger", FONT, 40, 60, 60, 170);
		newDanger.setPosition(450, 270);
		
		newText = new RectButton("Text", FONT, 40, 60, 60, 170);
		newText.setPosition(450, 210);
		
		select = new RectButton("Select", FONT, 25, 60, 60, 170);
		select.setPosition(-520, -270);
		
		move = new RectButton("Move", FONT, 25, 60, 60, 170);
		move.setPosition(-520, -310);
		
		scale = new RectButton("Scale", FONT, 25, 60, 60, 170);
		scale.setPosition(-520, -350);;
		
		delete = new RectButton("Delete", FONT, 25, 60, 60, 170);
		delete.setPosition(-430, -350);
		
		edit = new RectButton("Edit", FONT, 25, 60, 60, 170);
		edit.setPosition(-430, -310);
		
		playFromSpot = new RectButton("Play from Spot", FONT, 25, 60, 60, 170);
		playFromSpot.setPosition(300, -310);
		
		playFromSpawn = new RectButton("Play from Spawnpoint", FONT, 25, 60, 60, 170);
		playFromSpawn.setPosition(300, -350);
	}
	
	public void editor(GameWorld world, String filePath) {
		this.world = world;
		this.filePath = filePath;
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
		
		resetVars();
	}
	
	public void resetVars() {
		edit.setHidden(true);
		editMode = MOVE;
		selectedObject = null;
		editingText = false;
	}
	
	@Override
	public void initialize(Window window) {
		this.window = window;
		w2d = window.createWorld2d();
		r2d = createRenderer(w2d).defaultProgram();
		g2d = createGraphics2d(window);
		
		w2d.addObject(saveButton);
		w2d.addObject(setSpawnpoint);
		w2d.addObject(setMilkPos);
		w2d.addObject(setGoal);
		w2d.addObject(setDeathHeight); 
		w2d.addObject(newPlatform);
		w2d.addObject(newDanger); 
		w2d.addObject(newText);
		
		w2d.addObject(select);
		w2d.addObject(move);
		w2d.addObject(scale);
		w2d.addObject(delete);
		w2d.addObject(edit);
		
		w2d.addObject(playFromSpot);
		w2d.addObject(playFromSpawn);
		
		r2d.setClearColor(0.6f, 0.7f, 0.8f);
	}
	
	@Override
	public Scene update(float delta, Window window) {
		
		world.updateWorld(delta);
		/*
		if (i.onKeyReleased(InputValues.ESCAPE)) {
			if (showMouse) {
				window.hideCursor();
				window.setCursorPosition(window.getWidth()/2, window.getHeight()/2);
				showMouse = false;
			} else {
				window.showCursor();
				showMouse = true;
			}
		}
		*/
		Camera3d camera = r3d.getCamera();
		
		edit.setHidden(!(selectedObject instanceof Text));
		
		if (editingText) {
			select.setHidden(true);
			move.setHidden(true);
			scale.setHidden(true);
			newPlatform.setHidden(true);
			newDanger.setHidden(true);
			newText.setHidden(true);
			int[] keys = i.getOnKeysPressed();
			for (int i=0;i<keys.length;i++) {
				String name = shiftIfShifting(keySymbolOf(keys[i]));
				if (name.length() == 1) {
					text += name;
				} else if (name.equals("enter")) {
					text += "\n";
				}
			}

			if (i.onKeyPressed(InputValues.BACKSPACE)) {
				if (text.length() > 0) {
					text = text.substring(0, text.length()-1);
				}
			}
			if (selectedObject instanceof Text) {
				((Text)selectedObject).setText(text);
			}
			if (i.onMouseDown(InputValues.LEFT_MOUSE)) {
				editingText = false;
			}
		} else {
			select.setHidden(false);
			move.setHidden(false);
			scale.setHidden(false);
			newPlatform.setHidden(false);
			newDanger.setHidden(false);
			newText.setHidden(false);
			movement(delta, camera);
		}
		
		if (i.onMouseDown(RIGHT_MOUSE)) {
			mouseAnchor = i.getMousePos();
			rotationAnchor = camera.getRotation();
		} else if (i.isMouseDown(RIGHT_MOUSE)) {
			float dy = (float)(i.getMouseX()-mouseAnchor.x)*0.6f;
			float dx = (float)(i.getMouseY()-mouseAnchor.y)*0.6f;
			float dz = 0;
			camera.setRotation(rotationAnchor.x+dx, rotationAnchor.y+dy, rotationAnchor.z+dz);
		}
		
		if (saveButton.onMouseDown()) {
			WorldLoader.saveWorld(filePath, world);
		}
		
		if (setSpawnpoint.onMouseDown()) {
			world.setSpawnpoint(camera.getPosition());
		}
		
		if (setMilkPos.onMouseDown()) {
			world.setMilkPos(camera.getPosition());
		}
		
		if (setGoal.onMouseDown()) {
			world.setGoal(camera.getPosition());
		}
		
		if (setDeathHeight.onMouseDown()) {
			world.setDeathHeight(camera.getPosition().y);
		}
		
		if (delete.onMouseDown()) {
			if (selectedObject != null) {
				world.removeObject(selectedObject);
				selectedObject = null;
			}
		}
		
		if (newPlatform.onMouseDown()) {
			Vector3f vec = VectorUtils.toNormal(camera.getRotation());
			float d = 500;
			selectedObject = new Platform(camera.getX()+vec.x*d, camera.getY()+vec.y*d, camera.getZ()+vec.z*d, 50, 50, 50);
			world.addObject(selectedObject);
		}
		
		if (newDanger.onMouseDown()) {
			Vector3f vec = VectorUtils.toNormal(camera.getRotation());
			float d = 500;
			selectedObject = new Danger(camera.getX()+vec.x*d, camera.getY()+vec.y*d, camera.getZ()+vec.z*d, 50, 50, 50);
			world.addObject(selectedObject);
		}
		
		if (newText.onMouseDown()) {
			Vector3f vec = VectorUtils.toNormal(camera.getRotation());
			float d = 500;
			selectedObject = new Text(camera.getX()+vec.x*d, camera.getY()+vec.y*d, camera.getZ()+vec.z*d, "", 50);
			world.addObject(selectedObject);
			editingText = true;
			text = "";
		}
		
		if (edit.onMouseDown()) {
			editingText = true;
			text = ((Text)selectedObject).getText();
		}
		
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
		
		Vector3f p = world.getSpawnpoint();
		g3d.setColor(255, 0, 0);
		g3d.drawRect(p.x, p.y, p.z, 20, 20, 20);
		
		g3d.setColor(0, 255, 0);
		p = world.getGoal();
		
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
		
		p = camera.getPosition();
		float y = world.getDeathHeight();
		g3d.setColor(255, 0, 0);
		g3d.drawLine(1000, y, 0, -1000, y, 0);
		g3d.drawLine(0, y, 1000, 0, y, -1000);
		g3d.drawLine(1000, y, 1000, 1000, y, -1000);
		g3d.drawLine(-1000, y, -1000, 1000, y, -1000);
		g3d.drawLine(-1000, y, -1000, -1000, y, 1000);
		g3d.drawLine(1000, y, 1000, -1000, y, 1000);
		
		g3d.clearData();
		for (int i=0;i<objects.length;i++) {
			Object obj = objects[i];
			Vector3f pos = obj.getPosition();
			
			if (obj == selectedObject) {
				float d = 50;
				g3d.setColor(200, 0, 0);
				g3d.drawLine(pos.x, pos.y, pos.z, pos.x+d, pos.y, pos.z);
				g3d.setColor(0, 200, 0);
				g3d.drawLine(pos.x, pos.y, pos.z, pos.x, pos.y+d, pos.z);
				g3d.setColor(0, 0, 200);
				g3d.drawLine(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z+d);
				g3d.drawBillboardImage(selected, pos.x, pos.y, pos.z, 0.2f);
				g3d.drawBillboardImage(xPoint, pos.x+d, pos.y, pos.z, 0.2f);
				g3d.drawBillboardImage(yPoint, pos.x, pos.y+d, pos.z, 0.2f);
				g3d.drawBillboardImage(zPoint, pos.x, pos.y, pos.z+d, 0.2f);
			} else {
				g3d.drawBillboardImage(point, pos.x, pos.y, pos.z, 0.2f);
			}
		}
		
		resetView(g2d);
		g2d.bind();
		
		if (editingText) {
			int size = 75;
			float hw = FONT.getStringWidth(text, size)/2f;
			float hh = FONT.getStringHeight(text, size)/2f;
			g2d.setColor(100, 100, 100, 100);
			g2d.fillRect(-hw-15, -hh-15, hw+15, hh);
			g2d.setColor(255, 255, 255);
			g2d.drawString(text, 0, 0, size, 0, 0);
		}
		
		g2d.unbind();
		
		if (select.onMouseDown()) {
			objects = world.getObjects();
			float closest = 800;
			selectedObject = null;
			for (Object obj : objects) {
				Vector3f pos = obj.getPosition();
				float d = (float)Math.sqrt(Math.pow(pos.x-camera.getX(), 2)+Math.pow(pos.y-camera.getY(), 2)+Math.pow(pos.z-camera.getZ(), 2));
				if (d < closest) {
					closest = d;
					selectedObject = obj;
				}
			}
		}
		
		if (move.onMouseDown()) {
			editMode = MOVE;
		}
		if (scale.onMouseDown()) {
			editMode = SCALE;
		}
		
		if (editMode == MOVE) {
			move.setScale(1.2f, 1.2f);
			scale.setScale(1f, 1f);
		} else {
			move.setScale(1f, 1f);
			scale.setScale(1.2f, 1.2f);
		}
		
		if (playFromSpot.onMouseDown()) {
			WorldPlayer.scene.game(world, true);
			Vector3f pos = WorldPlayer.scene.getPlayer().getPosition();
			pos.x = camera.getX();
			pos.y = camera.getY();
			pos.z = camera.getZ();
			WorldPlayer.scene.getCamera().setRotation(camera.getRotation());
			return WorldPlayer.scene;
		}
		
		if (playFromSpawn.onMouseDown()) {
			WorldPlayer.scene.game(world, true);
			return WorldPlayer.scene;
		}
		
		g3d.unbind();
		
		resetView(r2d);
		r2d.bind();
		r2d.clearData();
		r2d.render();
		r2d.unbind();
		
		return null;
	}
	
	public Camera3d getCamera() {
		return r3d.getCamera();
	}
	
	public void movement(float delta, Camera3d camera) {
		float dx = 0;
		float dy = 0;
		float dz = 0;
		float speed = 1500f;
		
		if (i.isKeyPressed(keyValueOf("w"))) {
			dz += speed*delta;
		}
		if (i.isKeyPressed(keyValueOf("s"))) {
			dz -= speed*delta;
		}
		if (i.isKeyPressed(keyValueOf("q")) || i.isKeyPressed(LEFT_SHIFT)) {
			dy -= speed*delta;
		}
		if (i.isKeyPressed(keyValueOf("e")) || i.isKeyPressed(SPACE)) {
			dy += speed*delta;
		}
		if (i.isKeyPressed(keyValueOf("a"))) {
			dx -= speed*delta;
		}
		if (i.isKeyPressed(keyValueOf("d"))) {
			dx += speed*delta;
		}
		
		if (selectedObject != null && i.isMouseDown(LEFT_MOUSE)) {
			if (editMode == MOVE) {
				Vector3f pos = selectedObject.getPosition();
				Vector3f change = MatrixUtils.rotate(dx, dy, dz, 0, camera.getRotY(), camera.getRotZ());
				pos.x += change.x*0.05;
				pos.y += change.y*0.05;
				pos.z += change.z*0.05;
			} else {
				if (selectedObject instanceof Platform) {
					Vector3f size = ((Platform)selectedObject).getSize();
					size.x += dx*0.02;
					size.y += dy*0.02;
					size.z += dz*0.02;
				} else if (selectedObject instanceof Danger) {
					Vector3f size = ((Danger)selectedObject).getSize();
					size.x += dx*0.02;
					size.y += dy*0.02;
					size.z += dz*0.02;
				} else if (selectedObject instanceof Text) {
					Text t = (Text)selectedObject;
					t.setSize(t.getSize()+dx*0.02f+dy*0.02f+dz*0.02f);
				}
			}
		} else {
			camera.changePosition(MatrixUtils.rotate(dx, dy, dz, 0, camera.getRotY(), camera.getRotZ()));
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
	}
	
}
