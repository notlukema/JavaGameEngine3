
package jge3.engine.graphics.custom;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import jge3.common.interactable.Interactable;
import jge3.engine.display.Cursor;
import jge3.engine.graphics.Program;
import jge3.math.Vector3f;
import jge3.math.Vector4f;
import jge3.src3d.World3d;
import jge3.src3d.object.camera.Camera3d;
import jge3.src3d.object.camera.Frustum3d;
import jge3.utils.VectorUtils;

public class InteractiveRenderer3d extends CustomRenderer3d {
	
	public InteractiveRenderer3d defaultProgram() {
		Program.PHONG_SHADERS(createProgram("Default Program"));
		return this;
	}
	
	public InteractiveRenderer3d(World3d world) {
		super(world);
	}
	
	public InteractiveRenderer3d(World3d world, Camera3d camera) {
		super(world, camera);
	}
	
	@Override
	public void clearScreen() {
		glClearColor(r, g, b, a);
		glClear(GL_COLOR_BUFFER_BIT);
		Cursor.get().resetTouch();
	}
	
	@Override
	public void clearData() {
		glClear(GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		Cursor.get().resetDepth();
	}
	
	@Override
	public void render() {
		super.render();
		
		Cursor.GlobalCursor cursor = Cursor.get();
		Frustum3d frustum = camera.getFrustum();
		float width = world.getParent().getWidth();
		float height = world.getParent().getHeight();
		Vector4f mousePos = new Vector4f(
				(cursor.getMouseX()-left) * (float)(frustum.getRight()-frustum.getLeft())/(right-left) - width/2f,
				(height-cursor.getMouseY()-top) * (float)(frustum.getBottom()-frustum.getTop())/(bottom-top) - height/2f,
				0f, 1f);
		float mouseX = mousePos.x;
		float mouseY = mousePos.y;
		mouseX = mouseY;
		mouseY = mouseX;
		
		Vector3f pos = camera.getPosition();
		Vector3f rot = VectorUtils.toNormal(camera.getRotation());
		
		Interactable touching = world.touching(pos.x, pos.y, pos.z, rot.x, rot.y, rot.z, false);
		if (touching != null) {
			cursor.setTouching(touching, world.getTouchingZ());
		}
	}
	
}
