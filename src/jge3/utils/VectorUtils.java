
package jge3.utils;

import static jge3.utils.MatrixUtils.toRadians;

import jge3.math.*;
import jge3.math.*;

public class VectorUtils {
	
	public static final float minFloat = 0.001f;
	public static final double minDouble = 0.001;
	
	public static int[] toArray(Vector2i vec) {
		return new int[] {vec.x, vec.y};
	}
	
	public static int[] toArray(Vector3i vec) {
		return new int[] {vec.x, vec.y, vec.z};
	}
	
	public static int[] toArray(Vector4i vec) {
		return new int[] {vec.x, vec.y, vec.z, vec.w};
	}
	
	public static float[] toArray(Vector2f vec) {
		return new float[] {vec.x, vec.y};
	}
	
	public static float[] toArray(Vector3f vec) {
		return new float[] {vec.x, vec.y, vec.z};
	}
	
	public static float[] toArray(Vector4f vec) {
		return new float[] {vec.x, vec.y, vec.z, vec.w};
	}
	
	public static int dimensionsOf(Vector2i vec) {
		return 2;
	}
	
	public static int dimensionsOf(Vector3i vec) {
		return 3;
	}
	
	public static int dimensionsOf(Vector4i vec) {
		return 4;
	}
	
	public static int dimensionsOf(Vector2f vec) {
		return 2;
	}
	
	public static int dimensionsOf(Vector3f vec) {
		return 3;
	}
	
	public static int dimensionsOf(Vector4f vec) {
		return 4;
	}
	
	public static Vector4i toVector4i(Vector3i vec) {
		return new Vector4i(vec.x, vec.y, vec.z, 0);
	}
	
	public static Vector4i toVector4i(Vector2i vec) {
		return new Vector4i(vec.x, vec.y, 0, 0);
	}
	
	public static Vector3i toVector3i(Vector4i vec) {
		return new Vector3i(vec.x, vec.y, vec.z);
	}
	
	public static Vector3i toVector3f(Vector2i vec) {
		return new Vector3i(vec.x, vec.y, 0);
	}
	
	public static Vector2i toVector2i(Vector4i vec) {
		return new Vector2i(vec.x, vec.y);
	}
	
	public static Vector2i toVector2i(Vector3i vec) {
		return new Vector2i(vec.x, vec.y);
	}
	
	public static Vector4f toVector4f(Vector3f vec) {
		return new Vector4f(vec.x, vec.y, vec.z, 0f);
	}
	
	public static Vector4f toVector4f(Vector2f vec) {
		return new Vector4f(vec.x, vec.y, 0f, 0f);
	}
	
	public static Vector3f toVector3f(Vector4f vec) {
		return new Vector3f(vec.x, vec.y, vec.z);
	}
	
	public static Vector3f toVector3f(Vector2f vec) {
		return new Vector3f(vec.x, vec.y, 0f);
	}
	
	public static Vector2f toVector2f(Vector4f vec) {
		return new Vector2f(vec.x, vec.y);
	}
	
	public static Vector2f toVector2f(Vector3f vec) {
		return new Vector2f(vec.x, vec.y);
	}
	
	public static Vector2f toFloat(Vector2i vec) {
		return new Vector2f(vec.x, vec.y);
	}
	
	public static Vector3f toFloat(Vector3i vec) {
		return new Vector3f(vec.x, vec.y, vec.z);
	}
	
	public static Vector4f toFloat(Vector4i vec) {
		return new Vector4f(vec.x, vec.y, vec.z, vec.w);
	}
	
	public static Vector2i toInt(Vector2f vec) {
		return new Vector2i((int)vec.x, (int)vec.y);
	}
	
	public static Vector3i toInt(Vector3f vec) {
		return new Vector3i((int)vec.x, (int)vec.y, (int)vec.z);
	}
	
	public static Vector4i toInt(Vector4f vec) {
		return new Vector4i((int)vec.x, (int)vec.y, (int)vec.z, (int)vec.w);
	}
	
	public static Vector2f toNormal(float rotation) {
		return new Vector2f((float)Math.sin(Math.toRadians(rotation)), (float)Math.cos(Math.toRadians(rotation)));
	}
	
	public static Vector3f toNormal(Vector3f rotation) {
		Vector3f vec = new Vector3f(0, 0, 1);
		vec.rotateZ(-rotation.z*toRadians);
		vec.rotateX(rotation.x*toRadians);
		vec.rotateY(rotation.y*toRadians);
		return new Vector3f(vec.x, vec.y, vec.z);
	}
	
}
