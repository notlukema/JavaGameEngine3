
package jge3.game.world.objects.custom;

import jge3.engine.graphics.Program;
import jge3.engine.graphics.Renderer3d;
import jge3.src3d.World3d;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.camera.Camera3d;
import jge3.src3d.object.custom.CustomObject3d;

public class FullbrightObject3d extends CustomObject3d {
	
	public FullbrightObject3d() {
		super();
	}
	
	public FullbrightObject3d(Object3d object) {
		super(object);
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public void drawData(Renderer3d renderer, Program program, World3d world, Camera3d camera) {
	}
	
	@Override
	public void preDraw(Renderer3d renderer, Program program, World3d world, Camera3d camera) {
	}
	
}
