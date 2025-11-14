
package jge3.src2d.object.shapes;

import jge3.src2d.object.Vertex2d;

public class Rect extends AbstractShape {
	
	public Rect(float width, float height) {
		this(0f, 0f, width, height);
	}
	
	public Rect(float x, float y, float width, float height) {
		width /= 2;
		height /= 2;
		vertices = new Vertex2d[] {
				new Vertex2d(x-width, y-height, 0f, 0f),
				new Vertex2d(x+width, y-height, 1f, 0f),
				new Vertex2d(x+width, y+height, 1f, 1f),
				new Vertex2d(x-width, y+height, 0f, 1f)
		};
	}
	
	@Override
	void addIndices() {
		indice(vertices[0]);
		indice(vertices[1]);
		indice(vertices[2]);

		indice(vertices[0]);
		indice(vertices[2]);
		indice(vertices[3]);
	}
	
}
