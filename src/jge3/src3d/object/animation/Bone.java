
package jge3.src3d.object.animation;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;

import jge3.math.Matrix4f;

public class Bone {
	
	public static final int MAX_BONES = 200;
	
	public static final String STRUCTURE = "boneMatrices";
	
	//
	
	private int id;
	private String name;
	
	private Matrix4f offsetMatrix;
	private Matrix4f finalMatrix;
	
	private int matrixUniformID;
	
	public Bone(String name) {
		this.name = name;
		
		offsetMatrix = new Matrix4f();
		finalMatrix = new Matrix4f();
	}
	
	public void setFinalMatrix(Matrix4f matrix) {
		finalMatrix = matrix.mul(offsetMatrix);
	}
	
	public void bindBoneMatrix(int programID) {
		if (id > MAX_BONES) {
			return;
		}
		matrixUniformID = glGetUniformLocation(programID, STRUCTURE+"["+id+"]");
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        finalMatrix.get(fb);
	        glUniformMatrix4fv(matrixUniformID, false, fb);
		}
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Matrix4f getOffsetMatrix() {
		return offsetMatrix;
	}
	
	public void setOffsetMatrix(Matrix4f matrix) {
		matrix.get(offsetMatrix);
	}
	
}
