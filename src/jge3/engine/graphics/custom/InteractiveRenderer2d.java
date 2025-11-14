
package jge3.engine.graphics.custom;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import jge3.common.interactable.Interactable;
import jge3.engine.display.Cursor;
import jge3.engine.graphics.Program;
import jge3.math.Matrix4f;
import jge3.math.Vector4f;
import jge3.src2d.World2d;
import jge3.src2d.object.camera.Camera2d;
import jge3.src2d.object.camera.Frustum2d;

public class InteractiveRenderer2d extends CustomRenderer2d {
	
	@Override
	public InteractiveRenderer2d defaultProgram() {
		Program.RENDER_2D_SHADERS(createProgram("Default Program"));
		return this;
	}
	
	public InteractiveRenderer2d(World2d world) {
		super(world);
	}
	
	public InteractiveRenderer2d(World2d world, Camera2d camera) {
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
		Frustum2d frustum = camera.getFrustum();
		Vector4f mousePos = new Vector4f(
				(cursor.getMouseX()-(right+left)/2f)/(right-left) * (float)(frustum.getRight()-frustum.getLeft()),
				(world.getParent().getHeight()-cursor.getMouseY()-(bottom+top)/2f)/(bottom-top) * (float)(frustum.getBottom()-frustum.getTop()),
				0f, 1f).mul(new Matrix4f(camera.getCameraMatrix()).invert());
		Interactable touching = world.touching(mousePos.x, mousePos.y, false);
		if (touching != null) {
			cursor.setTouching(touching, world.getTouchingZ());
		}
	}
	
}
