package jge3.src2d.object;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static jge3.utils.ArrayUtils.*;
import static jge3.src2d.object.Vertex2d.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import jge3.common.image.Texture;
import jge3.src2d.object.shapes.Rect;
import jge3.src2d.object.shapes.Shape;

public class Sprite {
	
	public static final int STATIC = GL_STATIC_DRAW;
	public static final int DYNAMIC = GL_DYNAMIC_DRAW;
	public static final int STREAM = GL_STREAM_DRAW;
	
	public static final int MAX_LAYERS = 100;
	public static final String TEXTURE = "spriteTexture";
	
	//
	
	private int vao;
	private List<Integer> vbos;
	private int vertexCount;
	private int drawType;
	
	private int layer;
	private float globalLayer;
	private Texture texture;
	
	private List<Shape> objects;
	
	private boolean hidden;
	
	public Sprite(Texture texture) {
		vao = -1;
		vbos = new ArrayList<Integer>();
		objects = new ArrayList<Shape>();
		vertexCount = -1;
		drawType = STATIC;
		
		layer = 0;
		globalLayer = 0f;
		this.texture = texture;
		
		hidden = false;
	}
	
	public Sprite(Texture texture, Shape object) {
		this(texture);
		addObject(object);
	}
	
	public boolean addObject(Shape object) {
		return objects.add(object);
	}
	
	public boolean removeObject(Shape object) {
		return objects.remove(object);
	}
	
	public int getLayer() {
		return layer;
	}
	
	public void setLayer(int layer) {
		this.layer = layer; 
	}
	
	public float getGlobalLayer() {
		return globalLayer;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public int create(long contextID, int layer) {
		if (objects.size() == 0) {
			addObject(new Rect(texture.getImage().getWidth(), texture.getImage().getHeight()));
		}
		
		if (vao >= 0) {
			glDeleteVertexArrays(vao);
		}
		
		texture.generateIfNot(contextID);
		
		vbos = new ArrayList<Integer>();
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		ArrayList<Float> positions = new ArrayList<Float>();
		ArrayList<Float> uvs = new ArrayList<Float>();
		
		ArrayList<Integer> indices = new ArrayList<Integer>();

		for (int i=0;i<objects.size();i++) {
			objects.get(i).resetVertices();
		}
		for (int i=0;i<objects.size();i++) {
			objects.get(i).addData(positions, uvs);
		}
		for (int i=0;i<objects.size();i++) {
			objects.get(i).addIndices(indices);
		}
		
		float max = Object2d.MAX_LAYERS + 2;
		globalLayer = (1f-(layer+this.layer/(MAX_LAYERS+1f))/max*2) - 1f/max;
		
		float[] pos = floatToArray(positions);
		
		for (int i=2;i<pos.length;i+=3) {
			pos[i] = globalLayer;
		}
		
		storeData(POSITION, 3, pos);
		storeData(UV, 2, floatToArray(uvs));
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
