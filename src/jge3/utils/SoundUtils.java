
package jge3.utils;

import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;

import jge3.common.sound.Sound;

public class SoundUtils {
	
	public static ByteBuffer stereoToMono(ByteBuffer buffer) {
		int size = buffer.limit();
    	ByteBuffer monoBuffer = BufferUtils.createByteBuffer(size/2);
    	for (int i=0;i<size/4;i++) {
    		int left = buffer.get(i*4)+(buffer.get(i*4+1)<<8);
    		int right = buffer.get(i*4+2)+(buffer.get(i*4+3)<<8);
    		int mono = (left+right)/2;
    		monoBuffer.put(i*2, (byte)mono);
    		monoBuffer.put(i*2+1, (byte)(mono>>8));
    	}
    	return monoBuffer;
	}
	
	public static Sound load(String filePath) {
		return load(filePath, true);
	}
	
	public static Sound load(String filePath, boolean toMono) {
		AudioInputStream audioInputStream;
		AudioFormat format;
		
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
			format = audioInputStream.getFormat();
		} catch (UnsupportedAudioFileException e) {
			throw new RuntimeException("Couldn't load sound \""+filePath+"\":\n"+e.getLocalizedMessage());
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load sound \""+filePath+"\":\n"+e.getLocalizedMessage());
		}
		
		int channels = format.getChannels();
		float rate = format.getSampleRate();
		
	    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	    byte[] buffer = new byte[4096];
	    int read = 0;
	    while (read != -1) {
	    	try {
				read = audioInputStream.read(buffer, 0, buffer.length);
		    	byteOut.write(buffer);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
	    }
	    
	    ByteBuffer byteBuffer = ArrayUtils.toBuffer(byteOut.toByteArray());
	    
	    if (channels != 1 && channels != 2) {
	    	throw new RuntimeException("Unsupported channel count");
	    }
	    
	    if (toMono && channels == 2) {
	    	byteBuffer = stereoToMono(byteBuffer);
	    	channels = 1;
	    }
	    
	    try {
			audioInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return new Sound((channels==1?AL_FORMAT_MONO16:AL_FORMAT_STEREO16), (int)rate, byteBuffer);
	}
	
	public static Sound loadVorbis(String path) {
		return loadVorbis(path, true);
	}
	
	public static Sound loadVorbis(String path, boolean toMono) {
	    try (MemoryStack stack = MemoryStack.stackPush()) {
	        IntBuffer error = stack.mallocInt(1);
	        long decoder = stb_vorbis_open_filename(path, error, null);
	        if (decoder == NULL) {
	        	throw new RuntimeException("Couldn't load vorbis \""+path+"\":\n"+error.get(0));
	        }
	        
	        STBVorbisInfo info = STBVorbisInfo.malloc();
	        stb_vorbis_get_info(decoder, info);
	        int channels = info.channels();
	        channels = 1;
	        int rate = info.sample_rate();
	        int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);
	        
	        ByteBuffer pcm = BufferUtils.createByteBuffer(lengthSamples*Short.BYTES);
	        int length = stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm.asShortBuffer());
	        pcm.limit(length * channels);
	        
	        stb_vorbis_close(decoder);
	        info.free();
	        
	        if (toMono && channels == 2) {
	        	pcm = stereoToMono(pcm);
	        	channels = 1;
	        }
	        
			return new Sound((channels==1?AL_FORMAT_MONO16:AL_FORMAT_STEREO16), rate, pcm);
	    }
	}
	
}
