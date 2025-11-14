
package jge3.src3d.object;

import jge3.math.Vector3f;

public class Object extends Point {
	
	protected Vector3f rotation;
	protected Vector3f scale;
	
	public Object() {
		super();
		rotation = new Vector3f();
		scale = new Vector3f(1f, 1f, 1f);
		resetRotation();
		resetScale();
	}
	
	public Object(Vector3f position) {
		super(position);
		rotation = new Vector3f();
		scale = new Vector3f(1f, 1f, 1f);
		resetRotation();
		resetScale();
	}
	
	public Object(Vector3f position, Vector3f rotation) {
		super(position);
		this.rotation = rotation.clone();
		scale = new Vector3f(1f, 1f, 1f);
		resetRotation();
		resetScale();
	}
	
	protected void resetRotation() {
		transform.setRotation(rotation);
	}
	
	protected void resetScale() {
		transform.setScale(scale);
	}
	
	public void restrictRotation() {
		rotation.x %= 360;
		rotation.y %= 360;
		rotation.z %= 360;
		resetRotation();
	}
	
	public float getRotX() {
		return rotation.x;
	}
	
	public float getRotY() {
		return rotation.y;
	}
	
	public float getRotZ() {
		return rotation.z;
	}
	
	public Vector3f getRotation() {
		return rotation.clone();
	}
	
	public void setRotX(float x) {
		rotation.x = x;
		resetRotation();
	}
	
	public void setRotY(float y) {
		rotation.y = y;
		resetRotation();
	}
	
	public void setRotZ(float z) {
		rotation.z = z;
		resetRotation();
	}
	
	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
		resetRotation();
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation.x = rotation.x;
		this.rotation.y = rotation.y;
		this.rotation.z = rotation.z;
		resetRotation();
	}
	
	public void changeRotX(float dx) {
		rotation.x += dx;
		resetRotation();
	}
	
	public void changeRotY(float dy) {
		rotation.y += dy;
		resetRotation();
	}
	
	public void changeRotZ(float dz) {
		rotation.z += dz;
		resetRotation();
	}
	
	public void changeRotation(float dx, float dy, float dz) {
		rotation.x += dx;
		rotation.y += dy;
		rotation.z += dz;
		resetRotation();
	}
	
	public void changeRotation(Vector3f d) {
		rotation.x += d.x;
		rotation.y += d.y;
		rotation.z += d.z;
		resetRotation();
	}
	
	public float getScaleX() {
		return scale.x;
	}
	
	public float getScaleY() {
		return scale.y;
	}
	
	public float getScaleZ() {
		return scale.z;
	}
	
	public Vector3f getScale() {
		return scale.clone();
	}
	
	public void setScaleX(float x) {
		scale.x = x;
		resetScale();
	}
	
	public void setScaleY(float y) {
		scale.y = y;
		resetScale();
	}
	
	public void setScaleZ(float z) {
		scale.z = z;
		resetScale();
	}
	
	public void setScale(float x, float y, float z) {
		scale.x = x;
		scale.y = y;
		scale.z = z;
		resetScale();
	}
	
	public void setScale(Vector3f scale) {
		this.scale.x = scale.x;
		this.scale.y = scale.y;
		this.scale.z = scale.z;
		resetScale();
	}
	
	public void changeScaleX(float dx) {
		scale.x += dx;
		resetScale();
	}
	
	public void changeScaleY(float dy) {
		scale.y += dy;
		resetScale();
	}
	
	public void changeScaleZ(float dz) {
		scale.z += dz;
		resetScale();
	}
	
	public void changeScale(float dx, float dy, float dz) {
		scale.x += dx;
		scale.y += dy;
		scale.z += dz;
		resetScale();
	}
	
	public void changeScale(Vector3f d) {
		scale.x += d.x;
		scale.y += d.y;
		scale.z += d.z;
		resetScale();
	}
	
}
