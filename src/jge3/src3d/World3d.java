
package jge3.src3d;

import java.util.ArrayList;
import java.util.List;

import jge3.common.interactable.Interactable;
import jge3.common.object.World;
import jge3.engine.display.Window;
import jge3.math.Matrix4f;
import jge3.math.Vector4f;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.lights.Light3d;

public class World3d implements World {
	
	private Window parent;
	
	private List<Object3d> objects;
	private List<Light3d> lights;
	
	private float touchingZ;
	
	public World3d(Window parent) {
		this.parent = parent;
		clear();
	}

	@Override
	public void clear() {
		objects = new ArrayList<Object3d>();
		lights = new ArrayList<Light3d>();
	}
	
	@Override
	public Window getParent() {
		return parent;
	}
	
	public boolean addObject(Object3d object) {
		parent.bind();
		return objects.add(object.create());
	}
	
	public boolean removeObject(Object3d object) {
		return objects.remove(object);
	}
	
	public boolean addLight(Light3d light) {
		return lights.add(light);
	}
	
	public boolean removeLight(Light3d light) {
		return lights.remove(light);
	}
	
	public void addObjects(Object3d[] objects) {
		parent.bind();
		for (int i=0;i<objects.length;i++) {
			this.objects.add(objects[i].create());
		}
	}
	
	public void removeObjects(Object3d[] objects) {
		for (int i=0;i<objects.length;i++) {
			this.objects.remove(objects[i]);
		}
	}
	
	public void addLights(Light3d[] lights) {
		parent.bind();
		for (int i=0;i<lights.length;i++) {
			this.lights.add(lights[i]);
		}
	}
	
	public void removeLights(Light3d[] lights) {
		for (int i=0;i<lights.length;i++) {
			this.lights.remove(lights[i]);
		}
	}
	
	public Object3d[] getObjects() {
		Object3d[] objects = new Object3d[this.objects.size()];
		for (int i=0;i<objects.length;i++) {
			objects[i] = this.objects.get(i);
		}
		return objects;
	}
	
	public int getObjectCount() {
		return objects.size();
	}
	
	public Object3d getObject(int i) {
		return objects.get(i);
	}
	
	public Light3d[] getLights() {
		Light3d[] lights = new Light3d[this.lights.size()];
		for (int i=0;i<lights.length;i++) {
			lights[i] = this.lights.get(i);
		}
		return lights;
	}
	
	public int getLightCount() {
		return lights.size();
	}
	
	public Light3d getLight(int i) {
		return lights.get(i);
	}
	
	public Interactable touching(float x, float y, float z, float dirx, float diry, float dirz) {
		return touching(x, y, y, dirx, diry, dirz);
	}
	
	public Interactable touching(float x, float y, float z, float dirx, float diry, float dirz, boolean calcMatrices) {
		float lowestZ = -1;
		Interactable touching = null;
		
		int length = objects.size();
		for (int i=0;i<length;i++) {
			Object3d obj = objects.get(i);
			if (obj.isHidden()) {
				continue;
			}
			
			if (calcMatrices) {
				obj.calculateObjectMatrix();
			}
			Vector4f pos = new Vector4f(x-obj.getX(), y-obj.getY(), z-obj.getZ(), 0f).mul(new Matrix4f(obj.getObjectMatrix().invert()));
			Vector4f dir = new Vector4f(dirx, diry, dirz, 0f).mul(obj.getObjectMatrix());
			
			float lowest;
			if ((lowest = obj.touching(pos.x, pos.y, pos.z, dir.x, dir.y, dir.z)) >= 0f) {
				if (lowestZ < 0 || lowest < lowestZ) {
					touching = obj;
					lowestZ = lowest;
				}
			}
		}
		
		touchingZ = lowestZ;
		return touching;
	}
	
	public float getTouchingZ() {
		return touchingZ;
	}
	
}
