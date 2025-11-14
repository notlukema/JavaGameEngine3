
package jge3.src3d.object.audio;

import static org.lwjgl.openal.AL10.*;

import java.util.ArrayList;
import java.util.List;

import jge3.common.sound.Sound;
import jge3.common.sound.Source;
import jge3.math.Vector2f;
import jge3.math.Vector3f;

public class Source3d {
	
	private int alSourceID;
	private Source sound;
	
	private boolean useQueue;
	private List<Source> queue;
	
	private Vector3f sourceVelocity;
	private float pitch;
	private float gain;
	private float rolloff;
	private boolean looping;
	
	private float minGain;
	private float maxGain;
	private float referenceDistance;
	private float maxDistance;
	
	private boolean hasCone;
	private Vector3f coneDirection;
	private float innerCone;
	private float outerCone;
	private float outerGain;
	
	public Source3d() {
		sound = null;
		useQueue = true;
		reset();
	}
	
	public Source3d(Source sound) {
		this.sound = sound;
		useQueue = false;
		reset();
	}
	
	public Source3d(Sound sound) {
		this.sound = new Source(sound);
		useQueue = false;
		reset();
	}
	
	private void reset() {
		alSourceID = -1;
		queue = new ArrayList<Source>();
		
		sourceVelocity = new Vector3f();
		pitch = 1f;
		gain = 1f;
		rolloff = 1f;
		looping = false;
		
		minGain = 0f;
		maxGain = 1f;
		referenceDistance = 1000f;
		maxDistance = 100000f;
		
		hasCone = false;
		coneDirection = new Vector3f(0, 0, 1);
		innerCone = 90f;
		outerCone = 110f;
		outerGain = 0.25f;
	}
	
	public void delete() {
		alDeleteSources(alSourceID);
		alSourceID = -1;
	}
	
	public Source getSound() {
		return sound;
	}
	
	public void setSound(Source sound) {
		this.sound = sound;
		useQueue = false;
	}
	
	public void setSound(Sound sound) {
		this.sound = new Source(sound);
		useQueue = false;
	}
	
	public boolean queueSound(Source sound) {
		useQueue = true;
		return queue.add(sound);
	}
	
	public boolean unqueueSound(Source sound) {
		useQueue = true;
		return queue.remove(sound);
	}
	
	public void create(long contextID, Vector3f position) {
		if (alSourceID >= 0) {
			alDeleteSources(alSourceID);
		}
		
		alSourceID = alGenSources();
		updateData(position, contextID);
		
		if (useQueue) {
			alSourcei(alSourceID, AL_BUFFER, 0);
			/*
			int[] buffers = new int[queue.size()];
			for (int i=0;i<buffers.length;i++) {
				queue.get(i).generateIfNot(contextID);
				buffers[i] = queue.get(i).getID(contextID);
				alSourceQueueBuffers(alSourceID, queue.get(i).getID(contextID));
			}
			alSourceQueueBuffers(alSourceID, buffers);
			*/
		} else if (sound != null) {
			alSourcei(alSourceID, AL_BUFFER, sound.getID(contextID));
		}
	}
	
