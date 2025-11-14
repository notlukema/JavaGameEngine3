
package jge3.src2d.object.custom;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;

import jge3.common.object.CustomObject;
import jge3.engine.graphics.Program;
import jge3.engine.graphics.Renderer2d;
import jge3.math.Matrix2f;
import jge3.math.Matrix3f;
import jge3.math.Matrix4f;
import jge3.src2d.World2d;
import jge3.src2d.object.Object2d;
import jge3.src2d.object.camera.Camera2d;

public abstract class CustomObject2d extends Object2d implements CustomObject {
	
	private int programID;
	
	@Override
	public Object2d create() {
		super.create();
		init();
		return this;
	}
	
	public abstract void init();
	
	public void preBind(Renderer2d renderer, Program program, World2d world, Camera2d camera) {
		programID = program.getID();
		drawData(renderer, program, world, camera);
	}
	
	public abstract void drawData(Renderer2d renderer, Program program, World2d world, Camera2d camera);
	
	public abstract void preDraw(Renderer2d renderer, Program program, World2d world, Camera2d camera);
	
	@Override
	public int uniform1f(String name, float v1) {
		int id = glGetUniformLocation(programID, name);
		glUniform1f(id, v1);
		return id;
	}
	
	@Override
	public int uniform2f(String name, float v1, float v2) {
		int id = glGetUniformLocation(programID, name);
		glUniform2f(id, v1, v2);
		return id;
	}
	
	@Override
	public int uniform3f(String name, float v1, float v2, float v3) {
		int id = glGetUniformLocation(programID, name);
		glUniform3f(id, v1, v2, v3);
		return id;
	}
	
	@Override
	public int uniform4f(String name, float v1, float v2, float v3, float v4) {
		int id = glGetUniformLocation(programID, name);
		glUniform4f(id, v1, v2, v3, v4);
		return id;
	}
	
	@Override
	public int uniformf(String name, float[] v) {
		switch(v.length) {
		case 1:
			return uniform1f(name, v[0]);
		case 2:
			return uniform2f(name, v[0], v[1]);
		case 3:
			return uniform3f(name, v[0], v[1], v[2]);
		case 4:
			return uniform4f(name, v[0], v[1], v[2], v[3]);
		default:
			return -1;
		}
	}
	
	@Override
	public int uniform1i(String name, int v1) {
		int id = glGetUniformLocation(programID, name);
		glUniform1i(id, v1);
		return id;
	}
	
	@Override
	public int uniform2i(String name, int v1, int v2) {
		int id = glGetUniformLocation(programID, name);
		glUniform2i(id, v1, v2);
		return id;
	}
	
	@Override
	public int uniform3i(String name, int v1, int v2, int v3) {
		int id = glGetUniformLocation(programID, name);
		glUniform3i(id, v1, v2, v3);
		return id;
	}
	
	@Override
	public int uniform4i(String name, int v1, int v2, int v3, int v4) {
		int id = glGetUniformLocation(programID, name);
		glUniform4i(id, v1, v2, v3, v4);
		return id;
	}
	
	@Override
	public int uniformi(String name, int[] v) {
		switch(v.length) {
		case 1:
			return uniform1i(name, v[0]);
		case 2:
			return uniform2i(name, v[0], v[1]);
		case 3:
			return uniform3i(name, v[0], v[1], v[2]);
		case 4:
			return uniform4i(name, v[0], v[1], v[2], v[3]);
		default:
			return -1;
		}
	}
	
	@Override
	public int uniformMat2f(String name, Matrix2f mat) {
		int id = glGetUniformLocation(programID, name);
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(4);
	        mat.get(fb);
	        glUniformMatrix2fv(id, false, fb);
		}
		return id;
	}
	
	@Override
	public int uniformMat3f(String name, Matrix3f mat) {
		int id = glGetUniformLocation(programID, name);
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(9);
	        mat.get(fb);
	        glUniformMatrix3fv(id, false, fb);
		}
		return id;
	}
	
	@Override
	public int uniformMat4f(String name, Matrix4f mat) {
		int id = glGetUniformLocation(programID, name);
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        mat.get(fb);
	        glUniformMatrix4fv(id, false, fb);
		}
		return id;
	}
	
}
