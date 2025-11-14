
package jge3.test;

import static jge3.engine.display.WindowHints.*;

import jge3.common.image.Image;
import jge3.common.image.Texture;
import jge3.engine.Engine;
import jge3.engine.display.Cursor;
import jge3.engine.display.Monitor;
import jge3.engine.display.Window;
import jge3.engine.display.WindowHints;
import jge3.engine.graphics.Graphics2d;
import jge3.engine.graphics.Renderer3d;
import jge3.engine.input.Input;
import jge3.engine.input.InputValues;
import jge3.math.Vector2f;
import jge3.math.Vector4f;
import jge3.src3d.World3d;
import jge3.src3d.object.Mesh;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.lights.DirectionalLight;
import jge3.src3d.object.material.Material;
import jge3.src3d.object.shapes.Cube;
import jge3.utils.ImageUtils;

public class Test {
	
	public static void main(String[] args) {
		Engine engine = Engine.get();
		engine.init();
		
		Window window = new Window(400, 400, "window ayyyy", new WindowHints().addHint(TRANSPARENT, TRUE).addHint(VISIBLE, FALSE).addHint(FLOATING, TRUE));
		World3d world = new World3d(window);
		Renderer3d r3d = new Renderer3d(world).defaultProgram();
		Graphics2d g2d = new Graphics2d(window);
		window.setDecorated(false);
		window.setVisible(true);
		window.windowsHideTaskbarIcon();
		
		Input.get().addWindow(window);
		
		Vector2f windowpos = new Vector2f();
		float vx = 200;
		float vy = 200;
		
		Material material = new Material();
		material.setSpecularMap(new Texture(0));
		Mesh mesh = new Mesh(material);
		int size = 250;
		mesh.addObject(new Cube(size, size, size));
		Object3d object = new Object3d();
		object.addMesh(mesh);
		world.addObject(object);
		
		world.addLight(new DirectionalLight(0, 0, 1));
		
		r3d.setClearColor(0, 0, 0, 0);
		r3d.getCamera().setPosition(0, 0, -1000);
		r3d.setViewPort(-75, -75, window.getWidth()+75, window.getHeight()+75);
		
		int change = 0;
		material.setDiffuseMap(new Texture(createCubeImage(change, size)));
		
		float delta;
		long lastTime = System.nanoTime();
		long thisTime = lastTime;
		while (engine.getDisplayManager().getWindowCount() > 0) {
			thisTime = System.nanoTime();
			delta = (thisTime-lastTime)/1000000000f;
			lastTime = thisTime;
			engine.pre();
			
			windowpos.x += vx*delta;
			windowpos.y += vy*delta;
			if (windowpos.x < 0) {
				windowpos.x = 0;
				vx *= -1;
			}
			if (windowpos.y < 0) {
				windowpos.y = 0;
				vy *= -1;
			}
			Monitor m = engine.getDisplayManager().getPrimaryMonitor();
			if (windowpos.x + window.getWidth() > m.getWidth()) {
				windowpos.x = m.getWidth() - window.getWidth();
				vx *= -1;
			}
			if (windowpos.y + window.getHeight() > m.getHeight()) {
				windowpos.y = m.getHeight() - window.getHeight();
				vy *= -1;
			}
			window.setCursor(Cursor.TYPE_POINTING_HAND);
			
			if (Input.get().onMouseUp(InputValues.LEFT_MOUSE)) {
				change += 1;
				object.getMesh(0).getMaterial().setDiffuseMap(new Texture(createCubeImage(change, size)));
			}
			if (Input.get().onKeyReleased(InputValues.ESCAPE)) {
				window.setCloseRequested(true);
			}
			
			window.setPosition((int)windowpos.x, (int)windowpos.y);
			object.changeRotY(100*delta);
			
			r3d.bind();
			r3d.clear();
			r3d.render();
			r3d.unbind();
			
			g2d.bind();
			g2d.clearData();
			g2d.unbind();
			
			engine.post();
		}
		
		engine.terminate();
	}
	
	private static Image createCubeImage(int color, int size) {
		float val = color % 3;
		float r = val==0?1f:0f;
		float g = val==1?1f:0f;
		float b = val==2?1f:0f;
		return ImageUtils.createImage(size, size, new Vector4f(r, g, b, 1f));
	}
	
}
