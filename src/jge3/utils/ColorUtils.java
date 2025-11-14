
package jge3.utils;

import jge3.math.Vector4f;

public class ColorUtils {
	
	public static Vector4f black() {
		return new Vector4f(0f, 0f, 0f, 1f);
	}
	
	public static Vector4f white() {
		return new Vector4f(1f, 1f, 1f, 1f);
	}
	
	public static final float COLOR_MAX = 255f;
	
	public static final byte COLOR_BITS = 8;
	public static final int COLOR_ONE = (1 << COLOR_BITS) - 1;
	private static final byte BLUE_SHIFT = COLOR_BITS * 0;
	private static final byte GREEN_SHIFT = COLOR_BITS * 1;
	private static final byte RED_SHIFT = COLOR_BITS * 2;
	private static final byte ALPHA_SHIFT = COLOR_BITS * 3;
	
	
	public static Vector4f interpolateColors(Vector4f color1, Vector4f color2, double p) {
		float p1 = (float)p;
		float p2 = (float)(1-p);
		float r = color1.x*p1 + color2.x*p2;
		float g = color1.y*p1 + color2.y*p2;
		float b = color1.z*p1 + color2.z*p2;
		float a = color1.w*p1 + color2.w*p2;
		return new Vector4f(r, g, b, a);
	}
	
	public static Vector4f toVector(int r, int g, int b, int a) {
		return new Vector4f(r/COLOR_MAX, g/COLOR_MAX, b/COLOR_MAX, a/COLOR_MAX);
	}
	
	public static Vector4f toVector(int color) {
		return toVector(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
	}
	
	public static int toColor(Vector4f color) {
		return toColor((int)(color.x*COLOR_MAX), (int)(color.y*COLOR_MAX), (int)(color.z*COLOR_MAX), (int)(color.w*COLOR_MAX));
	}
	
	public static int toColor(int r, int g, int b) {
		return toColor(r, g, b, 255);
	}
	
	public static int toColor(int r, int g, int b, int a) {
		if (a < 0) {a = 0;}
		if (r < 0) {r = 0;}
		if (g < 0) {g = 0;}
		if (b < 0) {b = 0;}
		if (a > COLOR_ONE) {a = COLOR_ONE;}
		if (r > COLOR_ONE) {r = COLOR_ONE;}
		if (g > COLOR_ONE) {g = COLOR_ONE;}
		if (b > COLOR_ONE) {b = COLOR_ONE;}
		return (a << ALPHA_SHIFT | r << RED_SHIFT | g << GREEN_SHIFT | b << BLUE_SHIFT);
	}
	
	public static int getAlpha(int color) {
		return (color >> ALPHA_SHIFT) & COLOR_ONE;
	}
	
	public static int getRed(int color) {
		return (color >> RED_SHIFT) & COLOR_ONE;
	}
	
	public static int getGreen(int color) {
		return (color >> GREEN_SHIFT) & COLOR_ONE;
	}
	
	public static int getBlue(int color) {
		return (color >> BLUE_SHIFT) & COLOR_ONE;
	}
	
	public static int interpolateColors(int color1, int color2, double p) {
		double p1 = p;
		double p2 = 1-p;
		int a = (int)(getAlpha(color1)*p1 + getAlpha(color2)*p2);
		int r = (int)(getRed(color1)*p1 + getRed(color2)*p2);
		int g = (int)(getGreen(color1)*p1 + getGreen(color2)*p2);
		int b = (int)(getBlue(color1)*p1 + getBlue(color2)*p2);
		return toColor(r, g, b, a);
	}
	
}
