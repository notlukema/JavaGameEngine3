
package jge3.common.object;

import jge3.math.Matrix2f;
import jge3.math.Matrix3f;
import jge3.math.Matrix4f;

public interface CustomObject {
	
	public int uniform1f(String name, float v1);
	public int uniform2f(String name, float v1, float v2);
	public int uniform3f(String name, float v1, float v2, float v3);
	public int uniform4f(String name, float v1, float v2, float v3, float v4);
	
	public int uniformf(String name, float[] v);
	
	public int uniform1i(String name, int v1);
	public int uniform2i(String name, int v1, int v2);
	public int uniform3i(String name, int v1, int v2, int v3);
	public int uniform4i(String name, int v1, int v2, int v3, int v4);
	
	public int uniformi(String name, int[] v);
	
	public int uniformMat2f(String name, Matrix2f mat);
	public int uniformMat3f(String name, Matrix3f mat);
	public int uniformMat4f(String name, Matrix4f mat);
	
}
