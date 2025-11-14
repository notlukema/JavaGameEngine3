
package jge3.src2d.object;

import jge3.math.Vector2f;

public class Object extends Point {
	
	protected float rotation;
	protected Vector2f scale;
	
	public Object() {
		super();
		rotation = 0;
		scale = new Vector2f(1f, 1f);
		resetRotation();
		resetScale();
	}
	
	public Object(Vector2f position) {
		super(position);
		rotation = 0;
		scale = new Vector2f(1f, 1f);
		resetRotation();
		resetScale();
	}
	
	public Object(Vector2f position, float rotation) {
		super(position);
		this.rotation = rotation;
		scale = new Vector2f(1f, 1f);
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
		rotation %= 360;
		resetRotation();
	}
	
	public float getRot() {
		return rotation;
	}
	
	public void setRot(float rot) {
		rotation = rot;
		resetRotation();
	}
	
	public void changeRot(float d) {
		rotation += d;
		resetRotation();
	}
	
	public float getScaleX() {
		return scale.x;
	}
	
	public float getScaleY() {
		return scale.y;
	}
	
	public Vector2f getScale() {
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
	
	public void setScale(float x, float y) {
		scale.x = x;
		scale.y = y;
		resetScale();
	}
	
	public void setScale(Vector2f scale) {
		this.scale.x = scale.x;
		this.scale.y = scale.y;
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
	
	public void changeScale(float dx, float dy) {
		scale.x += dx;
		scale.y += dy;
		resetScale();
	}
	
	public void changeScale(Vector2f d) {
		scale.x += d.x;
		scale.y += d.y;
		resetScale();
	}
	
}
