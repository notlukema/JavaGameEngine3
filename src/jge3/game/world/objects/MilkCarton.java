
package jge3.game.world.objects;

import static jge3.game.ResourcePaths.*;

import jge3.game.world.objects.custom.FullbrightObject3d;
import jge3.math.Vector3f;
import jge3.src3d.io.ObjectImporter;
import jge3.src3d.object.Object3d;

public class MilkCarton extends Object {
	
	public static Object3d milk = new ObjectImporter(R_GAME+"/milk_carton/milk_carton.fbx").create();
	
	private Vector3f position;
	private float size;
	
	public MilkCarton(float x, float y, float z, float size) {
		position = new Vector3f(x, y, z);
		this.size = size;
		hidden = false;
		
		objects = new Object3d[] {new FullbrightObject3d(milk)};
	}
	
	@Override
	public void update(float delta) {
		objects[0].setPosition(position);
		objects[0].setScale(size, size, size);
		objects[0].changeRotY(delta*12);
		objects[0].restrictRotation();
	}
	
	@Override
	public Vector3f getPosition() {
		return position;
	}
	
	public float getSize() {
		return size;
	}
	
	public void setSize(float size) {
		this.size = size;
	}
	
}
