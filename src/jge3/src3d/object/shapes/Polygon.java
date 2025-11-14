
package jge3.src3d.object.shapes;

import jge3.src3d.object.Vertex3d;

public class Polygon extends AbstractShape {
	
	public Polygon(Vertex3d[] vertices) {
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
