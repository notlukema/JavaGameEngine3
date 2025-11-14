
package jge3.src3d.object;

import jge3.math.Vector3f;
import jge3.src3d.object.transform.Transform3d;

public class Point {
	
	protected Vector3f position;
	protected Transform3d transform;
	
	public Point() {
		position = new Vector3f();
		transform = new Transform3d();
		resetTranslate();
	}
	
	public Point(Vector3f position) {
		this.position = position.clone();
		transform = new Transform3d();
		resetTranslate();
	}
	
	protected void resetTranslate() {
		transform.setTranslation(position.x, position.y, -position.z);
	}
	
	public Transform3d getTransform() {
		return transform;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getZ() {
		return position.z;
	}
	
	public Vector3f getPosition() {
		return position.clone();
	}
	
	public void setX(float x) {
		position.x = x;
		resetTranslate();
	}
	
	public void setY(float y) {
		position.y = y;
		resetTranslate();
	}
	
	public void setZ(float z) {
		position.z = z;
		resetTranslate();
	}
	
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
		resetTranslate();
	}
	
	public void setPosition(Vector3f position) {
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
		resetTranslate();
	}
	
	public void changeX(float dx) {
		position.x += dx;
		resetTranslate();
	}
	
	public void changeY(float dy) {
		position.y += dy;
		resetTranslate();
	}
	
	public void changeZ(float dz) {
		position.z += dz;
		resetTranslate();
	}
	
	public void changePosition(float dx, float dy, float dz) {
		position.x += dx;
		position.y += dy;
		position.z += dz;
		resetTranslate();
	}
	
	public void changePosition(Vector3f d) {
		position.x += d.x;
		position.y += d.y;
		position.z += d.z;
		resetTranslate();
	}
	
}
