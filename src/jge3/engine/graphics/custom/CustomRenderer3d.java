
package jge3.engine.graphics.custom;

import jge3.engine.graphics.Renderer3d;
import jge3.src3d.World3d;
import jge3.src3d.object.camera.Camera3d;

public abstract class CustomRenderer3d extends Renderer3d {
	
	@Override
	public abstract CustomRenderer3d defaultProgram();
	
	public CustomRenderer3d(World3d world) {
		super(world);
	}
	
	public CustomRenderer3d(World3d world, Camera3d camera) {
		super(world, camera);
	}
	
}
