
package jge3.src3d.object;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import jge3.engine.graphics.Graphics3d;
import org.lwjgl.system.MemoryStack;

import jge3.common.interactable.Interactable;
import jge3.math.Matrix4f;
import jge3.math.Vector3f;
import jge3.src3d.object.animation.Animation;
import jge3.src3d.object.animation.Bone;
import jge3.src3d.object.animation.Node;
import jge3.src3d.object.audio.Source3d;
import jge3.src3d.object.material.Material;
import jge3.src3d.object.shapes.Shape;

public class Object3d extends Object implements Interactable {
	
	public static final String OBJECT_MATRIX = "objectMatrix";
	
	public static final int USE_ONLY = 1
						,DONT_USE = 2;
	
	//
	
	protected int[] vaos;
	protected List<Mesh> meshes;
	
	protected List<Source3d> sources;
	protected List<Animation> animations;
	protected Node rootNode;
	
	protected int objectUniformID;
	protected Matrix4f finalMatrix;
	
	protected boolean hidden;
	
	public Object3d() {
		super();
		resetLists();
		resetOther();
	}
	
	public Object3d(Mesh mesh) {
		this();
		addMesh(mesh);
	}
	
	public Object3d(Shape shape, Material material) {
		this();
		Mesh mesh = new Mesh(material);
		mesh.addObject(shape);
		addMesh(mesh);
	}
	
	public Object3d(Vector3f position) {
		super(position);
		resetLists();
		resetOther();
	}
	
	public Object3d(Vector3f position, Vector3f rotation) {
		super(position, rotation);
		resetLists();
		resetOther();
	}
	
	public Object3d(float x, float y, float z) {
		position = new Vector3f(x, y, z);
		rotation = new Vector3f();
		resetTranslate();
		resetRotation();
		resetLists();
		resetOther();
	}
	
	public Object3d(float x, float y, float z, float rx, float ry, float rz) {
		position = new Vector3f(x, y, z);
		rotation = new Vector3f(rx, ry, rz);
		resetTranslate();
		resetRotation();
		resetLists();
		resetOther();
	}
	
	private void resetLists() {
		vaos = new int[0];
		meshes = new ArrayList<Mesh>();
		sources = new ArrayList<Source3d>();
		animations = new ArrayList<Animation>();
	}
	
	private void resetOther() {
		rootNode = null;
		hidden = false;
	}
	
	public boolean addMesh(Mesh mesh) {
		return meshes.add(mesh);
	}
	
	public boolean removeMesh(Mesh mesh) {
		return meshes.remove(mesh);
	}
	
	public boolean addSource(Source3d source) {
		return sources.add(source);
	}
	
	public boolean removeSource(Source3d source) {
		return sources.remove(source);
	}
	
	public boolean addAnimation(Animation animation) {
		return animations.add(animation);
	}
	
	public boolean removeAnimation(Animation animation) {
		return animations.remove(animation);
	}
	
	public Node getRootNode() {
		return rootNode;
	}
	
	public void setRootNode(Node node) {
		rootNode = node;
	}
	
	public void delete() {
		for (int i=0;i<vaos.length;i++) {
			glDeleteVertexArrays(vaos[i]);
		}
		vaos = new int[0];
		
		while (sources.size() > 0) {
			sources.remove(0).delete();
		}
	}
	
	public Object3d create() {
		long glContextID = glfwGetCurrentContext();
		long alContextID = alcGetCurrentContext();
		
		vaos = new int[meshes.size()];
		for (int i=0;i<vaos.length;i++) {
			vaos[i] = meshes.get(i).create(glContextID);
		}
		
		int length = sources.size();
		for (int i=0;i<length;i++) {
			sources.get(i).create(alContextID, position);
		}
		
		return this;
	}
	
	public void updateVertexData() {
		for (int i=0;i<vaos.length;i++) {
			meshes.get(i).updateVertexData();
		}
	}
	
	public void updateSourceData(long contextID) {
		int length = sources.size();
		for (int i=0;i<length;i++) {
			sources.get(i).updateData(position, contextID);
		}
	}
	
	public void bindPose() {
		setNull(rootNode);
		rootNode.createBoneMatrix(new Matrix4f());
		int length = animations.size();
		for (int i=0;i<length;i++) {
			animations.get(i).setTick(0f);
		}
	}
	
	public void visualize(Graphics3d g) {
		rootNode.visualize(g, new Matrix4f(finalMatrix), this, 10, null);
	}
	
	public void visualize(Graphics3d g, float size) {
		rootNode.visualize(g, new Matrix4f(finalMatrix), this, size, null);
	}
	
	private void setNull(Node node) {
		node.setTransform(null);
		node.setRotation(null);
		node.setScale(null);
		Node[] children = node.getChildren();
		for (int i=0;i<children.length;i++) {
			setNull(children[i]);
		}
	}
	
	public void calculateObjectMatrix() {
		finalMatrix = transform.getObjectMatrix();
	}
	
	public Matrix4f getObjectMatrix() {
		return finalMatrix;
	}
	
	public void bindObjectMatrix(int programID) {
		objectUniformID = glGetUniformLocation(programID, OBJECT_MATRIX);
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        finalMatrix.get(fb);
	        glUniformMatrix4fv(objectUniformID, false, fb);
		}
	}
	
	public void bindBoneMatrices(int programID) {
		int count = meshes.size();
		for (int i=0;i<count;i++) {
			Bone[] bones = meshes.get(i).getBones();
			for (int j=0;j<bones.length;j++) {
				bones[j].bindBoneMatrix(programID);
			}
		}
	}
	
	public float touching(float x, float y, float z, float dirx, float diry, float dirz) {
		float minDistance = -1;
		for (int i=0;i<getMeshCount();i++) {
			if (getMesh(i).isHidden()) {
				continue;
			}
			
			Shape[] shapes = getMesh(i).getObjects();
			for (int j=0;j<shapes.length;j++) {
				minDistance = shapes[j].touching(x, y, z, dirx, diry, dirz, minDistance);
			}
		}
		return minDistance;
	}
	
	@Override
	public void mouseEnter() {}
	
	@Override
	public void mouseExit() {}
	
	@Override
	public void mouseDown(int button) {}
	
	@Override
	public void mouseUp(int button) {}
	
	public int[] getVAOs() {
		return vaos;
	}
	
	public int getVAO(int i) {
		return vaos[i];
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public Mesh[] getMeshes() {
		Mesh[] meshes = new Mesh[this.meshes.size()];
		for (int i=0;i<meshes.length;i++) {
			meshes[i] = this.meshes.get(i);
		}
		return meshes;
	}
	
	public int getMeshCount() {
		return vaos.length;
	}
	
	public Mesh getMesh(int i) {
		return meshes.get(i);
	}
	
	public Source3d[] getSources() {
		Source3d[] sources = new Source3d[this.sources.size()];
		for (int i=0;i<sources.length;i++) {
			sources[i] = this.sources.get(i);
		}
		return sources;
	}
	
	public int getSourceCount() {
		return sources.size();
	}
	
	public Source3d getSource(int i) {
		return sources.get(i);
	}
	
	public Animation[] getAnimations() {
		Animation[] animations = new Animation[this.animations.size()];
		for (int i=0;i<animations.length;i++) {
			animations[i] = this.animations.get(i);
		}
		return animations;
	}
	
	public int getAnimationCount() {
		return animations.size();
	}
	
	public Animation getAnimation(int i) {
		return animations.get(i);
	}
	
}
