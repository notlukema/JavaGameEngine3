
package jge3.test;

import static jge3.engine.input.InputValues.*;

import jge3.common.image.Texture;
import jge3.engine.display.*;
import jge3.engine.display.Window;
import jge3.engine.graphics.Renderer3d;
import jge3.engine.input.*;
import jge3.engine.managers.*;
import jge3.engine.input.Input;
import jge3.engine.managers.AudioManager;
import jge3.engine.managers.DisplayManager;
import jge3.engine.managers.GraphicsManager;
import jge3.src3d.World3d;
import jge3.src3d.io.ObjectImporter;
import jge3.src3d.object.*;
import jge3.src3d.object.camera.*;
import jge3.src3d.object.lights.*;
import jge3.src3d.object.material.*;
import jge3.src3d.object.shapes.*;
import jge3.utils.*;
import jge3.src3d.object.Mesh;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.camera.Camera3d;
import jge3.src3d.object.lights.DirectionalLight;
import jge3.src3d.object.lights.Light3d;
import jge3.src3d.object.material.Material;
import jge3.src3d.object.shapes.Cube;
import jge3.utils.MatrixUtils;

public class WindowTest {
	
	public static void main(String[] args) {
		
		// initialize
		DisplayManager dpm = DisplayManager.get();
		GraphicsManager glm = GraphicsManager.get();
		AudioManager alm = AudioManager.get();
		Input input = Input.get();
		
		dpm.initialize();
		glm.initialize();
		alm.initialize();
		
		Window window1 = dpm.createWindow(900, 900, "Window 1");
		Window window2 = dpm.createWindow(900, 900, "Window 2");
		World3d world1 = window1.createWorld3d();
		World3d world2 = window2.createWorld3d();
		Renderer3d glrenderer1 = glm.createRenderer(world1).defaultProgram();
		Renderer3d glrenderer2 = glm.createRenderer(world2).defaultProgram();
		Renderer3d glrenderer3 = glm.createRenderer(world1).defaultProgram();
		window1.setPosition(50, 100);
		window2.setPosition(1000, 100);
		input.addWindow(window1);
		input.addWindow(window2);
		
		Material m = new Material();
		m.setDiffuseMap(new Texture(255, 0, 0));
		Mesh mesh = new Mesh(m);
		mesh.addObject(new Cube(200, 200, 200));
		Object3d obj1 = new Object3d(0, 0, 0);
		obj1.addMesh(mesh);
		world1.addObject(obj1);
		
		Object3d torus = new ObjectImporter("resources/valentines-day-ornament/source/Valentine_s Day.fbx").createMultiple(1000)[0];
		torus.setRotX(270);
		torus.setRotY(180);
		world2.addObject(torus);
		
		// lights
		Light3d dir1 = new DirectionalLight(0, 0, 1);
		world1.addLight(dir1);

		Light3d dir2 = new DirectionalLight(0, 0, 1);
		world2.addLight(dir2);
		
		// set renderer stuff
        glrenderer1.setClearColor(0.1f, 0.7f, 0.8f, 1f);
        glrenderer1.getCamera().setZ(-1000);
        glrenderer3.setClearColor(0.1f, 0.7f, 0.8f, 1f);
        glrenderer3.getCamera().setZ(-2000);
        
        glrenderer2.setClearColor(0.6f, 0.7f, 0.8f, 1f);
        glrenderer2.getCamera().setZ(-1000);
        
        float fps = 0;
        int loops = 0;
        long thisTime = System.nanoTime();
        long lastTime = System.nanoTime();
        long lastSecond = System.nanoTime();
		while (dpm.getWindows().length > 0 && !input.isKeyPressed(keyValueOf("escape"))) {
			thisTime = System.nanoTime();
			float delta = (thisTime-lastTime)/1000000000f;
			lastTime = thisTime;
			dpm.pre();
			
			obj1.changeRotX(45*delta);
			obj1.changeRotY(45*delta);
			obj1.changeRotZ(45*delta);
			obj1.restrictRotation();
			
			torus.changeRotX(45*delta);
			torus.changeRotY(45*delta);
			torus.changeRotZ(45*delta);
			torus.restrictRotation();
			
			if (!window1.terminated()) {
				glrenderer1.bind();
				glrenderer1.clear();
				glrenderer1.render();
				glrenderer1.unbind();
				glrenderer3.bind();
				glrenderer3.clearData();
				glrenderer3.render();
				glrenderer3.unbind();
				window1.renderToScreen();
			}
			
			if (!window2.terminated()) {
				glrenderer2.bind();
				glrenderer2.clear();
				glrenderer2.render();
				glrenderer2.unbind();
				window2.renderToScreen();
			}
			
			dpm.destroyCloseRequestedWindows();
			
			glm.flush();
			
			loops++;
			
			if (thisTime-lastSecond >= 1000000000) {
				lastSecond = thisTime;
				fps = loops;
				System.out.println("FPS: "+fps);
				loops = 0;
			}
		}
		
		dpm.terminate();
		glm.terminate();
		alm.terminate();
	}
	
	public static void movement(Window window, Camera3d camera, float delta) {
		Input i = Input.get();
		float dx = 0;
		float dy = 0;
		float dz = 0;
		if (i.isKeyPressed(keyValueOf("w"))) {
			dz += 1000f*delta;
		}
		if (i.isKeyPressed(keyValueOf("s"))) {
			dz -= 1000f*delta;
		}
		if (i.isKeyPressed(keyValueOf("q"))) {
			dy -= 1000f*delta;
		}
		if (i.isKeyPressed(keyValueOf("e"))) {
			dy += 1000f*delta;
		}
		if (i.isKeyPressed(keyValueOf("a"))) {
			dx -= 1000f*delta;
		}
		if (i.isKeyPressed(keyValueOf("d"))) {
			dx += 1000f*delta;
		}
		camera.changePosition(MatrixUtils.rotate(dx, dy, dz, 0, camera.getRotY(), camera.getRotZ()));
		
		int x = window.getWidth()/2;
		int y = window.getHeight()/2;
		camera.changeRotY((float)(i.getMouseX()-x)/6);
		camera.changeRotX((float)(i.getMouseY()-y)/6);
		window.setCursorPosition(x, y);
	}
	
}
