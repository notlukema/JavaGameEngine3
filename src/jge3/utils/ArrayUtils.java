
package jge3.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

public class ArrayUtils {
	
	public static ByteBuffer toBuffer(byte[] data) {
		return BufferUtils.createByteBuffer(data.length).put(data).flip();
	}
	
	public static CharBuffer toBuffer(char[] data) {
		return BufferUtils.createCharBuffer(data.length).put(data).flip();
	}
	
	public static ShortBuffer toBuffer(short[] data) {
		return BufferUtils.createShortBuffer(data.length).put(data).flip();
	}
	
	public static IntBuffer toBuffer(int[] data) {
		return BufferUtils.createIntBuffer(data.length).put(data).flip();
	}
	
	public static LongBuffer toBuffer(long[] data) {
		return BufferUtils.createLongBuffer(data.length).put(data).flip();
	}
	
	public static FloatBuffer toBuffer(float[] data) {
		return BufferUtils.createFloatBuffer(data.length).put(data).flip();
	}
	
	public static DoubleBuffer toBuffer(double[] data) {
		return BufferUtils.createDoubleBuffer(data.length).put(data).flip();
	}
	
	public static byte[] byteToArray(List<Byte> list) {
		byte[] array = new byte[list.size()];
		for (int i=0;i<array.length;i++) {array[i] = list.get(i);}
		return array;
	}
	
	public static char[] charToArray(List<Character> list) {
		char[] array = new char[list.size()];
		for (int i=0;i<array.length;i++) {array[i] = list.get(i);}
		return array;
	}
	
	public static short[] shortToArray(List<Short> list) {
		short[] array = new short[list.size()];
		for (int i=0;i<array.length;i++) {array[i] = list.get(i);}
		return array;
	}
	
	public static int[] intToArray(List<Integer> list) {
		int[] array = new int[list.size()];
		for (int i=0;i<array.length;i++) {array[i] = list.get(i);}
		return array;
	}
	
	public static long[] longToArray(List<Long> list) {
		long[] array = new long[list.size()];
		for (int i=0;i<array.length;i++) {array[i] = list.get(i);}
		return array;
	}
	
	public static float[] floatToArray(List<Float> list) {
		float[] array = new float[list.size()];
		for (int i=0;i<array.length;i++) {array[i] = list.get(i);}
		return array;
	}
	
	public static double[] doubleToArray(List<Double> list) {
		double[] array = new double[list.size()];
		for (int i=0;i<array.length;i++) {array[i] = list.get(i);}
		return array;
	}
	
	public static boolean[] booleanToArray(List<Boolean> list) {
		boolean[] array = new boolean[list.size()];
		for (int i=0;i<array.length;i++) {array[i] = list.get(i);}
		return array;
	}
	
}
