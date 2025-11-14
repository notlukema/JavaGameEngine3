
package jge3.src2d.object.transform;

import static jge3.utils.MatrixUtils.toRadians;

import jge3.math.Matrix4f;
import jge3.math.Vector2f;

public class Transform2d {
	
	private Matrix4f translationMatrix;
	private Matrix4f rotationMatrix;
	private Matrix4f scaleMatrix;
	
	private Vector2f translate;
	private float rotate;
	private Vector2f scale;
	
	public Transform2d() {
		setTranslation(0f, 0f);
		setRotation(0f);
		setScale(1f, 1f);
	}
	
	public Transform2d(Vector2f translation, float rotation, Vector2f scale) {
		setTranslation(translation);
		setRotation(rotation);
		setScale(scale);
	}
	
	public void setTranslation(Vector2f translate) {
		setTranslation(translate.x, translate.y);
	}
	
	public void setTranslation(float x, float y) {
		translate = new Vector2f(x, y);
		translationMatrix = new Matrix4f().translate(x, y, 0);
	}
	
	public void setRotation(float rotation) {
		rotate = rotation;
		rotationMatrix = new Matrix4f().identity()
				.rotateZ(rotation*toRadians);
	}
	
	public void setScale(Vector2f scale) {
		setScale(scale.x, scale.y);
	}
	
	public void setScale(float x, float y) {
		scale = new Vector2f(x, y);
		scaleMatrix = new Matrix4f().scale(x, y, 1f);
	}
	
	public Matrix4f getFinalMatrix() {
		return new Matrix4f().identity()
				.rotateZ(rotate*toRadians)
				.translate(translate.x, translate.y, 0)
				.scale(scale.x, scale.y, 1f);
	}
	
	public Matrix4f getObjectMatrixRotatedAround(Vector2f origin) {
		return getObjectMatrixRotatedAround(origin.x, origin.y);
	}
	
	public Matrix4f getObjectMatrixRotatedAround(float x, float y) {
		return new Matrix4f().identity()
				.translate(translate.x+x, translate.y+y, 0)
				.rotateZ(rotate*toRadians)
				.translate(-x, -y, 0)
				.scale(scale.x, scale.y, 1f);
	}
	
	public Matrix4f getObjectMatrix() {
		return new Matrix4f().identity()
				.translate(translate.x, translate.y, 0)
				.rotateZ(rotate*toRadians)
				.scale(scale.x, scale.y, 1f);
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
	
	public Vector2f getTranslation() {
		return translate;
	}
	
	public float getRotation() {
		return rotate;
	}
	
	public Vector2f getScale() {
		return scale;
	}
	
}
