
package jge3.src3d.object.shapes;

import jge3.src3d.object.Vertex3d;

public class Cube extends AbstractShape {
	
	public Cube(float dx, float dy, float dz) {
		vertices = new Vertex3d[8];
		vertices[0] = new Vertex3d(dx, dy, dz);
		vertices[1] = new Vertex3d(-dx, dy, dz);
		vertices[2] = new Vertex3d(-dx, -dy, dz);
		vertices[3] = new Vertex3d(dx, -dy, dz);
		vertices[4] = new Vertex3d(dx, dy, -dz);
		vertices[5] = new Vertex3d(-dx, dy, -dz);
		vertices[6] = new Vertex3d(-dx, -dy, -dz);
		vertices[7] = new Vertex3d(dx, -dy, -dz);
	}
	
	@Override
	public void addIndices() {
		indice(vertices[0]);
		indice(vertices[1]);
		indice(vertices[2]);
		indice(vertices[0]);
		indice(vertices[2]);
		indice(vertices[3]);
		
		indice(vertices[5]);
		indice(vertices[4]);
		indice(vertices[6]);
		indice(vertices[6]);
		indice(vertices[4]);
		indice(vertices[7]);
		
		indice(vertices[3]);
		indice(vertices[2]);
		indice(vertices[6]);
		indice(vertices[3]);
		indice(vertices[6]);
		indice(vertices[7]);
		
		indice(vertices[1]);
		indice(vertices[0]);
		indice(vertices[4]);
		indice(vertices[1]);
		indice(vertices[4]);
		indice(vertices[5]);
		
		indice(vertices[2]);
		indice(vertices[1]);
		indice(vertices[5]);
		indice(vertices[2]);
		indice(vertices[5]);
		indice(vertices[6]);
		
		indice(vertices[0]);
		indice(vertices[3]);
		indice(vertices[4]);
		indice(vertices[4]);
		indice(vertices[3]);
		indice(vertices[7]);
	}
	
}
