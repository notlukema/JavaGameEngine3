
package jge3.test;

import static jge3.engine.display.WindowHints.*;
import static jge3.engine.input.InputValues.*;

import jge3.common.image.Texture;
import jge3.engine.audio.Audio3d;
import jge3.engine.display.*;
import jge3.engine.display.Window;
import jge3.engine.graphics.Renderer3d;
import jge3.engine.input.*;
import jge3.engine.managers.*;
import jge3.engine.input.Input;
import jge3.engine.managers.AudioManager;
import jge3.engine.managers.DisplayManager;
import jge3.engine.managers.GraphicsManager;
import jge3.math.Vector2f;
import jge3.math.Vector3f;
import jge3.src3d.World3d;
import jge3.src3d.io.ObjectImporter;
import jge3.src3d.object.*;
import jge3.src3d.object.camera.*;
import jge3.src3d.object.Mesh;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.Vertex3d;
import jge3.src3d.object.camera.Camera3d;
import jge3.src3d.object.custom.BillboardObject;
import jge3.src3d.object.lights.*;
import jge3.src3d.object.material.*;
import jge3.src3d.object.shapes.*;
import jge3.utils.*;
import jge3.src3d.object.lights.AmbientLight;
import jge3.src3d.object.lights.DirectionalLight;
import jge3.src3d.object.lights.PointLight;
import jge3.src3d.object.lights.SpotLight;
import jge3.src3d.object.material.Material;
import jge3.src3d.object.shapes.Triangle;
import jge3.utils.MatrixUtils;
import jge3.utils.VectorUtils;

public class CustomObjectTest {
	
	public static void main(String[] args) {
		
		// initialize
		DisplayManager dpm = DisplayManager.get();
		GraphicsManager glm = GraphicsManager.get();
		AudioManager alm = AudioManager.get();
		Input input = Input.get();
		dpm.initialize();
		glm.initialize();
		alm.initialize();
		
		// create window and renderer
		//long[] monitors = dpm.getMonitors();
		@SuppressWarnings("unused")
		int a = RESIZABLE;
		Window window1 = dpm.createWindow(1300, 900, "Import Test");
		World3d world = window1.createWorld3d();
		Renderer3d glrenderer1 = glm.createRenderer(world).defaultProgram();
		Audio3d alaudio1 = alm.createAudioPlayer(world, glrenderer1.getCamera());
		
		input.addWindow(window1);
		window1.hideCursor();
		
		Material m2 = new Material();
		m2.setDiffuseMap(new Texture(200, 200, 200));
		Mesh mesh2 = new Mesh(m2);
		Vertex3d v1 = new Vertex3d(new Vector3f(-5000, -1000, -5000), new Vector3f(0, 1, 0), new Vector2f(0, 0));
		Vertex3d v2 = new Vertex3d(new Vector3f(-5000, -1000, 5000), new Vector3f(0, 1, 0), new Vector2f(0, 0));
		Vertex3d v3 = new Vertex3d(new Vector3f(5000, -1000, 5000), new Vector3f(0, 1, 0), new Vector2f(0, 0));
		Vertex3d v4 = new Vertex3d(new Vector3f(5000, -1000, -5000), new Vector3f(0, 1, 0), new Vector2f(0, 0));
		
		mesh2.addObject(new Triangle(new Vertex3d[] {v1, v2, v3}));
		mesh2.addObject(new Triangle(new Vertex3d[] {v1, v3, v4}));
		Object3d obj2 = new Object3d(0, 0, 0);
		obj2.addMesh(mesh2);
		world.addObject(obj2);
		
		BillboardObject torus = new BillboardObject(new ObjectImporter("resources/valentines-day-ornament/source/Valentine_s Day.fbx").createMultiple(1000)[0]);
		
		torus.setRotX(270);
		torus.setRotY(180);
		torus.setPosition(500, 200, 1000);
		world.addObject(torus);
		
		//Object3d[] apples = new ObjectImporter("resources/apples/apple.fbx").create(100);
		//world.addObjects(apples);
		
		//Object3d person = new ObjectImporter("resources/LowPolyHuman/BaseMesh.fbx").create(300)[0];
		//world.addObject(person);
		
		// ambient light
		AmbientLight suna = new AmbientLight();
		suna.setColor(255, 0, 0);
		suna.setIntensity(0.1f);
		world.addLight(suna);
		
		// directional light
		double sunDir = 0;
		DirectionalLight sund = new DirectionalLight();
		sund.setColor(255, 174, 66);
		sund.setIntensity(0.5f);
		world.addLight(sund);
		
		// point light
		PointLight sunp = new PointLight(-700, 230, -1000);
		sunp.setColor(200, 200, 200);
		sunp.setIntensity(1f);
		sunp.setConstAtt(0.005f);
		sunp.setLinAtt(0.0008f);
		sunp.setQuadAtt(0.0000001f);
		world.addLight(sunp);
		
		// spot light
		SpotLight suns = new SpotLight(700, 230, -1000);
		suns.setColor(200, 200, 200);
		suns.setIntensity(3f);
		suns.setLinAtt(0.00005f);
		suns.setConeAngle(40f);
		suns.setDirection(0, -1, 0);
		world.addLight(suns);
		
		// set renderer stuff
        glrenderer1.setClearColor(0.06f, 0.07f, 0.08f, 1f);
        glrenderer1.getCamera().setZ(-1000);
        
        // sound
        //radioSong.setRolloff(0.01f);
        
        float fps = 0;
        int loops = 0;
        long thisTime = System.nanoTime();
        long lastTime = System.nanoTime();
        long lastSecond = System.nanoTime();
		while (dpm.getWindows().length > 0) {
			thisTime = System.nanoTime();
			float delta = (thisTime-lastTime)/1000000000f;
			lastTime = thisTime;
			dpm.pre();
			
			Vector3f last = glrenderer1.getCamera().getPosition();
			movement(window1, glrenderer1.getCamera(), delta);
			Vector3f current = glrenderer1.getCamera().getPosition();
			sund.setDirection(0, (float)Math.cos(Math.toRadians(sunDir)), (float)Math.sin(Math.toRadians(sunDir)));
			suns.setPosition(glrenderer1.getCamera().getPosition());
			suns.setDirection(VectorUtils.toNormal(glrenderer1.getCamera().getRotation()));
			
			sunDir += 45*delta;

			//torus.changeRotX(45*delta);
			//torus.changeRotY(45*delta);
			//torus.changeRotZ(45*delta);
			//torus.changeZ(-500*delta);
			
			glrenderer1.bind();
			glrenderer1.clear();
			glrenderer1.render();
			glrenderer1.unbind();
			
			alaudio1.setListenerVelocity(current.x-last.x, current.y-last.y, current.z-last.z);
			
			alaudio1.update();
			
			if (input.onKeyReleased(keyValueOf("escape"))) {
				window1.setCloseRequested(true);
			}
			
			window1.renderToScreen();
			
			dpm.destroyCloseRequestedWindows();

			//glm.flush();
			//dpm.renderToScreens();
			
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
