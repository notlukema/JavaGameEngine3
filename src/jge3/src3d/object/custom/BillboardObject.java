
package jge3.src3d.object.custom;

import jge3.engine.graphics.Program;
import jge3.engine.graphics.Renderer3d;
import jge3.math.Matrix3f;
import jge3.math.Matrix4f;
import jge3.src3d.World3d;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.camera.Camera3d;

public class BillboardObject extends CustomObject3d {
	
	public BillboardObject() {
		super();
	}
	
	public BillboardObject(Object3d object) {
		super(object);
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public void drawData(Renderer3d renderer, Program program, World3d world, Camera3d camera) {
		finalMatrix = new Matrix4f(new Matrix3f(camera.getCameraMatrix()).transpose()).mul(finalMatrix);
	}
	
	@Override
	public void preDraw(Renderer3d renderer, Program program, World3d world, Camera3d camera) {
	}
	
}
