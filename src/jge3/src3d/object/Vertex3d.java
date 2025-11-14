
package jge3.src3d.object;

import java.util.ArrayList;
import java.util.List;

import jge3.math.Vector2f;
import jge3.math.Vector3f;
import jge3.src3d.object.animation.Bone;

public class Vertex3d {
	
	public static final int POSITION = 0;
	public static final int NORMAL = 1;
	public static final int UV = 2;
	public static final int BONES = 3;
	public static final int WEIGHTS = 4;
	
	public static final int MAX_BONES = 4;
	
	//
	
	private Vector3f position;
	private Vector3f normal;
	private Vector2f uv;
	
	private List<Bone> bones;
	private List<Float> weights;
	
	private int indice;
	
	public Vertex3d(float x, float y, float z) {
		this(new Vector3f(x, y, z), new Vector3f(), new Vector2f());
	}
	
	public Vertex3d(Vector3f position) {
		this(position, new Vector3f(), new Vector2f());
	}
	
	public Vertex3d(Vector3f position, Vector3f normal) {
		this(position, normal, new Vector2f());
	}
	
	public Vertex3d(Vector3f position, Vector3f normal, Vector2f uv) {
		this.position = position.clone();
		this.normal = normal.clone();
		this.uv = uv.clone();
		
		bones = new ArrayList<Bone>();
		weights = new ArrayList<Float>();
		
		indice = -1;
	}
	
	public Vector3f getPosition() {
		return position.clone();
	}
	
	public Vector3f getNormal() {
		return normal.clone();
	}
	
	public Vector2f getUV() {
		return uv.clone();
	}
	
	public void reset() {
		indice = -1;
	}
	
	public boolean addBone(Bone bone, float weight) {
		return bones.add(bone) && weights.add(weight);
	}
	
	public boolean removeBone(Bone bone) {
		int i = bones.indexOf(bone);
		if (i < 0) {
			return false;
		}
		bones.remove(i);
		weights.remove(i);
		return true;
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
		positions.add(-position.z);
	}
	
	public void addToNormals(ArrayList<Float> normals) {
		if (indice >= 0) {
			return;
		}
		normals.add(normal.x);
		normals.add(normal.y);
		normals.add(-normal.z);
	}
	
	public void addToUVs(ArrayList<Float> uvs) {
		if (indice >= 0) {
			return;
		}
		uvs.add(uv.x);
		uvs.add(1-uv.y);
	}
	
	public void addToBoneIDs(ArrayList<Integer> boneIDs) {
		if (indice >= 0) {
			return;
		}
		int length = bones.size();
		if (length > MAX_BONES) {
			length = MAX_BONES;
		}
		int i = 0;
		for (;i<length;i++) {
			boneIDs.add(bones.get(i).getID());
			if (bones.get(i).getID() > 200) {
				System.out.println("A");
			}
		}
		for (;i<MAX_BONES;i++) {
			boneIDs.add(-1);
		}
	}
	
	public void addToWeights(ArrayList<Float> weights) {
		if (indice >= 0) {
			return;
		}
		int length = this.weights.size();
		if (length > MAX_BONES) {
			length = MAX_BONES;
		}
		int i = 0;
		for (;i<length;i++) {
			weights.add(this.weights.get(i));
		}
		for (;i<MAX_BONES;i++) {
			weights.add(0f);
		}
	}
	
	public void updateVertexData() {
	}
	
}
