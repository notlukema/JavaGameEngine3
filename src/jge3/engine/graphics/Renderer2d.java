
package jge3.engine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static jge3.src2d.object.Vertex2d.*;

import java.util.ArrayList;

import jge3.engine.managers.GraphicsManager;
import jge3.src2d.World2d;
import jge3.src2d.object.Object2d;
import jge3.src2d.object.Sprite;
import jge3.src2d.object.camera.Camera2d;
import jge3.src2d.object.camera.Frustum2d;
import jge3.src2d.object.custom.CustomObject2d;

public class Renderer2d extends Renderer {
	
	public Renderer2d defaultProgram() {
		Program.RENDER_2D_SHADERS(createProgram("Default Program"));
		return this;
	}
	
	protected World2d world;
	protected Camera2d camera;
	
	public Renderer2d(GraphicsManager manager, World2d world, Camera2d camera) {
		terminated = false;
		this.manager = manager;
		this.world = world;
		this.camera = camera;
		
		programs = new ArrayList<Program>();
		
		setClearColor(0f, 0f, 0f, 1.0f);
		setViewPort(camera.getFrustum());
	}
	
	public Renderer2d(World2d world) {
		this(world, new Camera2d(0, 0, world.getParent().getWidth(), world.getParent().getHeight()));
	}
	
	public Renderer2d(World2d world, Camera2d camera) {
		this(GraphicsManager.get(), world, camera);
		GraphicsManager.get().addRenderer(this);
	}
	
	@Override
	public void bind() {
		contextID = world.getParent().setGraphicsContext();
		
		glViewport(left, top, right-left, bottom-top);
	}
	
	public void render() {
		int count = programs.size();
		if (count == 0) {
			return;
		}
		
		camera.calcCameraMatrix();
		for (int i=0;i<count;i++) {
			boundProgram = programs.get(i);
			boundProgram.bindProgram();
			boundProgramID = boundProgram.getID();
			boundProgramName = boundProgram.getName();
			filter = boundProgram.getFilter();
			isGlobal = boundProgram.isGlobal();
			setSettings(boundProgram.getSettings());
			camera.bindProjectionMatrix(boundProgramID);
			camera.bindCameraMatrix(boundProgramID);
			
			Object2d[] objects = world.getObjects();
			for (int j=0;j<objects.length;j++) {
				if (objects[j].isHidden()) {
					continue;
				}
				renderObject(objects[j]);
			}
			
			boundProgram.unbind();
		}
	}
	
	public void renderObject(Object2d obj) {
		obj.calculateObjectMatrix();
		CustomObject2d cobj = obj instanceof CustomObject2d ? (CustomObject2d)obj : null;
		if (cobj != null) {
			cobj.preBind(this, boundProgram, world, camera);
		}
		obj.bindObjectMatrix(boundProgramID);
		for (int i=0;i<obj.getSpriteCount();i++) {
			Sprite sprite = obj.getSprite(i);
			
			if (sprite.isHidden()) {
				continue;
			}
			
			if (filter != null) {
				if (filter.block(obj)) {
					continue;
				}
			}
			
			if (bindMaterial) {
				sprite.getTexture().bindTexture(boundProgramID, Sprite.TEXTURE, 0, contextID);
			}

			if (cobj != null) {
				cobj.preDraw(this, boundProgram, world, camera);
			}
			
			glBindVertexArray(sprite.getVAO());
			glEnableVertexAttribArray(POSITION);
			glEnableVertexAttribArray(UV);
			
			glDrawElements(GL_TRIANGLES, sprite.getVertexCount(), GL_UNSIGNED_INT, 0);
		}
		glDisableVertexAttribArray(POSITION);
		glDisableVertexAttribArray(UV);
		
		glBindVertexArray(0);
	}
	
	public void setViewPort(Frustum2d frustum) {
		left = frustum.getLeft();
		top = frustum.getTop();
		right = frustum.getRight();
		bottom = frustum.getBottom();
	}
	
	public void resetView() {
		long id = world.getParent().getID();
		setViewPort(id);
		camera.getFrustum().setSize(id);
	}
	
	public Camera2d getCamera() {
		return camera;
	}
	
	public void setCamera(Camera2d camera) {
		this.camera = camera;
	}
	
	@Override
	public World2d getWorld() {
		return world;
	}
	
	@Override
	public void destroy() {
		manager.destroy(this);
		while (programs.size() > 0) {
			destroyProgram(programs.remove(0));
		}
	}
	
}
