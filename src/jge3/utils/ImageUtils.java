
package jge3.utils;

import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;

import jge3.common.image.Image;
import jge3.math.Vector3f;
import jge3.math.Vector4f;
import jge3.math.Vector4i;

public class ImageUtils {
	
	public static Image loadImage(String path) {
	    try (MemoryStack stack = MemoryStack.stackPush()) {
	        IntBuffer w = stack.mallocInt(1);
	        IntBuffer h = stack.mallocInt(1);
	        IntBuffer channels = stack.mallocInt(1);
	        ByteBuffer buffer = stbi_load(path, w, h, channels, 4);
	        if (buffer == null) {
	        	throw new RuntimeException("Couldn't load image \""+path+"\":\n"+stbi_failure_reason());
	        }
	        return new Image(w.get(0), h.get(0), buffer);
	    }
	}
	
	public static Image createImage(int width, int height) {
		return createImage(width, height, 0, 0, 0, 255);
	}
	
	public static Image createImage(int width, int height, Vector4f color) {
		return createImage(width, height, (int)(color.x*255), (int)(color.y*255), (int)(color.z*255), (int)(color.w*255));
	}
	
	public static Image createImage(int width, int height, int color) {
		return createImage(width, height, ColorUtils.getRed(color), ColorUtils.getGreen(color), ColorUtils.getBlue(color), ColorUtils.getAlpha(color));
	}
	
	public static Image createImage(int width, int height, int r, int g, int b) {
		return createImage(width, height, r, g, b, 255);
	}
	
	public static Image createImage(int width, int height, int r, int g, int b, int a) {
	    int size = width*height*4;
	    // maybe change to memory util one day idk i dont like freeing memory manually
	    ByteBuffer buffer = BufferUtils.createByteBuffer(size);
	    for (int i=0;i<size;i+=4) {
	        buffer.put(i, (byte)(r));
	        buffer.put(i+1, (byte)(g));
	        buffer.put(i+2, (byte)(b));
	        buffer.put(i+3, (byte)(a));
	    }
	    return new Image(width, height, buffer);
	}
	
	public static void drawToImage(Image drawTo, Image image, int x, int y, int width, int height) {
		for (int dx=0;dx<width;dx++) {
			for (int dy=0;dy<height;dy++) {
				int rx = x+dx;
				int ry = y+dy;
				if (rx < 0 || rx >= drawTo.getWidth() || ry < 0 || ry >= drawTo.getHeight()) {
					continue;
				}
				int px = (int)Math.round((float)dx/width*image.getWidth());
				int py = (int)Math.round((float)dy/height*image.getHeight());
				drawTo.drawPixel(rx, ry, image.getPixelInt(px, py));
			}
		}
	}
	
	public static Image cloneImage(Image image) {
		Image newImage = createImage(image.getWidth(), image.getHeight());
		for (int x=0;x<image.getWidth();x++) {
			for (int y=0;y<image.getHeight();y++) {
				newImage.setPixel(x, y, image.getPixelRGBAInt(x, y));
			}
		}
		return newImage;
	}
	
	public static GLFWImage toGLFWImage(Image image) {
		return GLFWImage.create().set(image.getWidth(), image.getHeight(), image.getBuffer());
	}
	
	public static Image toImage(GLFWImage image) {
		return new Image(image.width(), image.height(), image.pixels(image.width()*image.height()*4));
	}
	
	public static GLFWImage loadGLFWImage(String path) {
		return toGLFWImage(loadImage(path));
	}
	
	public static Vector4f normalToImage(Vector3f normal) {
		return normalToImage(normal.x, normal.y, normal.z);
	}
	
	public static Vector4f normalToImage(float nx, float ny, float nz) {
		return new Vector4f((nx+1)/2, (ny+1)/2, (nz+1)/2, 1f);
	}
	
	public static Vector3f imageToNormal(Vector4f color) {
		return imageToNormal(color.x, color.y, color.z);
	}
	
	public static Vector3f imageToNormal(float r, float g, float b) {
		return new Vector3f(r*2-1, g*2-1, b*2-1);
	}
	
	public static abstract class ImageIterator {
		public abstract Vector4i pixel(int r, int g, int b, int a, int x, int y);
	}
	
	public static void iterateImage(Image image, ImageIterator iterator) {
		for (int x=0;x<image.getWidth();x++) {
			for (int y=0;y<image.getHeight();y++) {
				Vector4i oldColor = image.getPixelRGBAInt(x, y);
				Vector4i newColor = iterator.pixel(oldColor.x, oldColor.y, oldColor.z, oldColor.w, x, y);
				image.setPixel(x, y, newColor.x, newColor.y, newColor.z, newColor.w);
			}
		}
	}
	
}
