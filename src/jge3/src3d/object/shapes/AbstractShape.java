package jge3.src3d.object.shapes;

import java.util.ArrayList;
import java.util.List;

import jge3.src3d.object.Vertex3d;
import jge3.utils.IntersectUtils;

public abstract class AbstractShape implements Shape {
	
	protected int increment;
	protected Vertex3d[] vertices;
	protected ArrayList<Integer> indices;
	
	private List<Tri> triangles;
	
	private class Tri {
		public Vertex3d v1;
		public Vertex3d v2;
		public Vertex3d v3;
		public Tri() {
			v1 = null;
			v2 = null;
			v3 = null;
		}
		public boolean full() {
			return v1 != null && v2 != null && v3 != null;
		}
		public boolean put(Vertex3d v) {
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
	public void addData(ArrayList<Float> positions, ArrayList<Float> normals, ArrayList<Float> uvs,  ArrayList<Integer> boneIDs,  ArrayList<Float> weights) {
		for (int i=0;i<vertices.length;i++) {
			vertices[i].addToPositions(positions);
			vertices[i].addToNormals(normals);
			vertices[i].addToUVs(uvs);
			vertices[i].addToBoneIDs(boneIDs);
			vertices[i].addToWeights(weights);
			
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
	
	public void indice(int vertex) {
		indice(vertices[vertex]);
	}
	
	protected void indice(Vertex3d vertex) {
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
	public float touching(float x, float y, float z, float dirx, float diry, float dirz, float min) {
		float minDistance = -1;
		int length = triangles.size();
		for (int i=0;i<length;i++) {
			Tri tri = triangles.get(i);
			float intersect = IntersectUtils.intersectRayTriangle(x, y, z, dirx, diry, dirz, tri.v1.getPosition(), tri.v2.getPosition(), tri.v3.getPosition());
			if (intersect >= 0) {
				float intersectZ = intersect;
				if (minDistance < 0 || intersectZ < minDistance) {
					minDistance = intersectZ;
				}
			}
		}
		return minDistance < 0 ? min : minDistance;
	}
	
}
