
package jge3.src3d.object.shapes;

import java.util.ArrayList;

public interface Shape {
	
	public void resetVertices();
	public void addData(ArrayList<Float> positions, ArrayList<Float> normals, ArrayList<Float> uvs, ArrayList<Integer> boneIDs,  ArrayList<Float> weights);
	public void addIndices(ArrayList<Integer> indices);
	
	public void updateVertexData();
	
	public float touching(float x, float y, float z, float dirx, float diry, float dirz, float lowestZ);
	
}
