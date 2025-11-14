package jge3.common.sound;

import static org.lwjgl.openal.AL10.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Source {
	
	private Map<Long, Integer> soundIDs;
	
	private Sound sound;
	
	public Source(Sound sound) {
		soundIDs = new HashMap<Long, Integer>();
		this.sound = sound;
	}
	
	public Sound getSound() {
		return sound;
	}
	
	public void setSound(Sound sound) {
		this.sound = sound;
	}
	
	public int getID(long contextID) {
		return soundIDs.getOrDefault(contextID, 0);
	}
	
	public void generateSound(long contextID) {
		if (soundIDs.containsKey(contextID)) {
			alDeleteBuffers(soundIDs.remove(contextID));
		}
		generate(contextID);
	}
	
	public void generateIfNot(long contextID) {
		if (!soundIDs.containsKey(contextID)) {
			generate(contextID);
		}
	}
	
	private void generate(long contextID) {
		int soundID = alGenBuffers();
		alBufferData(soundID, sound.getFormat(), sound.getBuffer(), sound.getFrequency());
		soundIDs.put(contextID, soundID);
	}
	
	public void destroy() {
    	for (Entry<Long, Integer> entry : soundIDs.entrySet()) {
    		alDeleteBuffers(entry.getValue());
    	}
    	soundIDs.clear();
	}
	
}
