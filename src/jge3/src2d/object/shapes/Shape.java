
package jge3.src2d.object.shapes;

import java.util.ArrayList;

public interface Shape {
	
	public void resetVertices();
	public void addData(ArrayList<Float> positions, ArrayList<Float> uvs);
	public void addIndices(ArrayList<Integer> indices);
	
	public void updateVertexData();
	
	public boolean touching(float x, float y);
	
}
