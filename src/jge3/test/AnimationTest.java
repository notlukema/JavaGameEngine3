
package jge3.test;

import static jge3.engine.input.InputValues.*;

import jge3.common.image.Texture;
import jge3.engine.audio.Audio3d;
import jge3.engine.display.*;
import jge3.engine.display.Window;
import jge3.engine.graphics.Graphics3d;
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
import jge3.src3d.object.lights.*;
import jge3.src3d.object.material.*;
import jge3.src3d.object.Mesh;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.Vertex3d;
import jge3.src3d.object.camera.Camera3d;
import jge3.src3d.object.lights.DirectionalLight;
import jge3.src3d.object.lights.PointLight;
import jge3.src3d.object.material.Material;
import jge3.src3d.object.shapes.Cube;
import jge3.src3d.object.shapes.Triangle;
import jge3.utils.*;
import jge3.utils.MatrixUtils;

public class AnimationTest {
	
	public static void main(String[] args) {
		
		// initialize
		DisplayManager dpm = DisplayManager.get();
		GraphicsManager glm = GraphicsManager.get();
		AudioManager alm = AudioManager.get();
		Input input = Input.get();
		dpm.initialize();
		glm.initialize();
		alm.initialize();
		
		Window window1 = dpm.createWindow(1300, 900, "Animation Test");
		World3d world = window1.createWorld3d();
		Renderer3d glrenderer1 = glm.createRenderer(world).defaultProgram();
		Graphics3d g = new Graphics3d(window1, glrenderer1.getCamera());
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
		/*
		Object3d obj = new Object3d();
		Material m3 = new Material();
		Mesh mesh3 = new Mesh(m3);
		obj.addMesh(mesh3);
		mesh3.addObject(new Cube(50, 50, 50));
		world.addObject(obj);
*/
		Material m3 = new Material();
		Object3d obj = new Object3d(new Cube(50, 50, 50), m3);
		world.addObject(obj);
		
		//Object3d character = new ObjectImporter("resources/player/untitled.fbx").create();character.setScale(3f, 3f, 3f);
		Object3d character = new ObjectImporter("resources/dragon/source/dragon.fbx").create();
		//Object3d character = new ObjectImporter("resources/dragon2/fbx/Dragon 2.5_fbx.fbx").create();
		world.addObject(character);
		
		// directional light
		DirectionalLight sund = new DirectionalLight();
		sund.setColor(255, 174, 66);
		sund.setIntensity(0.8f);
		world.addLight(sund);
		
		// point light
		PointLight sunp = new PointLight(-700, 230, -1000);
		sunp.setColor(200, 200, 200);
		sunp.setIntensity(1f);
		sunp.setConstAtt(0.005f);
		sunp.setLinAtt(0.0008f);
		sunp.setQuadAtt(0.0000001f);
		world.addLight(sunp);
		
		//MatrixUtils.printMatrix(new Matrix4f().identity().translate(500, 20, 50).rotateX(2f).rotateZ(4.3f).scale(1.2f, 0.9f, 1f));
		//MatrixUtils.printMatrix(new Matrix4f().identity().translate(500, 20, 50).mul(new Matrix4f().identity().rotateX(2f).rotateZ(4.3f)).mul(new Matrix4f().identity().scale(1.2f, 0.9f, 1f)));
		
		// spot light
		/*
		SpotLight suns = new SpotLight(700, 230, -1000);
		suns.setColor(200, 200, 200);
		suns.setIntensity(1.2f);
		suns.setConstAtt(1f);
		suns.setLinAtt(0.00001f);
		suns.setQuadAtt(0f);
		suns.setConeAngle(45f);
		suns.setDirection(0, -1, 0);
		world.addLight(suns);
		*/
		
		System.out.println("WASD + QE to move.\nSpace to switch animations.\nZ to toggle bones.");
		
		// set renderer stuff
        glrenderer1.setClearColor(0.45f, 0.525f, 0.6f, 1f);
        glrenderer1.getCamera().setZ(-1000);

		int anim = 0;
        character.bindPose();
		
		boolean showBones = false;
        
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
			
			movement(window1, glrenderer1.getCamera(), delta);
			//suns.setPosition(glrenderer1.getCamera().getPosition());
			//suns.setDirection(VectorUtils.toNormal(glrenderer1.getCamera().getRotation()));
			sunp.setPosition(glrenderer1.getCamera().getPosition());
			
			character.getAnimation(anim).play(delta);
			//character.getAnimation(anim).toDefault();
			
			glrenderer1.bind();
			glrenderer1.clear();
			glrenderer1.render();
			glrenderer1.unbind();
			
			if (input.onKeyReleased(SPACE)) {
				character.bindPose();
				anim++;
				if (anim >= character.getAnimationCount()) {
					anim = 0;
				}
			}
			
			if (input.onKeyReleased(Z)) {
				showBones = !showBones;
			}
			
			if (showBones) {
				g.bind();
				g.setColor(0, 255, 0);
				g.clearData();
				character.visualize(g);
				g.unbind();
			}
			
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
			dz += 750f*delta;
		}
		if (i.isKeyPressed(keyValueOf("s"))) {
			dz -= 750f*delta;
		}
		if (i.isKeyPressed(keyValueOf("q"))) {
			dy -= 750f*delta;
		}
		if (i.isKeyPressed(keyValueOf("e"))) {
			dy += 750f*delta;
		}
		if (i.isKeyPressed(keyValueOf("a"))) {
			dx -= 750f*delta;
		}
		if (i.isKeyPressed(keyValueOf("d"))) {
			dx += 750f*delta;
		}
		
		camera.changePosition(MatrixUtils.rotate(dx, dy, dz, 0, camera.getRotY(), camera.getRotZ()));
		
		int x = window.getWidth()/2;
		int y = window.getHeight()/2;
		camera.changeRotY((float)(i.getMouseX()-x)/6);
		camera.changeRotX((float)(i.getMouseY()-y)/6);
		window.setCursorPosition(x, y);
	}
	
}
