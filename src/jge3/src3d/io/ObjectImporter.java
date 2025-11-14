
package jge3.src3d.io;

import static org.lwjgl.assimp.Assimp.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryStack;

import jge3.common.image.Texture;
import jge3.math.Matrix4f;
import jge3.math.Quaternionf;
import jge3.math.Vector2f;
import jge3.math.Vector3f;
import jge3.math.Vector4f;
import jge3.src3d.object.Mesh;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.Vertex3d;
import jge3.src3d.object.animation.Animation;
import jge3.src3d.object.animation.Bone;
import jge3.src3d.object.animation.Channel;
import jge3.src3d.object.animation.Node;
import jge3.src3d.object.material.Material;
import jge3.src3d.object.shapes.Polygon;
import jge3.src3d.object.shapes.Shape;
import jge3.utils.ImageUtils;

public class ObjectImporter {
	
	private String filePath;
	
	public ObjectImporter() {
		filePath = "";
	}
	
	public ObjectImporter(String filePath) {
		this.filePath = filePath;
	}
	
	public void setPath(String filePath) {
		this.filePath = filePath;
	}
	
	public Object3d create() {
		String basePath = filePath.substring(0, filePath.lastIndexOf("/")+1);
		Object3d object = new Object3d();
		
		AIScene scene = aiImportFile(filePath, 0);
		
		if (scene == null) {
			System.out.println("Unable to load model \""+filePath+"\"");
			return new Object3d();
		}
		
		int meshCount = scene.mNumMeshes();
		int materialCount = scene.mNumMaterials();
		PointerBuffer meshesBuffer = scene.mMeshes();
		PointerBuffer materialsBuffer = scene.mMaterials();
		
		//ArrayList<Mesh> meshes = new ArrayList<Mesh>();
		ArrayList<Material> materials = new ArrayList<Material>();
		
		for (int i=0;i<materialCount;i++) {
			AIMaterial material = AIMaterial.create(materialsBuffer.get(i));
			Material m = new Material();
			Vector4f c;
			String tp;
			
			// emissive
			// reflective
			
			c = materialColor(AI_MATKEY_COLOR_DIFFUSE, material);
			if (c != null) {
				// alpha - transparent
				m.setDiffuseMap(new Texture(c));
			}
			
			c = materialColor(AI_MATKEY_COLOR_SPECULAR, material);
			if (c != null) {
				m.setSpecularMap(new Texture(c));
			}
			
			c = materialColor(AI_MATKEY_COLOR_AMBIENT, material);
			if (c != null) {
				m.setAmbientMap(new Texture(c));
			}
			
			tp = texturePath(aiTextureType_DIFFUSE, material);
			if (tp != null) {
				// alpha - opacity
				m.setDiffuseMap(new Texture(ImageUtils.loadImage(basePath+tp)));
			}
			
			tp = texturePath(aiTextureType_SPECULAR, material);
			if (tp != null) {
				m.setSpecularMap(new Texture(ImageUtils.loadImage(basePath+tp)));
			}
			
			tp = texturePath(aiTextureType_AMBIENT, material);
			if (tp != null) {
				m.setAmbientMap(new Texture(ImageUtils.loadImage(basePath+tp)));
			}
			
			tp = texturePath(aiTextureType_NORMALS, material);
			if (tp != null) {
				m.setNormalMap(new Texture(ImageUtils.loadImage(basePath+tp)));
			}
			
			materials.add(m);
		}
		
		ArrayList<Vertex3d> v = new ArrayList<Vertex3d>();
		ArrayList<Shape> s = new ArrayList<Shape>();
		ArrayList<Bone> boneList = new ArrayList<Bone>();
		
		for (int i=0;i<meshCount;i++) {
			AIMesh mesh = AIMesh.create(meshesBuffer.get(i));
			Mesh m = new Mesh(materials.get(mesh.mMaterialIndex()));
			
			AIVector3D.Buffer verticesBuffer = mesh.mVertices();
			AIVector3D.Buffer normalsBuffer = mesh.mNormals();
			int vertexCount = mesh.mNumVertices();
			for (int j=0;j<vertexCount;j++) {
				AIVector3D pos = verticesBuffer.get(j);
				Vector3f position = new Vector3f(pos.x(), pos.y(), -pos.z());
				AIVector3D norm;
				Vector3f normal = new Vector3f();
				if (normalsBuffer != null) {
					norm = normalsBuffer.get(j);
					normal.x = norm.x();
					normal.y = norm.y();
					normal.z = -norm.z();
					normal.safeNormalize();
				}
				AIVector3D uv = null;
				if (mesh.mTextureCoords(0) != null) {
					uv = mesh.mTextureCoords(0).get(j);
				}
				if (uv == null) {
					v.add(new Vertex3d(position, new Vector3f(normal.x, normal.y, normal.z), new Vector2f()));
				} else {
					v.add(new Vertex3d(position, new Vector3f(normal.x, normal.y, normal.z), new Vector2f(uv.x(), uv.y())));
				}
			}
			
			PointerBuffer bones = mesh.mBones();
			int boneCount = mesh.mNumBones();
			for (int j=0;j<boneCount;j++) {
				AIBone bone = AIBone.create(bones.get(j));
				Bone b = new Bone(bone.mName().dataString());
				
				AIMatrix4x4 offset = bone.mOffsetMatrix();
				Matrix4f offsetMatrix = toMatrix(offset);
				
				b.setOffsetMatrix(offsetMatrix);
				
				AIVertexWeight.Buffer weights = bone.mWeights();
				int weightCount = bone.mNumWeights();
				for (int h=0;h<weightCount;h++) {
					AIVertexWeight weight = weights.get(h);
					v.get(weight.mVertexId()).addBone(b, weight.mWeight());
				}
				
				m.addBone(b);
				boneList.add(b);
			}
			
			AIFace.Buffer facesBuffer = mesh.mFaces();
			int faceCount = mesh.mNumFaces();
			for (int j=0;j<faceCount;j++) {
				AIFace face = facesBuffer.get(j);
				IntBuffer indices = face.mIndices();
				Vertex3d[] vs = new Vertex3d[face.mNumIndices()];
				for (int h=0;h<vs.length;h++) {
					vs[h] = v.get(indices.get(h));
				}
				s.add(new Polygon(vs));
			}
			
			for (int j=0;j<s.size();j++) {
				m.addObject(s.get(j));
			}
			object.addMesh(m);
			
			v.clear();
			s.clear();
		}
		
		HashMap<String, Node> nodeMap = new HashMap<String, Node>();
		AINode root = scene.mRootNode();
		Bone[] boneArray = new Bone[boneList.size()];
		for (int i=0;i<boneArray.length;i++) {
			boneArray[i] = boneList.get(i);
		}
		Node rootNode = createNode(root, boneArray, nodeMap);
		object.setRootNode(rootNode);
		
		PointerBuffer animations = scene.mAnimations();
		int animationsCount = scene.mNumAnimations();
		for (int i=0;i<animationsCount;i++) {
			AIAnimation aiAnimation = AIAnimation.create(animations.get(i));
			Animation animation = new Animation(aiAnimation.mName().dataString(), rootNode);
			animation.setDuration((float)aiAnimation.mDuration());
			animation.setTicksPerSecond((float)aiAnimation.mTicksPerSecond());
			
			PointerBuffer channels = aiAnimation.mChannels();
			Channel[] c = new Channel[aiAnimation.mNumChannels()];
			for (int j=0;j<c.length;j++) {
				AINodeAnim aiChannel = AINodeAnim.create(channels.get(j));
				Node n = nodeMap.get(aiChannel.mNodeName().dataString());
				if (n == null) {
					continue;
				}
				c[j] = new Channel(n);
				
				AIVectorKey.Buffer positions = aiChannel.mPositionKeys();
				AIQuatKey.Buffer rotations = aiChannel.mRotationKeys();
				AIVectorKey.Buffer scalings = aiChannel.mScalingKeys();
				
				Vector3f[] pos = new Vector3f[aiChannel.mNumPositionKeys()];
				Quaternionf[] rot = new Quaternionf[aiChannel.mNumRotationKeys()];
				Vector3f[] scal = new Vector3f[aiChannel.mNumScalingKeys()];
				
				float[] posTime = new float[pos.length];
				float[] rotTime = new float[rot.length];
				float[] scalTime = new float[scal.length];
				
				for (int h=0;h<pos.length;h++) {
					AIVector3D aiPosition = positions.get(h).mValue();
					pos[h] = new Vector3f(aiPosition.x(), aiPosition.y(), aiPosition.z());
					posTime[h] = (float)positions.get(h).mTime();
				}
				for (int h=0;h<rot.length;h++) {
					AIQuaternion aiRotation = rotations.get(h).mValue();
					rot[h] = new Quaternionf(aiRotation.x(), aiRotation.y(), aiRotation.z(), aiRotation.w());
					rotTime[h] = (float)rotations.get(h).mTime();
				}
				for (int h=0;h<scal.length;h++) {
					AIVector3D aiScaling = scalings.get(h).mValue();
					scal[h] = new Vector3f(aiScaling.x(), aiScaling.y(), aiScaling.z());
					scalTime[h] = (float)scalings.get(h).mTime();
				}
				
				c[j].setPositions(pos);
				c[j].setRotations(rot);
				c[j].setScalings(scal);
				c[j].setPositionTimeStamps(posTime);
				c[j].setRotationTimeStamps(rotTime);
				c[j].setScalingTimeStamps(scalTime);
			}
			
			animation.setChannels(c);
			object.addAnimation(animation);
		}
		
		aiReleaseImport(scene);
		
		return object;
	}
	
