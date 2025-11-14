
package jge3.src3d.object.lights;

import jge3.math.Vector3f;

public abstract class PositionalLight extends Light3d {
	
	protected Vector3f position;
	
	protected float constantAtt;
	protected float linearAtt;
	protected float quadraticAtt;
	
	protected int positionUniformID;
	protected int constantUniformID;
	protected int linearUniformID;
	protected int quadraticUniformID;
	
	public PositionalLight() {
		super();
		
		position = new Vector3f();
		
		constantAtt = 0.005f;
		linearAtt = 0.0006f;
		quadraticAtt = 0.000003f;
	}
	
	public float getConstAtt() {
		return constantAtt;
	}
	
	public float getLinAtt() {
		return linearAtt;
	}
	
	public float getQuadAtt() {
		return quadraticAtt;
	}
	
	public void setConstAtt(float constantAttenuation) {
		constantAtt = constantAttenuation;
	}
	
	public void setLinAtt(float linearAttenuation) {
		linearAtt = linearAttenuation;
	}
	
	public void setQuadAtt(float quadraticAttenuation) {
		quadraticAtt = quadraticAttenuation;
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
	}
	
	public void setY(float y) {
		position.y = y;
	}
	
	public void setZ(float z) {
		position.z = z;
	}
	
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public void setPosition(Vector3f position) {
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
	}
	
	public void changeX(float dx) {
		position.x += dx;
	}
	
	public void changeY(float dy) {
		position.y += dy;
	}
	
	public void changeZ(float dz) {
		position.z += dz;
	}
	
	public void changePosition(float dx, float dy, float dz) {
		position.x += dx;
		position.y += dy;
		position.z += dz;
	}
	
	public void changePosition(Vector3f d) {
		position.x += d.x;
		position.y += d.y;
		position.z += d.z;
	}
	
}
