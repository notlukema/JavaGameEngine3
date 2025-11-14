
package jge3.game.world.objects;

import jge3.common.image.Texture;
import jge3.math.Vector3f;
import jge3.src3d.object.Mesh;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.material.Material;
import jge3.src3d.object.shapes.Cube;

public class Platform extends Object {
	
	private Vector3f position;
	private Vector3f size;
	
	private Mesh[] platformParts;
	
	public Platform(float x, float y, float z, float l, float h, float w) {
		position = new Vector3f(x, y, z);
		size = new Vector3f(l, h, w);
		hidden = false;
		
		objects = new Object3d[] {new Object3d()};
		
		Material m = new Material();
		m.setDiffuseMap(new Texture(50, 50, 200));
		platformParts = new Mesh[] {new Mesh(m)};
		platformParts[0].addObject(new Cube(1, 1, 1));
		
		for (Mesh mesh : platformParts) {
			objects[0].addMesh(mesh);
		}
	}
	
	@Override
	public void update(float delta) {
		objects[0].setPosition(position);
		objects[0].setScale(size);
	}
	
	@Override
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getSize() {
		return size;
	}
	
}