	private Node createNode(AINode aiNode, Bone[] bones, HashMap<String, Node> nodeMap) {
		String name = aiNode.mName().dataString();
		
		ArrayList<Bone> bonesList = new ArrayList<Bone>();
		for (int i=0;i<bones.length;i++) {
			if (bones[i].getName().equals(name)) {
				bonesList.add(bones[i]);
			}
		}
		Bone[] bonesArray = new Bone[bonesList.size()];
		for (int i=0;i<bonesArray.length;i++) {
			bonesArray[i] = bonesList.get(i);
		}
		
		Node node = new Node(name, bonesArray);
		
		nodeMap.put(name, node);
		node.setTransformMatrix(toMatrix(aiNode.mTransformation()));
		PointerBuffer aiChildren = aiNode.mChildren();
		Node[] children = new Node[aiNode.mNumChildren()];
		for (int i=0;i<children.length;i++) {
			children[i] = createNode(AINode.create(aiChildren.get(i)), bones, nodeMap);
			//try{System.out.println(children[i].getBone().getName()+"->"+node.getBone().getName());}catch(Exception e){}
		}
		node.setChildren(children);
		return node;
	}
	
	public Object3d[] createMultiple(float scale) {
		String basePath = filePath.substring(0, filePath.lastIndexOf("/")+1);
		ArrayList<Object3d> objects = new ArrayList<Object3d>();
		AIScene scene = aiImportFileEx(filePath, 0, null);
		if (scene == null) {
			System.out.println("Unable to load model \""+filePath+"\"");
			return new Object3d[0];
		}
		int meshCount = scene.mNumMeshes();
		int materialCount = scene.mNumMaterials();
		PointerBuffer meshesBuffer = scene.mMeshes();
		PointerBuffer materialsBuffer = scene.mMaterials();
		ArrayList<Material> materials = new ArrayList<Material>();
		for (int i=0;i<materialCount;i++) {
			AIMaterial material = AIMaterial.create(materialsBuffer.get(i));
			Material m = new Material();
			Vector4f c;
			String tp;
			c = materialColor(AI_MATKEY_COLOR_DIFFUSE, material);
			if (c != null) {
				m.setDiffuseMap(new Texture(c));
			}
			c = materialColor(AI_MATKEY_COLOR_SPECULAR, material);
			if (c != null) {
				m.setSpecularMap(new Texture(c));
			}
			c = materialColor(AI_MATKEY_COLOR_AMBIENT, material);
			if (c != null) {
				m.setAmbientMap(new Texture(c));
			}
			tp = texturePath(aiTextureType_DIFFUSE, material);
			if (tp != null) {
				m.setDiffuseMap(new Texture(ImageUtils.loadImage(basePath+tp)));
			}
			tp = texturePath(aiTextureType_SPECULAR, material);
			if (tp != null) {
				m.setSpecularMap(new Texture(ImageUtils.loadImage(basePath+tp)));
			}
			tp = texturePath(aiTextureType_AMBIENT, material);
			if (tp != null) {
				m.setAmbientMap(new Texture(ImageUtils.loadImage(basePath+tp)));
			}
			tp = texturePath(aiTextureType_NORMALS, material);
			if (tp != null) {
				m.setNormalMap(new Texture(ImageUtils.loadImage(basePath+tp)));
			}
			materials.add(m);
		}
		ArrayList<Vertex3d> v = new ArrayList<Vertex3d>();
		ArrayList<Shape> s = new ArrayList<Shape>();
		for (int i=0;i<meshCount;i++) {
			Object3d object = new Object3d();
			AIMesh mesh = AIMesh.create(meshesBuffer.get(i));
			Mesh m = new Mesh(materials.get(mesh.mMaterialIndex()));
			AIVector3D.Buffer verticesBuffer = mesh.mVertices();
			AIVector3D.Buffer normalsBuffer = mesh.mNormals();
			int vertexCount = mesh.mNumVertices();
			for (int j=0;j<vertexCount;j++) {
				AIVector3D pos = verticesBuffer.get(j);
				Vector3f position = new Vector3f(pos.x()*scale, pos.y()*scale, -pos.z()*scale);
				AIVector3D norm;
				Vector3f normal = new Vector3f();
				if (normalsBuffer != null) {
					norm = normalsBuffer.get(j);
					normal.x = norm.x();
					normal.y = norm.y();
					normal.z = -norm.z();
					normal.safeNormalize();
				}
				AIVector3D uv = null;
				if (mesh.mTextureCoords(0) != null) {
					uv = mesh.mTextureCoords(0).get(j);
				}
				if (uv == null) {
					v.add(new Vertex3d(position, new Vector3f(normal.x, normal.y, normal.z), new Vector2f()));
				} else {
					v.add(new Vertex3d(position, new Vector3f(normal.x, normal.y, normal.z), new Vector2f(uv.x(), uv.y())));
				}
			}
			AIFace.Buffer facesBuffer = mesh.mFaces();
			int faceCount = mesh.mNumFaces();
			for (int j=0;j<faceCount;j++) {
				AIFace face = facesBuffer.get(j);
				IntBuffer indices = face.mIndices();
				Vertex3d[] vs = new Vertex3d[face.mNumIndices()];
				for (int h=0;h<vs.length;h++) {
					vs[h] = v.get(indices.get(h));
				}
				s.add(new Polygon(vs));
			}
			for (int j=0;j<s.size();j++) {
				m.addObject(s.get(j));
			}
			object.addMesh(m);
			objects.add(object);
			v.clear();
			s.clear();
		}
		Object3d[] objects2 = new Object3d[objects.size()]; 
		for (int i=0;i<objects2.length;i++) {
			objects2[i] = objects.get(i);
		}
		aiReleaseImport(scene);
		return objects2;
	}
	
	private Vector4f materialColor(String type, AIMaterial material) {
		AIColor4D color = AIColor4D.create();
		if (aiGetMaterialColor(material, type, aiTextureType_NONE, 0, color) == aiReturn_SUCCESS) {
			return new Vector4f(color.r(), color.g(), color.b(), color.a());
		} else {
			return null;
		}
	}
	
	private String texturePath(int type, AIMaterial material) {
		if (aiGetMaterialTextureCount(material, type) > 0) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				AIString texturePath = AIString.calloc(stack);
				aiGetMaterialTexture(material, type, 0, texturePath, (IntBuffer)null, null, null, null, null, null);
				return texturePath.dataString();
			}
		} else {
			return null;
		}
	}
	
	private Matrix4f toMatrix(AIMatrix4x4 mat) {
		return new Matrix4f(mat.a1(), mat.b1(), mat.c1(), mat.d1(),
							mat.a2(), mat.b2(), mat.c2(), mat.d2(),
							mat.a3(), mat.b3(), mat.c3(), mat.d3(),
							mat.a4(), mat.b4(), mat.c4(), mat.d4());
	}
	
}
