
package jge3.src3d.object;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static jge3.utils.ArrayUtils.*;
import static jge3.src3d.object.Vertex3d.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import jge3.src3d.object.animation.Bone;
import jge3.src3d.object.material.Material;
import jge3.src3d.object.shapes.Shape;

public class Mesh {
	
	public static final int STATIC = GL_STATIC_DRAW;
	public static final int DYNAMIC = GL_DYNAMIC_DRAW;
	public static final int STREAM = GL_STREAM_DRAW;
	
	//
	
	private int vao;
	private List<Integer> vbos;
	private int vertexCount;
	private int drawType;
	
	private Material material;
	
	private List<Shape> objects;
	
	private List<Bone> bones;
	
	private boolean hidden;
	
	public Mesh(Material material) {
		this.material = material;
		vao = -1;
		vbos = new ArrayList<Integer>();
		objects = new ArrayList<Shape>();
		bones = new ArrayList<Bone>();
		vertexCount = -1;
		drawType = STATIC;
		
		hidden = false;
	}
	
	public Mesh(Material material, Shape object) {
		this(material);
		addObject(object);
	}
	
	public boolean addObject(Shape object) {
		return objects.add(object);
	}
	
	public boolean removeObject(Shape object) {
		return objects.remove(object);
	}
	
	public boolean addBone(Bone bone) {
		return bones.add(bone);
	}
	
	public boolean removeBone(Bone bone) {
		return bones.remove(bone);
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public int create(long contextID) {
		if (vao >= 0) {
			glDeleteVertexArrays(vao);
		}
		
		material.getDiffuseMap().generateIfNot(contextID);
		material.getAmbientMap().generateIfNot(contextID);
		material.getSpecularMap().generateIfNot(contextID);
		material.getNormalMap().generateIfNot(contextID);
		
		vbos = new ArrayList<Integer>();
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		for (int i=0;i<bones.size();i++) {
			bones.get(i).setID(i);
		}
		
		ArrayList<Float> positions = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Float> uvs = new ArrayList<Float>();
		
		ArrayList<Integer> boneIDs = new ArrayList<Integer>();
		ArrayList<Float> weights = new ArrayList<Float>();
		
		ArrayList<Integer> indices = new ArrayList<Integer>();

		for (int i=0;i<objects.size();i++) {
			objects.get(i).resetVertices();
		}
		for (int i=0;i<objects.size();i++) {
			objects.get(i).addData(positions, normals, uvs, boneIDs, weights);
		}
		for (int i=0;i<objects.size();i++) {
			objects.get(i).addIndices(indices);
		}
		
		storeData(POSITION, 3, floatToArray(positions));
		storeData(NORMAL, 3, floatToArray(normals));
		storeData(UV, 2, floatToArray(uvs));
		storeData(BONES, MAX_BONES, intToArray(boneIDs));
		storeData(WEIGHTS, MAX_BONES, floatToArray(weights));
		bindIndices(intToArray(indices));
		
		glBindVertexArray(0);
		
		return vao;
	}
	
	private void storeData(int attribute, int dimensions, float[] data) {
		int vbo = glGenBuffers();
		vbos.add(vbo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = toBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, drawType);
		glVertexAttribPointer(attribute, dimensions, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private void storeData(int attribute, int dimensions, int[] data) {
		int vbo = glGenBuffers();
		vbos.add(vbo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		IntBuffer buffer = toBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, drawType);
		glVertexAttribIPointer(attribute, dimensions, GL_INT, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private void bindIndices(int[] data) {
		int vbo = glGenBuffers();
		vbos.add(vbo);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
		IntBuffer buffer = toBuffer(data);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, drawType);
		vertexCount = data.length;
	}
	
	public void updateVertexData() {
		int length = objects.size();
		for (int i=0;i<length;i++) {
			objects.get(i).updateVertexData();
		}
	}
	
	public Shape[] getObjects() {
		Shape[] objects = new Shape[this.objects.size()];
		for (int i=0;i<objects.length;i++) {
			objects[i] = this.objects.get(i);
		}
		return objects;
	}
	
	public int getObjectCount() {
		return objects.size();
	}
	
	public Shape getObject(int i) {
		return objects.get(i);
	}
	
	public Bone[] getBones() {
		Bone[] bones = new Bone[this.bones.size()];
		for (int i=0;i<bones.length;i++) {
			bones[i] = this.bones.get(i);
		}
		return bones;
	}
	
	public int getBoneCount() {
		return bones.size();
	}
	
	public Bone getBone(int i) {
		return bones.get(i);
	}
	
	public int getVAO() {
		return vao;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public int getDrawType() {
		return drawType;
	}
	
	public void setDrawType(int drawType) {
		this.drawType = drawType;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
}
