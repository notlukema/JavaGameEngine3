
package jge3.src2d.object;

import java.util.ArrayList;

import jge3.math.Vector2f;

public class Vertex2d {
	
	public static final int POSITION = 0;
	public static final int UV = 1;
	
	//
	
	private Vector2f position;
	private Vector2f uv;
	
	private int indice;
	
	public Vertex2d(float x, float y) {
		this(new Vector2f(x, y), new Vector2f());
	}
	
	public Vertex2d(float x, float y, float u, float v) {
		position = new Vector2f(x, y);
		uv = new Vector2f(u, v);
		
		indice = -1;
	}
	
	public Vertex2d(Vector2f position) {
		this(position, new Vector2f());
	}
	
	public Vertex2d(Vector2f position, Vector2f uv) {
		this.position = position.clone();
		this.uv = uv.clone();
		
		indice = -1;
	}
	
	public Vector2f getPosition() {
		return position.clone();
	}
	
	public Vector2f getUV() {
		return uv.clone();
	}
	
	public void reset() {
		indice = -1;
	}
	
	public int getIndice() {
		return indice;
	}
	
	public void setIndice(int i) {
		if (indice >= 0) {
			return;
		}
		indice = i;
	}
	
	public void addIndice(ArrayList<Integer> indices) {
		indices.add(indice);
	}
	
	public void addToPositions(ArrayList<Float> positions) {
		if (indice >= 0) {
			return;
		}
		positions.add(position.x);
		positions.add(position.y);
		positions.add(0f);
	}
	
	public void addToUVs(ArrayList<Float> uvs) {
		if (indice >= 0) {
			return;
		}
		uvs.add(uv.x);
		uvs.add(1-uv.y);
	}
	
	public void updateVertexData() {
	}
	
}
