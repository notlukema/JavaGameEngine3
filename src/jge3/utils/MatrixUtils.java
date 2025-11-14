
package jge3.utils;

import jge3.math.*;
import jge3.math.Matrix2f;
import jge3.math.Matrix3f;
import jge3.math.Matrix4f;
import jge3.math.Vector3f;

public class MatrixUtils {
	
	public static final float toRadians = (float)Math.PI/180;
	
	public static void printMatrix(Matrix4f matrix) {
		float[] mat = new float[16];
		matrix.get(mat);
		for (int i=0;i<mat.length;i++) {
			System.out.print(mat[i]+"  ");
			if ((i+1)%4 == 0) {
				System.out.println();
			}
		}
	}
	
	public static Vector3f rotate(float x, float y, float z, Matrix4f mat) {
		return new Vector3f(x, y, z).mul(mat, 1f);
	}
	
	public static Vector3f rotate(float x, float y, float z, float rotx, float roty, float rotz) {
		Matrix4f mat = new Matrix4f();
		mat.rotateX(rotx*toRadians);
		mat.rotateY(roty*toRadians);
		mat.rotateZ(rotz*toRadians);
		return new Vector3f(x, y, z).mul(mat, 1f);
	}
	
	public Matrix2f toMatrix2f(Matrix3f mat) {
		return new Matrix2f(mat);
	}
	
	public Matrix3f toMatrix3f(Matrix4f mat) {
		return new Matrix3f(mat);
	}
	
	public Matrix3f toMatrix3f(Matrix2f mat) {
		return new Matrix3f(mat);
	}
	
	public Matrix4f toMatrix4f(Matrix3f mat) {
		return new Matrix4f(mat);
	}
	
}
