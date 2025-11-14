
package jge3.engine.managers;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import jge3.engine.audio.Audio3d;
import jge3.src3d.World3d;
import jge3.src3d.object.camera.Camera3d;

public class AudioManager {
	
	private static AudioManager manager = new AudioManager();
	
	public static AudioManager get() {
		return manager;
	}
	
	//
	
	private List<Audio3d> audio3ds;
	
	private ALCCapabilities alDeviceCaps;
	private ALCapabilities alCaps;
	
	private AudioManager() {
		audio3ds = new ArrayList<Audio3d>();
	}
	
	public void initialize() {
		long alDeviceID = alcOpenDevice((ByteBuffer)null);
		if (alDeviceID == NULL) {
			throw new RuntimeException("Couldn't open OpenAL device");
		}
		alDeviceCaps = ALC.createCapabilities(alDeviceID);
		
		long context = alcCreateContext(alDeviceID, (IntBuffer)null);
		alcMakeContextCurrent(context);
		AL.createCapabilities(alDeviceCaps);
		alCaps = AL.getCapabilities();
		alcDestroyContext(context);
		alcCloseDevice(alDeviceID);
	}
	
	public ALCCapabilities getDeviceCapabilities() {
		return alDeviceCaps;
	}
	
	public ALCapabilities getCapabilities() {
		return alCaps;
	}
	
	public void destroy(Audio3d audioPlayer) {
		audioPlayer.freeUsing();
		audio3ds.remove(audioPlayer);
	}
	
	public Audio3d createAudioPlayer(World3d world) {
		Audio3d audio = new Audio3d(this, world, null);
		audio3ds.add(audio);
		return audio;
	}
	
	public Audio3d createAudioPlayer(World3d world, Camera3d camera) {
		Audio3d audio = new Audio3d(this, world, camera);
		audio3ds.add(audio);
		return audio;
	}
	
	public void addAudioPlayer(Audio3d audio) {
		audio3ds.add(audio);
	}
	
	public Audio3d[] getAudio3ds() {
		Audio3d[] audioPlayers = new Audio3d[audio3ds.size()];
		for (int i=0;i<audioPlayers.length;i++) {
			audioPlayers[i] = audio3ds.get(i);
		}
		return audioPlayers;
	}
	
	public int getAudio3dCount() {
		return audio3ds.size();
	}
	
	public Audio3d getAudio3d(int i) {
		return audio3ds.get(i);
	}
	
	public void throwErrorIf(String str) {
		int error = alGetError();
		if (error != AL_NO_ERROR) {
			switch(error) {
				case AL_INVALID_NAME:
					throw new RuntimeException(str+": Invalid AL Name!");
				case AL_INVALID_ENUM:
					throw new RuntimeException(str+": Invalid AL Enum!");
				case AL_INVALID_VALUE:
					throw new RuntimeException(str+": Invalid AL Value!");
				case AL_INVALID_OPERATION:
					throw new RuntimeException(str+": Invalid AL Operation!");
				case AL_OUT_OF_MEMORY:
					throw new RuntimeException(str+": AL out of memory!");
				default:
					throw new RuntimeException(str+": Unknown AL error!");
			}
		}
	}
	
	public void terminate() {
		while(audio3ds.size()>0) {
			destroy(audio3ds.get(0));
		}
	}
	
}
