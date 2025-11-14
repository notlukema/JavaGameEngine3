
package jge3.engine.audio;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;

public class ALContext {
	
	private long deviceID;
	private long contextID;
	
	public ALContext() {
		deviceID = alcOpenDevice((ByteBuffer)null);
		if (deviceID == NULL) {
			throw new RuntimeException("Couldn't open OpenAL device");
		}
		
		contextID = alcCreateContext(deviceID, (IntBuffer)null);
        if (contextID == NULL) {
        	throw new RuntimeException("Couldn't create OpenAL context");
        }
        alcMakeContextCurrent(contextID);
        AL.createCapabilities(ALC.getCapabilities());
	}
	
	public long getDeviceID() {
		return deviceID;
	}
	
	public long getContextID() {
		return contextID;
	}
	
	public void free() {
		if (alcGetCurrentContext() == contextID) {
			alcMakeContextCurrent(NULL);
		}
		alcDestroyContext(contextID);
		alcCloseDevice(deviceID);
	}
	
}
