
package jge3.src3d.object.shapes;

import jge3.src3d.object.Vertex3d;

public class Triangle extends AbstractShape {
	
	public Triangle(Vertex3d[] vertices) {
		if (vertices.length != 3) {
			error("Triangle", vertices.length);
		}
		this.vertices = vertices;
	}

	@Override
	public void addIndices() {
		indice(vertices[0]);
		indice(vertices[1]);
		indice(vertices[2]);
	}
	
}
