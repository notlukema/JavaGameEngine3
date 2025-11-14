
package jge3.src2d.object.shapes;

import static jge3.utils.MatrixUtils.toRadians;

import jge3.src2d.object.Vertex2d;

public class Circle extends AbstractShape {
	
	private float x;
	private float y;
	private float r;
	
	public Circle(float radius) {
		this(0, 0, radius);
	}
	
	public Circle(float x, float y, float radius) {
		this.x = x;
		this.y = y;
		r = radius;
		
		float c = r*2*(float)Math.PI;
		vertices = new Vertex2d[(int)Math.ceil(c/10)];
		
		float angle = 0;
		float increment = 360f/vertices.length;
		for (int i=0;i<vertices.length;i++) {
			float sin = (float)Math.sin(angle*toRadians);
			float cos = (float)Math.cos(angle*toRadians);
			vertices[i] = new Vertex2d(sin*r, cos*r, (sin+1)/2, (cos+1)/2);
			angle += increment;
		}
	}
	
	@Override
	void addIndices() {
		for (int i=2;i<vertices.length;i++) {
			indice(vertices[0]);
			indice(vertices[i-1]);
			indice(vertices[i]);
		}
	}
	
	@Override
	public boolean touching(float x, float y) {
		float distance = (float)(Math.pow(this.x-x, 2) + Math.pow(this.y-y, 2));
		return distance <= r*r;
	}
	
}
