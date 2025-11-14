
package jge3.engine.graphics.custom;

import jge3.engine.graphics.Renderer2d;
import jge3.src2d.World2d;
import jge3.src2d.object.camera.Camera2d;

public abstract class CustomRenderer2d extends Renderer2d {
	
	@Override
	public abstract CustomRenderer2d defaultProgram();
	
	public CustomRenderer2d(World2d world) {
		super(world);
	}
	
	public CustomRenderer2d(World2d world, Camera2d camera) {
		super(world, camera);
	}
	
}
