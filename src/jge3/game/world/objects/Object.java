
package jge3.game.world.objects;

import jge3.math.Vector3f;
import jge3.src3d.object.Object3d;

public abstract class Object {
	
	protected boolean hidden;
	protected Object3d[] objects;
	
	public Object3d[] getObjects() {
		return objects;
	}
	
	public void updateObject(float delta) {
		for (Object3d obj : objects) {
			obj.setHidden(hidden);
		}
		update(delta);
	}
	
	public abstract void update(float delta);
	
	public abstract Vector3f getPosition();
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
}
