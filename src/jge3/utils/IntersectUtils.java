
package jge3.utils;

import org.joml.Intersectionf;

import jge3.math.Vector3f;

public class IntersectUtils {
	
	public static float intersectRayTriangle(float x, float y, float z, float dirx, float diry, float dirz, Vector3f v1, Vector3f v2, Vector3f v3) {
		return Intersectionf.intersectRayTriangle(x, y, z, dirx, diry, dirz, v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, v3.x, v3.y, v3.z, 0);
	}
	
}
