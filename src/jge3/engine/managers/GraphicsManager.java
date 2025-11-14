
package jge3.engine.managers;

import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GLCapabilities;

import jge3.engine.display.Window;
import jge3.engine.graphics.Graphics2d;
import jge3.engine.graphics.Graphics3d;
import jge3.engine.graphics.Renderer2d;
import jge3.engine.graphics.Renderer3d;
import jge3.src2d.World2d;
import jge3.src2d.object.camera.Camera2d;
import jge3.src3d.World3d;
import jge3.src3d.object.camera.Camera3d;

public class GraphicsManager {
	
	private static GraphicsManager manager = new GraphicsManager();
	
	public static GraphicsManager get() {
		return manager;
	}
	
	//

	private List<Renderer2d> renderer2ds;
	private List<Renderer3d> renderer3ds;
	
	private List<Graphics2d> graphics2ds;
	private List<Graphics3d> graphics3ds;
	
	private GraphicsManager() {
		renderer2ds = new ArrayList<Renderer2d>();
		renderer3ds = new ArrayList<Renderer3d>();
		
		graphics2ds = new ArrayList<Graphics2d>();
		graphics3ds = new ArrayList<Graphics3d>();
	}
	
	public void initialize() {
	}
	
	public GLCapabilities getCapabilities() {
		return DisplayManager.get().getGLCapabilities();
	}
	
	public void destroy(Renderer2d renderer) {
		renderer.freeUsing();
		renderer2ds.remove(renderer);
	}
	
	public void destroy(Renderer3d renderer) {
		renderer.freeUsing();
		renderer3ds.remove(renderer);
	}
	
	public void destroy(Graphics2d graphics) {
		graphics.freeUsing();
		graphics2ds.remove(graphics);
	}
	
	public void destroy(Graphics3d graphics) {
		graphics.freeUsing();
		graphics3ds.remove(graphics);
	}
	
	public Renderer2d createRenderer(World2d world) {
		return createRenderer(world, new Camera2d(0, 0, world.getParent().getWidth(), world.getParent().getHeight()));
	}
	
	public Renderer2d createRenderer(World2d world, Camera2d camera) {
		Renderer2d renderer = new Renderer2d(this, world, camera);
		renderer2ds.add(renderer);
		return renderer;
	}
	
	public Renderer3d createRenderer(World3d world) {
		return createRenderer(world, new Camera3d(0, 0, world.getParent().getWidth(), world.getParent().getHeight()));
	}
	
	public Renderer3d createRenderer(World3d world, Camera3d camera) {
		Renderer3d renderer = new Renderer3d(this, world, camera);
		renderer3ds.add(renderer);
		return renderer;
	}
	
	public void addRenderer(Renderer2d renderer) {
		renderer2ds.add(renderer);
	}
	
	public void addRenderer(Renderer3d renderer) {
		renderer3ds.add(renderer);
	}
	
	public Graphics2d createGraphics2d(Window window) {
		return createGraphics2d(window, new Camera2d(0, 0, window.getWidth(), window.getHeight()));
	}
	
	public Graphics2d createGraphics2d(Window window, Camera2d camera) {
		Graphics2d graphics = new Graphics2d(this, window, camera);
		graphics2ds.add(graphics);
		return graphics;
	}
	
	public Graphics3d createGraphics3d(Window window) {
		return createGraphics3d(window, new Camera3d(0, 0, window.getWidth(), window.getHeight()));
	}
	
	public Graphics3d createGraphics3d(Window window, Camera3d camera) {
		Graphics3d graphics = new Graphics3d(this, window, camera);
		graphics3ds.add(graphics);
		return graphics;
	}
	
	public void addGraphics(Graphics2d graphics) {
		graphics2ds.add(graphics);
	}
	
	public void addGraphics(Graphics3d graphics) {
		graphics3ds.add(graphics);
	}
	
	public Renderer2d[] getRenderer2ds() {
		Renderer2d[] renderers = new Renderer2d[renderer2ds.size()];
		for (int i=0;i<renderers.length;i++) {
			renderers[i] = renderer2ds.get(i);
		}
		return renderers;
	}
	
	public int getRenderer2dCount() {
		return renderer2ds.size();
	}
	
	public Renderer2d getRenderer2d(int i) {
		return renderer2ds.get(i);
	}
	
	public Renderer3d[] getRenderer3ds() {
		Renderer3d[] renderers = new Renderer3d[renderer3ds.size()];
		for (int i=0;i<renderers.length;i++) {
			renderers[i] = renderer3ds.get(i);
		}
		return renderers;
	}
	
	public int getRenderer3dCount() {
		return renderer3ds.size();
	}
	
	public Renderer3d getRenderer3d(int i) {
		return renderer3ds.get(i);
	}
	
	public Graphics2d[] getGraphics2ds() {
		Graphics2d[] graphics = new Graphics2d[graphics2ds.size()];
		for (int i=0;i<graphics.length;i++) {
			graphics[i] = graphics2ds.get(i);
		}
		return graphics;
	}
	
	public int getGraphics2dCount() {
		return graphics2ds.size();
	}
	
	public Graphics2d getGraphics2d(int i) {
		return graphics2ds.get(i);
	}
	
	public Graphics3d[] getGraphics3ds() {
		Graphics3d[] graphics = new Graphics3d[graphics3ds.size()];
		for (int i=0;i<graphics.length;i++) {
			graphics[i] = graphics3ds.get(i);
		}
		return graphics;
	}
	
	public int getGraphics3dCount() {
		return graphics3ds.size();
	}
	
	public Graphics3d getGraphics3d(int i) {
		return graphics3ds.get(i);
	}
	
	public int getMaxVertexUniforms() {
		return GL_MAX_VERTEX_UNIFORM_COMPONENTS;
	}
	
	public int getMaxFragmentUniforms() {
		return GL_MAX_FRAGMENT_UNIFORM_COMPONENTS;
	}
	
	public void flush() {
		glFlush();
	}
	
	public void finish() {
		glFinish();
	}
	
	public void terminate() {
		while(renderer2ds.size()>0) {
			destroy(renderer2ds.get(0));
		}
		while(renderer3ds.size()>0) {
			destroy(renderer3ds.get(0));
		}
		while(graphics2ds.size()>0) {
			destroy(graphics2ds.get(0));
		}
	}
	
}
