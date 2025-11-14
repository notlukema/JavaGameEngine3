
package jge3.src2d.object.shapes;

import jge3.src2d.object.Vertex2d;

public class Poly extends AbstractShape {
	
	public Poly(Vertex2d[] vertices) {
		if (vertices.length < 3) {
			error("Polygon", vertices.length);
		}
		this.vertices = vertices;
	}
	
	@Override
	public void addIndices() {
		for (int i=2;i<vertices.length;i++) {
			indice(vertices[0]);
			indice(vertices[i-1]);
			indice(vertices[i]);
		}
	}
	
}
