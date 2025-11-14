
package jge3.common.image;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import jge3.math.Vector3f;
import jge3.math.Vector3i;
import jge3.math.Vector4f;
import jge3.math.Vector4i;
import jge3.utils.ColorUtils;

public class Image {
	
	private final int width;
	private final int height;
	private ByteBuffer buffer;
	
	public Image(int width, int height, ByteBuffer buffer) {
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}
	
	public int getPixelInt(int x, int y) {
		int i = getIndex(x, y);
		return ColorUtils.toColor(byteToInt(buffer.get(i)), byteToInt(buffer.get(i+1)), byteToInt(buffer.get(i+2)), byteToInt(buffer.get(i+3)));
	}
	
	public Vector4i getPixelRGBAInt(int x, int y) {
		int i = getIndex(x, y);
		return new Vector4i(byteToInt(buffer.get(i)), byteToInt(buffer.get(i+1)), byteToInt(buffer.get(i+2)), byteToInt(buffer.get(i+3)));
	}
	
	public Vector4f getPixelRGBAFloat(int x, int y) {
		int i = getIndex(x, y);
		return new Vector4f(byteToInt(buffer.get(i))/255f, byteToInt(buffer.get(i+1))/255f, byteToInt(buffer.get(i+2))/255f, byteToInt(buffer.get(i+3))/255f);
	}
	
	private int byteToInt(byte b) {
		return (256+(int)b)%256;
	}
	
	private int getIndex(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			throw new IndexOutOfBoundsException("Index "+x+", "+y+" out of bounds for size "+width+", "+height);
		}
		return x*4 + y*width*4;
	}
	
	public void setPixel(int x, int y, int color) {
		setPixel(x, y, ColorUtils.getRed(color), ColorUtils.getGreen(color), ColorUtils.getBlue(color), ColorUtils.getAlpha(color));
	}
	
	public void setPixel(int x, int y, Vector3i color) {
		setPixel(x, y, color.x, color.y, color.z, 255);
	}
	
	public void setPixel(int x, int y, Vector4i color) {
		setPixel(x, y, color.x, color.y, color.z, color.w);
	}
	
	public void setPixel(int x, int y, Vector3f color) {
		setPixel(x, y, (int)(color.x*255), (int)(color.y*255), (int)(color.z*255), 255);
	}
	
	public void setPixel(int x, int y, Vector4f color) {
		setPixel(x, y, (int)(color.x*255), (int)(color.y*255), (int)(color.z*255), (int)(color.w*255));
	}
	
	public void setPixel(int x, int y, int r, int g, int b) {
		setPixel(x, y, r, g, b, 255);
	}
	
	public void setPixel(int x, int y, int r, int g, int b, int a) {
		int i = getIndex(x, y);
		buffer.put(i, (byte)r);
		buffer.put(i+1, (byte)g);
		buffer.put(i+2, (byte)b);
		buffer.put(i+3, (byte)a);
	}
	
	public void drawPixel(int x, int y, int color) {
		drawPixel(x, y, ColorUtils.getRed(color), ColorUtils.getGreen(color), ColorUtils.getBlue(color), ColorUtils.getAlpha(color));
	}
	
	public void drawPixel(int x, int y, Vector3i color) {
		drawPixel(x, y, color.x, color.y, color.z, 255);
	}
	
	public void drawPixel(int x, int y, Vector4i color) {
		drawPixel(x, y, color.x, color.y, color.z, color.w);
	}
	
	public void drawPixel(int x, int y, Vector3f color) {
		drawPixel(x, y, (int)(color.x*255), (int)(color.y*255), (int)(color.z*255), 255);
	}
	
	public void drawPixel(int x, int y, Vector4f color) {
		drawPixel(x, y, (int)(color.x*255), (int)(color.y*255), (int)(color.z*255), (int)(color.w*255));
	}
	
	public void drawPixel(int x, int y, int r, int g, int b) {
		drawPixel(x, y, r, g, b, 255);
	}
	
	public void drawPixel(int x, int y, int r, int g, int b, int a) {
		if (a == 0) {
			return;
		}
		int i = getIndex(x, y);
		if (a < 255) {
			int index = getIndex(x, y);
			int alpha = byteToInt(buffer.get(index+3));
			float ap = alpha/255f;
			float p = (a/255f)*ap+(1-ap);
			float ip = 1f-p;
			r = (int)(r*p + byteToInt(buffer.get(index))*ip);
			g = (int)(g*p + byteToInt(buffer.get(index+1))*ip);
			b = (int)(b*p + byteToInt(buffer.get(index+2))*ip);
			a = Math.max(a, alpha);
		}
		buffer.put(i, (byte)r);
		buffer.put(i+1, (byte)g);
		buffer.put(i+2, (byte)b);
		buffer.put(i+3, (byte)a);
	}
	
	public Image subImage(int x, int y, int w, int h) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(w*h*4);
		for (int dx=0;dx<w;dx++) {
			for (int dy=0;dy<h;dy++) {
				int i1 = getIndex(x+dx, y+dy);
				int i2 = dx*4 + dy*w*4;
				buffer.put(i2, this.buffer.get(i1));
				buffer.put(i2+1, this.buffer.get(i1+1));
				buffer.put(i2+2, this.buffer.get(i1+2));
				buffer.put(i2+3, this.buffer.get(i1+3));
			}
		}
		return new Image(w, h, buffer);
	}
	
	public void setImage(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
}
