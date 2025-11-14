
package jge3.src2d.object;

import jge3.math.Vector2f;
import jge3.src2d.object.transform.Transform2d;

public class Point {
	
	protected Vector2f position;
	protected Transform2d transform;
	
	public Point() {
		position = new Vector2f();
		transform = new Transform2d();
		resetTranslate();
	}
	
	public Point(Vector2f position) {
		this.position = position.clone();
		transform = new Transform2d();
		resetTranslate();
	}
	
	protected void resetTranslate() {
		transform.setTranslation(position.x, position.y);
	}
	
	public Transform2d getTransform() {
		return transform;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public Vector2f getPosition() {
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
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
		resetTranslate();
	}
	
	public void setPosition(Vector2f position) {
		this.position.x = position.x;
		this.position.y = position.y;
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
	
	public void changePosition(float dx, float dy) {
		position.x += dx;
		position.y += dy;
		resetTranslate();
	}
	
	public void changePosition(Vector2f d) {
		position.x += d.x;
		position.y += d.y;
		resetTranslate();
	}
	
}
