
package jge3.game.world;

import jge3.game.world.objects.Platform;
import jge3.math.Vector3f;
import jge3.game.world.objects.Danger;
import jge3.game.world.objects.Object;

public class Player {
	
	private Vector3f position;
	private Vector3f size;
	private float camYPercent = 0.5f*0.75f;
	private float cameraY;
	
	private float gravity = 1000;
	private Vector3f velocity;
	
	private boolean onGround;
	private boolean dead;
	private float deathPercentage;
	
	public Player() {
		position = new Vector3f();
		size = new Vector3f();
		cameraY = size.y*camYPercent;
		
		velocity = new Vector3f();
	}
	
	public Player(Vector3f position, Vector3f size) {
		this.position = position.clone();
		this.size = size.clone();
		cameraY = size.y*camYPercent;
		
		velocity = new Vector3f();
		dead = false;
		deathPercentage = 1f;
	}
	
	public void update(float delta, GameWorld world) {
		velocity.y -= gravity*delta;
		
		onGround = false;
		
		if (dead) {
			deathPercentage -= deathPercentage/8f;
			position.x += velocity.x*delta*deathPercentage;
			position.y += velocity.y*delta*deathPercentage;
			position.z += velocity.z*delta*deathPercentage;
		} else {
			deathPercentage = 1f;
			
			position.x += velocity.x*delta;
			collision(world, true, false, false);
			position.y += velocity.y*delta;
			collision(world, false, true, false);
			position.z += velocity.z*delta;
			collision(world, false, false, true);
		}
		
		if (position.y < world.getDeathHeight() && velocity.y <= 0) {
			dead = true;
		}
	}
	
	private void collision(GameWorld world, boolean x, boolean y, boolean z) {
		int cx = (velocity.x > 0 ? -1 : 1);
		int cy = (velocity.y > 0 ? -1 : 1);
		int cz = (velocity.z > 0 ? -1 : 1);
		
		Object[] objects = world.getObjects();
		for (int i=0;i<objects.length;i++) {
			Object obj = objects[i];
			Vector3f pos = obj.getPosition();
			if (obj instanceof Platform) {
				Platform platform = (Platform)obj;
				Vector3f s = platform.getSize();
				float sx = s.x+size.x/2;
				float sy = s.y+size.y/2;
				float sz = s.z+size.z/2;
				if (Math.abs(pos.x-position.x)<sx && Math.abs(pos.y-position.y)<sy && Math.abs(pos.z-position.z)<sz) {
					if (x) {
						position.x = pos.x + sx*cx;
					}
					if (y) {
						position.y = pos.y + sy*cy;
						if (cy > 0) {
							onGround = true;
						}
						velocity.y = 0;
					}
					if (z) {
						position.z = pos.z + sz*cz;
					}
				}
			}
			if (obj instanceof Danger) {
				Danger danger = (Danger)obj;
				Vector3f s = danger.getSize();
				if (Math.abs(pos.x-position.x)<s.x+size.x/2&&Math.abs(pos.y-position.y)<s.y+size.y/2&&Math.abs(pos.z-position.z)<s.z+size.z/2) {
					dead = true;
				}
			}
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getSize() {
		return size;
	}
	
	public Vector3f getVelocity() {
		return velocity;
	}
	
	public float getCameraY() {
		return cameraY;
	}
	
	public Vector3f getCameraPosition() {
		return new Vector3f(position.x, position.y+cameraY, position.z);
	}
	
	public boolean onGround() {
		return onGround;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
}
