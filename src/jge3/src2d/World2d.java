
package jge3.src2d;

import java.util.ArrayList;
import java.util.List;

import jge3.common.interactable.Interactable;
import jge3.common.object.World;
import jge3.engine.display.Window;
import jge3.math.Vector4f;
import jge3.src2d.object.Object2d;

public class World2d implements World {
	
	private Window parent;
	
	private List<Object2d> objects;
	
	private float touchingZ;
	
	public World2d(Window parent) {
		this.parent = parent;
		clear();
	}
	
	@Override
	public void clear() {
		objects = new ArrayList<Object2d>();
	}
	
	public Window getParent() {
		return parent;
	}
	
	public boolean addObject(Object2d object) {
		parent.bind();
		return objects.add(object.create());
	}
	
	public boolean removeObject(Object2d object) {
		return objects.remove(object);
	}
	
	public void addObjects(Object2d[] objects) {
		parent.bind();
		for (int i=0;i<objects.length;i++) {
			this.objects.add(objects[i].create());
		}
	}
	
	public void removeObjects(Object2d[] objects) {
		for (int i=0;i<objects.length;i++) {
			this.objects.remove(objects[i]);
		}
	}
	
	public Object2d[] getObjects() {
		Object2d[] objects = new Object2d[this.objects.size()];
		for (int i=0;i<objects.length;i++) {
			objects[i] = this.objects.get(i);
		}
		return objects;
	}
	
	public int getObjectCount() {
		return objects.size();
	}
	
	public Object2d getObject(int i) {
		return objects.get(i);
	}
	
	public Interactable touching(float x, float y) {
		return touching(x, y, true);
	}
	
	public Interactable touching(float x, float y, boolean calcMatrices) {
		int highestLayer = -1;
		Interactable touching = null;
		
		int length = objects.size();
		for (int i=0;i<length;i++) {
			Object2d obj = objects.get(i);
			if (obj.isHidden()) {
				continue;
			}
			
			if (obj.getLayer() > highestLayer) {
				
				Vector4f pos = new Vector4f(x, y, 0f, 1f);
				if (calcMatrices) {
					obj.calculateObjectMatrix();
				}
				pos.mul(obj.getObjectMatrix().invert());
				if (obj.touching(pos.x, pos.y)) {
					highestLayer = obj.getLayer();
					touching = obj;
				}
			}
			
		}
		
		touchingZ = Object2d.MAX_LAYERS-highestLayer;
		return touching;
	}
	
	public float getTouchingZ() {
		return touchingZ;
	}
	
}
