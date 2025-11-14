
package jge3.engine.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

import jge3.engine.managers.AudioManager;
import jge3.math.Vector3f;
import jge3.src3d.World3d;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.camera.Camera3d;
import jge3.utils.VectorUtils;

public class Audio3d {
	
	public static final int INVERSE = AL_INVERSE_DISTANCE
						,INVERSE_CLAMPED = AL_INVERSE_DISTANCE_CLAMPED
						,LINEAR = AL_LINEAR_DISTANCE
						,LINEAR_CLAMPED = AL_LINEAR_DISTANCE_CLAMPED
						,EXPONENTIAL = AL_EXPONENT_DISTANCE
						,EXPONENTIAL_CLAMPED = AL_EXPONENT_DISTANCE_CLAMPED;
	
	//
	
	private boolean terminated;
	private AudioManager manager;
	
	private long contextID;
	
	private World3d world;
	private Camera3d camera;
	
	private int distanceModel;
	
	private float gain;
	private Vector3f velocity;
	
	public Audio3d(AudioManager manager, World3d world, Camera3d camera) {
		terminated = false;
		this.manager = manager;
		
		this.world = world;
		this.camera = camera;
		
		distanceModel = INVERSE;
		
		gain = 1f;
		velocity = new Vector3f();
	}
	
	public Audio3d(World3d world) {
		this(world, null);
	}
	
	public Audio3d(World3d world, Camera3d camera) {
		this(AudioManager.get(), world, camera);
		AudioManager.get().addAudioPlayer(this);
	}
	
	public void freeUsing() {
		terminated = true;
	}
	
	public void update() {
		contextID = world.getParent().setAudioContext();
		
		alDistanceModel(distanceModel);
		
		if (camera != null) {
			alListener3f(AL_POSITION, camera.getX(), camera.getY(), -camera.getZ());
			alListener3f(AL_VELOCITY, velocity.x, velocity.y, velocity.z);
			
			float[] orientation = new float[6];
			
			Vector3f at = VectorUtils.toNormal(camera.getRotation());
			orientation[0] = at.x;
			orientation[1] = at.y;
			orientation[2] = -at.z;
			
			Vector3f upRotation = camera.getRotation().clone();
			upRotation.x -= 90;
			Vector3f up = VectorUtils.toNormal(upRotation);
			orientation[3] = up.x;
			orientation[4] = up.y;
			orientation[5] = -up.z;
			
			alListenerfv(AL_ORIENTATION, orientation);
		}
		
		Object3d[] objects = world.getObjects();
		for (int i=0;i<objects.length;i++) {
			objects[i].updateSourceData(contextID);
		}
	}
	
	public int getDistanceModel() {
		return distanceModel;
	}
	
	public void setDistanceModel(int model) {
		distanceModel = model;
	}
	
	public float getListenerGain() {
		return gain;
	}
	
	public void setListenerGain(float gain) {
		this.gain = gain;
	}
	
	public Vector3f getListenerVelocity() {
		return velocity;
	}
	
	public void setListenerVelocity(float vx, float vy, float vz) {
		velocity.x = vx;
		velocity.y = vy;
		velocity.z = vz;
	}
	
	public void setListenerVelocity(Vector3f velocity) {
		this.velocity = velocity.clone();
	}
	
	public Camera3d getCamera() {
		return camera;
	}
	
	public void setCamera(Camera3d camera) {
		this.camera = camera;
	}
	
	public World3d getWorld() {
		return world;
	}
	
	public boolean terminated() {
		return terminated;
	}
	
	public void destroy() {
		manager.destroy(this);
	}
	
}
