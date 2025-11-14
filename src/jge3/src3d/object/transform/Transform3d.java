
package jge3.src3d.object.transform;

import static jge3.utils.MatrixUtils.toRadians;

import jge3.math.Matrix4f;
import jge3.math.Vector3f;

public class Transform3d {
	
	private Matrix4f translationMatrix;
	private Matrix4f rotationMatrix;
	private Matrix4f scaleMatrix;
	
	private Vector3f translate;
	private Vector3f rotate;
	private Vector3f scale;
	
	public Transform3d() {
		setTranslation(0f, 0f, 0f);
		setRotation(0f, 0f, 0f);
		setScale(1f, 1f, 1f);
	}
	
	public Transform3d(Vector3f translation, Vector3f rotation, Vector3f scale) {
		setTranslation(translation);
		setRotation(rotation);
		setScale(scale);
	}
	
	public void setTranslation(Vector3f translate) {
		setTranslation(translate.x, translate.y, translate.z);
	}
	
	public void setTranslation(float x, float y, float z) {
		translate = new Vector3f(x, y, z);
		translationMatrix = new Matrix4f().translate(x, y, z);
	}
	
	public void setRotation(Vector3f rotation) {
		setRotation(rotation.x, rotation.y, rotation.z);
	}
	
	public void setRotation(float x, float y, float z) {
		rotate = new Vector3f(x, y, z);
		rotationMatrix = new Matrix4f().identity()
				.rotateZ(z*toRadians)
				.rotateX(x*toRadians)
				.rotateY(y*toRadians);
	}
	
	public void setScale(Vector3f scale) {
		setScale(scale.x, scale.y, scale.z);
	}
	
	public void setScale(float x, float y, float z) {
		scale = new Vector3f(x, y, z);
		scaleMatrix = new Matrix4f().scale(x, y, z);
	}
	
	public Matrix4f getFinalMatrix() {
		return new Matrix4f().identity()
				.rotateZ(rotate.z*toRadians)
				.rotateX(rotate.x*toRadians)
				.rotateY(rotate.y*toRadians)
				.translate(translate.x, translate.y, translate.z)
				.scale(scale.x, scale.y, scale.z);
	}
	
	public Matrix4f getObjectMatrixRotatedAround(Vector3f origin) {
		return getObjectMatrixRotatedAround(origin.x, origin.y, origin.z);
	}
	
	public Matrix4f getObjectMatrixRotatedAround(float x, float y, float z) {
		return new Matrix4f().identity()
				.translate(translate.x+x, translate.y+y, translate.z+z)
				.rotateZ(rotate.z*toRadians)
				.rotateX(rotate.x*toRadians)
				.rotateY(rotate.y*toRadians)
				.translate(-x, -y, -z)
				.scale(scale.x, scale.y, scale.z);
	}
	
	public Matrix4f getObjectMatrix() {
		return new Matrix4f().identity()
				.translate(translate.x, translate.y, translate.z)
				.rotateZ(rotate.z*toRadians)
				.rotateX(rotate.x*toRadians)
				.rotateY(rotate.y*toRadians)
				.scale(scale.x, scale.y, scale.z);
	}
	
	public Matrix4f getTranslationMatrix() {
		return translationMatrix;
	}
	
	public Matrix4f getRotationMatrix() {
		return rotationMatrix;
	}
	
	public Matrix4f getScaleMatrix() {
		return scaleMatrix;
	}
	
	public Vector3f getTranslation() {
		return translate;
	}
	
	public Vector3f getRotation() {
		return rotate;
	}
	
	public Vector3f getScale() {
		return scale;
	}
	
}
