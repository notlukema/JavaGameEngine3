

package jge3.common.sound;

import java.nio.ByteBuffer;

public class Sound {
	
	private int format;
	private int frequency;
	private ByteBuffer buffer;
	
	public Sound(int format, int frequency, ByteBuffer buffer) {
		this.format = format;
		this.frequency = frequency;
		this.buffer = buffer;
	}
	
	/*public void setBuffer(ShortBuffer buffer) {
		this.buffer = buffer;
	}*/
	
	public int getFormat() {
		return format;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
}
	
}
