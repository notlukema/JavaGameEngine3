package jge3.src2d.object.shapes;

import java.util.ArrayList;
import java.util.List;

import jge3.math.Vector2f;
import jge3.src2d.object.Vertex2d;

public abstract class AbstractShape implements Shape {
	
	protected int increment;
	protected Vertex2d[] vertices;
	protected ArrayList<Integer> indices;
	
	private List<Tri> triangles;
	
	private class Tri {
		public Vertex2d v1;
		public Vertex2d v2;
		public Vertex2d v3;
		public Tri() {
			v1 = null;
			v2 = null;
			v3 = null;
		}
		public boolean full() {
			return v1 != null && v2 != null && v3 != null;
		}
		public boolean put(Vertex2d v) {
			if (v1 == null) {
				v1 = v;
			} else if (v2 == null) {
				v2 = v;
			} else if (v3 == null) {
				v3 = v;
			} else {
				return false;
			}
			return true;
		}
	}
	
	protected void error(String name, int vertexCount) {
		throw new RuntimeException("Unsuitable vertex count for shape \""+name+"\": "+vertexCount);
	}
	
	@Override
	public void resetVertices() {
		for (int i=0;i<vertices.length;i++) {
			vertices[i].reset();
		}
		triangles = new ArrayList<Tri>();
	}
	
	@Override
	public void addData(ArrayList<Float> positions, ArrayList<Float> uvs) {
		for (int i=0;i<vertices.length;i++) {
			vertices[i].addToPositions(positions);
			vertices[i].addToUVs(uvs);
			
			vertices[i].setIndice((positions.size()/3)-1);
		}
		triangles.add(new Tri());
	}
	
	@Override
	public void addIndices(ArrayList<Integer> indices) {
		this.indices = indices;
		addIndices();
	}
	
	abstract void addIndices();
	
	protected void indice(int vertex) {
		indice(vertices[vertex]);
	}
	
	protected void indice(Vertex2d vertex) {
		vertex.addIndice(indices);
		Tri tri = triangles.get(triangles.size()-1);
		if (tri.full()) {
			tri = new Tri();
			triangles.add(tri);
		}
		tri.put(vertex);
	}
	
	@Override
	public void updateVertexData() {
		for (int i=0;i<vertices.length;i++) {
			vertices[i].updateVertexData();
		}
	}
	
	@Override
	public boolean touching(float x, float y) {
		int length = triangles.size();
		for (int i=0;i<length;i++) {
			Tri tri = triangles.get(i);
			Vector2f pos1 = tri.v1.getPosition();
			Vector2f pos2 = tri.v2.getPosition();
			Vector2f pos3 = tri.v3.getPosition();
			float d1 = (pos2.y-pos1.y)*(x-pos1.x) - (pos2.x-pos1.x)*(y-pos1.y);
			float d2 = (pos3.y-pos2.y)*(x-pos2.x) - (pos3.x-pos2.x)*(y-pos2.y);
			float d3 = (pos1.y-pos3.y)*(x-pos3.x) - (pos1.x-pos3.x)*(y-pos3.y);
			if ((d1 >= 0 && d2 >= 0 && d3 >= 0) || (d1 <= 0 && d2 <= 0 && d3 <= 0)) {
				return true;
			}
		}
		return false;
	}
	
}