	public void updateData(Vector3f position, long contextID) {
		sound.generateIfNot(contextID);
		
		alSourcef(alSourceID, AL_PITCH, pitch);
		alSourcef(alSourceID, AL_GAIN, gain);
		alSourcef(alSourceID, AL_ROLLOFF_FACTOR, rolloff);
		
		alSourcef(alSourceID, AL_MIN_GAIN, minGain);
		alSourcef(alSourceID, AL_MAX_GAIN, maxGain);
		alSourcef(alSourceID, AL_REFERENCE_DISTANCE, referenceDistance);
		alSourcef(alSourceID, AL_MAX_DISTANCE, maxDistance);
		
		if (hasCone) {
			alSourcef(alSourceID, AL_CONE_INNER_ANGLE, innerCone);
			alSourcef(alSourceID, AL_CONE_OUTER_ANGLE, outerCone);
			alSourcef(alSourceID, AL_CONE_OUTER_GAIN, outerGain);
			
			alSource3f(alSourceID, AL_DIRECTION, coneDirection.x, coneDirection.y, -coneDirection.z);
		} else {
			alSourcef(alSourceID, AL_CONE_INNER_ANGLE, 360f);
			alSourcef(alSourceID, AL_CONE_OUTER_ANGLE, 0f);
			alSourcef(alSourceID, AL_CONE_OUTER_GAIN, 1f);
			
			alSource3f(alSourceID, AL_DIRECTION, 0f, 0f, 0f);
		}
		
		alSource3f(alSourceID, AL_POSITION, position.x, position.y, -position.z);
		alSource3f(alSourceID, AL_VELOCITY, sourceVelocity.x, sourceVelocity.y, -sourceVelocity.z);
		
		alSourcei(alSourceID, AL_LOOPING, (looping?AL_TRUE:AL_FALSE));
	}
	
	public void playSource() {
		alSourcePlay(alSourceID);
	}
	
	public void pauseSource() {
		alSourcePause(alSourceID);
	}
	
	public void stopSource() {
		alSourceStop(alSourceID);
	}
	
	public void rewindSource() {
		alSourceRewind(alSourceID);
	}
	
	public int getSourceID() {
		return alSourceID;
	}
	
	public Vector3f getSourceVelocity() {
		return sourceVelocity;
	}
	
	public void setSourceVelocity(float vx, float vy, float vz) {
		sourceVelocity.x = vx;
		sourceVelocity.y = vy;
		sourceVelocity.z = vz;
	}
	
	public void setSourceVelocity(Vector3f velocity) {
		sourceVelocity = velocity.clone();
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getGain() {
		return gain;
	}
	
	public float getRolloff() {
		return rolloff;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setGain(float gain) {
		this.gain = gain;
	}
	
	public void setRolloff(float rolloff) {
		this.rolloff = rolloff;
	}
	
	public boolean isLooping() {
		return looping;
	}
	
	public void setLooping(boolean looping) {
		this.looping = looping;
	}
	
	public float getMinGain() {
		return minGain;
	}
	
	public float getMaxGain() {
		return maxGain;
	}
	
	public Vector2f getGainBounds() {
		return new Vector2f(minGain, maxGain);
	}
	
	public void setMinGain(float min) {
		minGain = min;
	}
	
	public void setMaxGain(float max) {
		maxGain = max;
	}
	
	public void setGainBounds(float min, float max) {
		minGain = min;
		maxGain = max;
	}
	
	public float getReferenceDistance() {
		return referenceDistance;
	}
	
	public void setReferenceDistance(float distance) {
		referenceDistance = distance;
	}
	
	public float getMaxDistance() {
		return maxDistance;
	}
	
	public void setMaxDistance(float distance) {
		maxDistance = distance;
	}
	
	public boolean usingCone() {
		return hasCone;
	}
	
	public void setUsingCone(boolean using) {
		hasCone = using;
	}
	
	public Vector3f getConeDirection() {
		return coneDirection;
	}
	
	public void setConeDirection(Vector3f direction) {
		coneDirection = direction.clone().safeNormalize();
	}
	
	public void setConeDirection(float dx, float dy, float dz) {
		coneDirection = new Vector3f(dx, dy, dz).safeNormalize();
	}
	
	public float getInnerConeAngle() {
		return innerCone;
	}
	
	public void setInnerConeAngle(float angle) {
		innerCone = angle;
	}
	
	public float getOuterConeAngle() {
		return outerCone;
	}
	
	public void setOuterConeAngle(float angle) {
		outerCone = angle;
	}
	
	public float getOuterConeGain() {
		return outerGain;
	}
	
	public void setOuterConeGain(float gain) {
		outerGain = gain;
	}
	
}
