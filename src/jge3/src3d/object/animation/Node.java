
package jge3.src3d.object.animation;

import jge3.math.Matrix4f;
import jge3.math.Quaternionf;
import jge3.math.Vector3f;
import jge3.engine.graphics.Graphics3d;
import jge3.src3d.object.Object3d;

public class Node {
	
	private String name;
	private Bone[] bones;
	private Matrix4f transformation;
	
	private Vector3f transform;
	private Quaternionf rotate;
	private Vector3f scale;
	
	private Node[] children;
	
	public Node(String name, Bone[] bones) {
		this.name = name;
		this.bones = bones;
		transformation = new Matrix4f().identity();
		
		transform = null;
		rotate = null;
		scale = null;
		
		children = new Node[0];
	}
	
	public String getName() {
		return name;
	}
	
	public Bone[] getBones() {
		return bones;
	}
	
	public Matrix4f getTransformMatrix() {
		return transformation;
	}
	
	public void setTransformMatrix(Matrix4f matrix) {
		transformation = matrix;
	}
	
	public void createBoneMatrix(Matrix4f parent) {
		applyTransformations(parent);
		
		for (int i=0;i<bones.length;i++) {
			bones[i].setFinalMatrix(new Matrix4f(parent));
		}
		for (int i=0;i<children.length;i++) {
			children[i].createBoneMatrix(new Matrix4f(parent));
		}
	}
	
	private void applyTransformations(Matrix4f mat) {
		if (transform == null && rotate == null && scale == null) {
			mat.mul(transformation);
		} else {
			if (transform != null) { 
				mat.translate(transform.x, transform.y, transform.z);
			}
			if (rotate != null) {
				mat.rotate(rotate);
			}
			if (scale != null) {
				mat.scale(scale.x, scale.y, scale.z);
			}
		}
	}
	
	public void visualize(Graphics3d g, Matrix4f parent, Object3d object, float size, Vector3f last) {
		applyTransformations(parent);
		Vector3f pos = object.getPosition().mul(parent, 1);
		if (last != null) {
			g.drawLine(last.x, last.y, -last.z, pos.x, pos.y, -pos.z);
		}
		g.drawRect(pos.x, pos.y, -pos.z, size, size, size);
		
		for (int i=0;i<children.length;i++) {
			children[i].visualize(g, new Matrix4f(parent), object, size, pos);
		}
	}
	
	public Vector3f getTransform() {
		return transform;
	}
	
	public void setTransform(Vector3f transform) {
		this.transform = transform;
	}
	
	public Quaternionf getRotation() {
		return rotate;
	}
	
	public void setRotation(Quaternionf rotation) {
		rotate = rotation;
	}
	
	public Vector3f getScale() {
		return scale; 
	}
	
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public Node[] getChildren() {
		return children;
	}
	
	public void setChildren(Node[] children) {
		this.children = children;
	}
	
}
