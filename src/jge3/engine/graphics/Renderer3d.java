
package jge3.engine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static jge3.src3d.object.Vertex3d.*;

import java.util.ArrayList;

import jge3.engine.managers.GraphicsManager;
import jge3.src3d.World3d;
import jge3.src3d.object.Mesh;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.camera.Camera3d;
import jge3.src3d.object.camera.Frustum3d;
import jge3.src3d.object.custom.CustomObject3d;
import jge3.src3d.object.lights.Light3d;
import jge3.src3d.object.material.Material;

public class Renderer3d extends Renderer {
	
	public Renderer3d defaultProgram() {
		Program.PHONG_SHADERS(createProgram("Default Program"));
		return this;
	}
	
	private boolean bindMaterial;
	
	protected World3d world;
	protected Camera3d camera;
	
	public Renderer3d(GraphicsManager manager, World3d world, Camera3d camera) {
		terminated = false;
		this.manager = manager;
		this.world = world;
		this.camera = camera;
		
		programs = new ArrayList<Program>();
		
		setClearColor(0f, 0f, 0f, 1.0f);
		setViewPort(camera.getFrustum());
	}
	
	public Renderer3d(World3d world) {
		this(world, new Camera3d(0, 0, world.getParent().getWidth(), world.getParent().getHeight()));
	}
	
	public Renderer3d(World3d world, Camera3d camera) {
		this(GraphicsManager.get(), world, camera);
		GraphicsManager.get().addRenderer(this);
	}
	
	@Override
	public void bind() {
		contextID = world.getParent().setGraphicsContext();
		
		glFrontFace(GL_CW);
		
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
			
			Light3d.reset();
			Light3d[] lights = world.getLights();
			for (int j=0;j<lights.length;j++) {
				lights[j].bindLight(boundProgramID);
			}
			Light3d.bindLightCount(boundProgramID);
			
			Object3d[] objects = world.getObjects();
			for (int j=0;j<objects.length;j++) {
				if (objects[j].isHidden()) {
					continue;
				}
				renderObject(objects[j]);
			}
			
			programs.get(i).unbind();
		}
	}
	
	public void renderObject(Object3d obj) {
		obj.calculateObjectMatrix();
		CustomObject3d cobj = obj instanceof CustomObject3d ? (CustomObject3d)obj : null;
		if (cobj != null) {
			cobj.preBind(this, boundProgram, world, camera);
		}
		obj.bindObjectMatrix(boundProgramID);
		obj.bindBoneMatrices(boundProgramID);
		for (int i=0;i<obj.getMeshCount();i++) {
			Mesh mesh = obj.getMesh(i);
			
			if (mesh.isHidden()) {
				continue;
			}
			
			if (filter != null) {
				if (filter.block(obj)) {
					continue;
				}
			}
			
			if (bindMaterial || true) {
				Material material = mesh.getMaterial();
				
				material.bindMaterial(boundProgramID);
				material.getDiffuseMap().bindTexture(boundProgramID, Material.DIFFUSE, 0, contextID);
			    material.getAmbientMap().bindTexture(boundProgramID, Material.AMBIENT, 1, contextID);
				material.getSpecularMap().bindTexture(boundProgramID, Material.SPECULAR, 2, contextID);
				material.getNormalMap().bindTexture(boundProgramID, Material.NORMAL, 3, contextID);
			}
			
			if (cobj != null) {
				cobj.preDraw(this, boundProgram, world, camera);
			}
			
			glBindVertexArray(mesh.getVAO());
			glEnableVertexAttribArray(POSITION);
			glEnableVertexAttribArray(NORMAL);
			glEnableVertexAttribArray(UV);
			glEnableVertexAttribArray(BONES);
			glEnableVertexAttribArray(WEIGHTS);
			
			glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
			
		}
		glDisableVertexAttribArray(POSITION);
		glDisableVertexAttribArray(NORMAL);
		glDisableVertexAttribArray(UV);
		glDisableVertexAttribArray(BONES);
		glDisableVertexAttribArray(WEIGHTS);
		
		glBindVertexArray(0);
	}
	
	public void setViewPort(Frustum3d frustum) {
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
	
	public Camera3d getCamera() {
		return camera;
	}
	
	public void setCamera(Camera3d camera) {
		this.camera = camera;
	}
	
	@Override
	public World3d getWorld() {
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
