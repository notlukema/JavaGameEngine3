
package jge3.game.world;

import java.util.ArrayList;
import java.util.List;

import jge3.src3d.World3d;
import jge3.game.world.objects.MilkCarton;
import jge3.game.world.objects.Object;
import jge3.math.Vector3f;

public class GameWorld {
	
	private World3d world;
	
	private Vector3f spawnpoint;
	private Vector3f milk;
	private Vector3f goal;
	
	private MilkCarton milkCarton;
	private boolean grabbedMilk;
	
	private float deathHeight;
	
	private List<Object> objects;
	
	public GameWorld(World3d world) {
		this.world = world;
		spawnpoint = new Vector3f();
		milk = new Vector3f();
		goal = new Vector3f();
		deathHeight = -1000;
		objects = new ArrayList<Object>();
		milkCarton = new MilkCarton(milk.x, milk.y, milk.z, 3);
		addObject(milkCarton);
		
		resetWorld();
	}
	
	public void resetWorld() {
		grabbedMilk = false;
	}
	
	public void loadWorld(String path) {
		WorldLoader.loadWorld(path, this);
	}
	
	public void saveWorld(String path) {
		WorldLoader.saveWorld(path, this);
	}
	
	public boolean addObject(Object object) {
		world.addObjects(object.getObjects());
		return objects.add(object);
	}
	
	public boolean removeObject(Object object) {
		world.removeObjects(object.getObjects());
		return objects.remove(object);
	}
	
	public void updateWorld(float delta) {
		Vector3f pos = milkCarton.getPosition();
		pos.x = milk.x;
		pos.y = milk.y;
		pos.z = milk.z;
		milkCarton.setHidden(grabbedMilk);
		for (Object obj : objects) {
			obj.updateObject(delta);
		}
	}
	
	public Object[] getObjects() {
		Object[] objects = new Object[this.objects.size()];
		for (int i=0;i<objects.length;i++) {
			objects[i] = this.objects.get(i);
		}
		return objects;
	}
	
	public int getObjectCount() {
		return objects.size();
	}
	
	public Object getObject(int i) {
		return objects.get(i);
	}
	
	public Vector3f getSpawnpoint() {
		return spawnpoint;
	}
	
	public void setSpawnpoint(Vector3f pos) {
		spawnpoint = pos.clone();
	}
	
	public Vector3f getMilkPos() {
		return milk;
	}
	
	public void setMilkPos(Vector3f pos) {
		milk = pos.clone();
	}
	
	public Vector3f getGoal() {
		return goal;
	}
	
	public void setGoal(Vector3f pos) {
		goal = pos.clone();
	}
	
	public float getDeathHeight() {
		return deathHeight;
	}
	
	public void setDeathHeight(float height) {
		deathHeight = height;
	}
	
	public World3d getWorld() {
		return world;
	}
	
}
