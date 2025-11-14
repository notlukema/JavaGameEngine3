
package jge3.src3d.object.shapes;

import java.util.ArrayList;

import jge3.math.Vector2f;
import jge3.math.Vector3f;
import jge3.src3d.object.Vertex3d;

public class Sphere extends AbstractShape {
	
	private float size;
	private ArrayList<Tri> triangles;
	
	private float x;
	private float y;
	private float z;
	
	private class V {
		public float x;
		public float y;
		public float z;
		public Vertex3d v;
		public V(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
			v = null;
		}
		public V clone() {
			return new V(x, y, z);
		}
	}
	
	private class Tri {
		public V v1;
		public V v2;
		public V v3;
		public Tri(V v1, V v2, V v3) {
			this.v1 = v1;
			this.v2 = v2;
			this.v3 = v3;
		}
		public Tri clone() {
			return new Tri(v1, v2, v3);
		}
	}
	
	public Sphere(float size, int divisions) {
		this(size, divisions, 0f, 0f, 0f);
	}
	
	public Sphere(float size, int divisions, float x, float y, float z) {
		this.size = size;
		triangles = new ArrayList<Tri>();
		
		vertices = new Vertex3d[0];
		
		this.x = x;
		this.y = y;
		this.z = z;

		V v1 = new V(size, 0, 0);
		V v2 = new V(-size, 0, 0);
		V v3 = new V(0, size, 0);
		V v4 = new V(0, -size, 0);
		V v5 = new V(0, 0, size);
		V v61 = new V(0, 0, -size);
		V v62 = new V(0, 0, -size);
		
		triangles.add(new Tri(v1, v3, v5));
		triangles.add(new Tri(v3, v1, v61));
		triangles.add(new Tri(v3, v2, v5));
		triangles.add(new Tri(v2, v3, v62));
		triangles.add(new Tri(v4, v1, v5));
		triangles.add(new Tri(v1, v4, v61));
		triangles.add(new Tri(v2, v4, v5));
		triangles.add(new Tri(v4, v2, v62));
		
		for (int i=0;i<divisions;i++) {
			int count = triangles.size();
			for (int j=0;j<count;j++) {
				Tri tri = triangles.get(0);
				
				V v01 = tri.v1;
				V v02 = tri.v2;
				V v03 = tri.v3;
				V v04 = new V((v01.x+v02.x)/2, (v01.y+v02.y)/2, (v01.z+v02.z)/2);
				V v05 = new V((v02.x+v03.x)/2, (v02.y+v03.y)/2, (v02.z+v03.z)/2);
				V v06 = new V((v01.x+v03.x)/2, (v01.y+v03.y)/2, (v01.z+v03.z)/2);
				triangles.add(new Tri(v01, v04, v06));
				triangles.add(new Tri(v04, v02, v05));
				triangles.add(new Tri(v05, v03, v06));
				
				triangles.add(new Tri(v04, v05, v06));
				
				triangles.remove(0);
			}
		}
		
		int length = triangles.size();
		/* this will prevent uv mapping from warping but idc anymore
		for (int i=0;i<length;i++) {
			Tri tri = triangles.get(i);
			if (onBorder(tri.v1)) {
				tri.v1 = tri.v1.clone();
			}
			if (onBorder(tri.v2)) {
				tri.v3 = tri.v2.clone();
			}
			if (onBorder(tri.v2)) {
				tri.v3 = tri.v2.clone();
			}
		}
		*/
		for (int i=0;i<length;i++) {
			V v01 = triangles.get(i).v1;
			V v02 = triangles.get(i).v2;
			V v03 = triangles.get(i).v3;
			vertex(v01, v02.x>0||v03.x>0, v02.z>0||v03.z>0);
			vertex(v02, v01.x>0||v03.x>0, v01.z>0||v03.z>0);
			vertex(v03, v01.x>0||v02.x>0, v01.z>0||v02.z>0);
		}
	}
	
	private boolean onBorder(V v) {
		return v.x == 0 && v.z <= 0;
	}
	
	private void vertex(V v, boolean right, boolean front) {
		if (v.v != null) {
			return;
		}
		Vector3f normal = new Vector3f(v.x, v.y, v.z);
		normal.normalize();
		Vector3f position = new Vector3f(normal.x*size, normal.y*size, normal.z*size);
		double dir;
		boolean onBorder = onBorder(v);
		if (onBorder) {
			dir = right?0:2*Math.PI;
		} else {
			dir = Math.PI+Math.atan(-position.x/position.z);
			if (position.z < 0) {
				dir += Math.PI;
			}
			dir %= 2*Math.PI;
		}
		double xp = dir/(2*Math.PI);
		double yp = 1-Math.acos(position.y/size)/Math.PI;
		if (yp == 0 || yp == 1) {
			xp = 0.5;
		}
		Vector2f uv = new Vector2f((float)xp, (float)yp);
		v.v = new Vertex3d(new Vector3f(x+position.x, y+position.y, z+position.z), normal, uv);
	}
	
	@Override
	public void resetVertices() {
		super.resetVertices();
		int length = triangles.size();
		for (int i=0;i<length;i++) {
			triangles.get(i).v1.v.reset();
			triangles.get(i).v2.v.reset();
			triangles.get(i).v3.v.reset();
		}
	}
	
	private ArrayList<Float> p;
	private ArrayList<Float> n;
	private ArrayList<Float> uv;
	private ArrayList<Integer> b;
	private ArrayList<Float> w;
	
	@Override
	public void addData(ArrayList<Float> positions, ArrayList<Float> normals, ArrayList<Float> uvs,  ArrayList<Integer> boneIDs,  ArrayList<Float> weights) {
		super.addData(positions, normals, uvs, boneIDs, weights);
		p = positions;
		n = normals;
		uv = uvs;
		b = boneIDs;
		w = weights;
		int length = triangles.size();
		for (int i=0;i<length;i++) {
			vertexData(triangles.get(i).v1.v);
			vertexData(triangles.get(i).v2.v);
			vertexData(triangles.get(i).v3.v);
		}
	}
	
	private void vertexData(Vertex3d vertex) {
		vertex.addToPositions(p);
		vertex.addToNormals(n);
		vertex.addToUVs(uv);
		vertex.addToBoneIDs(b);
		vertex.addToWeights(w);
		vertex.setIndice((p.size()/3)-1);
	}

	@Override
	public void addIndices() {
		int length = triangles.size();
		for (int i=0;i<length;i++) {
			indice(triangles.get(i).v1.v);
			indice(triangles.get(i).v2.v);
			indice(triangles.get(i).v3.v);
		}
	}
	
}
