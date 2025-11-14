
package jge3.utils;

import static org.lwjgl.stb.STBTruetype.*;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;

import jge3.common.image.Image;
import jge3.common.image.text.Font;
import jge3.common.image.text.Symbol;
import jge3.math.Vector4i;

public class FontUtils {
	
	public static final int DEFAULT_SIZE = 75;
	
	public static Font loadTrueType(String path) {
		return loadTrueType(path, DEFAULT_SIZE);
	}
	
	public static Font loadTrueType(String path, int pixelScale) {
		byte[] fileBytes = FileUtils.loadFileBytes(path);
		ByteBuffer bytes = BufferUtils.createByteBuffer(fileBytes.length);
		bytes.put(fileBytes);
		bytes.flip();
		
		String name = path.substring(path.lastIndexOf("/")+1, path.length());
		name = name.substring(0, name.lastIndexOf("."));
		return loadTrueType(name, bytes, pixelScale);
	}
	
	public static Font loadTrueType(InputStream stream) {
		return loadTrueType(stream, DEFAULT_SIZE);
	}
	
	public static Font loadTrueType(InputStream stream, int pixelScale) {
		byte[] fileBytes = FileUtils.loadResourceBytes(stream);
		ByteBuffer bytes = BufferUtils.createByteBuffer(fileBytes.length);
		bytes.put(fileBytes);
		bytes.flip();
		
		String name = "font";
		return loadTrueType(name, bytes, pixelScale);
	}
	
	public static Font loadTrueType(String name, ByteBuffer bytes, int pixelScale) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			STBTTFontinfo info = STBTTFontinfo.malloc();
			
			stbtt_InitFont(info, bytes);
			/*
	        name = Charset.defaultCharset().decode(stbtt_GetFontNameString(info, STBTT_PLATFORM_ID_UNICODE, STBTT_UNICODE_EID_UNICODE_1_0,
	        		STBTT_MS_LANG_ENGLISH, 0)).toString();
			*/
			
			float scale = stbtt_ScaleForPixelHeight(info, pixelScale);
			
			int bw = 100 + 8*pixelScale;
			int bh = 100 + 8*pixelScale;
	        STBTTBakedChar.Buffer charData = STBTTBakedChar.malloc(96);
	        
	        ByteBuffer bitmap = BufferUtils.createByteBuffer(bw*bh);
	        stbtt_BakeFontBitmap(bytes, pixelScale, bitmap, bw, bh, 32, charData);
	        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?`~";
	        Symbol[] characters = new Symbol[chars.length()+1];
            STBTTAlignedQuad q = STBTTAlignedQuad.malloc(stack);
            
	        for (int i=1;i<characters.length;i++) {
	        	char character = chars.charAt(i-1);
	        	
	        	int codepoint = character;
	        	
	        	FloatBuffer x = stack.mallocFloat(1);
	        	FloatBuffer y = stack.mallocFloat(1);
	        	
                stbtt_GetBakedQuad(charData, bw, bh, codepoint-32, x, y, q, true);
                
                int x1 = (int)(q.s0()*bw);
                int y1 = (int)(q.t0()*bh);
                int x2 = (int)(q.s1()*bw);
                int y2 = (int)(q.t1()*bh);
                
		        Image image = ImageUtils.createImage(x2-x1, y2-y1, 255, 255, 255);
		        ImageUtils.iterateImage(image, new ImageUtils.ImageIterator() {
					@Override
					public Vector4i pixel(int r, int g, int b, int a, int x, int y) {
						int c = (bitmap.get((x+x1)+bw*(y+y1))+256)%256;
						return new Vector4i(r, g, b, c);
					}
		        });
		        
		        characters[i] = new Symbol(character, image, q.x0(), q.y1(), x.get(0));
	        }
	        
	        IntBuffer advance = stack.mallocInt(1);
	        stbtt_GetCodepointHMetrics(info, ' ', advance, stack.mallocInt(1));
	        characters[0] = new Symbol(' ', ImageUtils.createImage(0, 0), 0, 0, advance.get(0)*scale);
	        /*
			for (int i=1;i<characters.length;i++) {
	        	char character = chars.charAt(i-1);
	        	
	        	int codepoint = character;
	        	
	        	FloatBuffer x = stack.mallocFloat(1);
	        	FloatBuffer y = stack.mallocFloat(1);
	        	
                stbtt_GetBakedQuad(charData, bw, bh, codepoint-32, x, y, q, true);
				
		        IntBuffer width = stack.mallocInt(1);
		        IntBuffer height = stack.mallocInt(1);
		        IntBuffer xoff = stack.mallocInt(1);
		        IntBuffer yoff = stack.mallocInt(1);
		        
		        ByteBuffer glyph = stbtt_GetCodepointBitmap(info, scale, scale, codepoint, width, height, xoff, yoff);
                
		        Image image = ImageUtils.createImage(width.get(0), height.get(0), 255, 255, 255);
		        ImageUtils.iterateImage(image, new ImageUtils.ImageIterator() {
					@Override
					public Vector4i pixel(int r, int g, int b, int a, int x, int y) {
						int c = (glyph.get(x+width.get(0)*y)+256)%256;
						return new Vector4i(r, g, b, c);
					}
		        });
		        
		        characters[i] = new Symbol(character, image, q.x0(), q.y1(), x.get(0));
		        
			}
			*/
	        IntBuffer ascent = stack.mallocInt(1);
	        IntBuffer descent = stack.mallocInt(1);
	        IntBuffer lineGap = stack.mallocInt(1);
	        stbtt_GetFontVMetrics(info, ascent, descent, lineGap);
			
			return new Font(name, characters, pixelScale, ascent.get(0)*scale, descent.get(0)*scale, lineGap.get(0)*scale);
		}
	}
	
}
