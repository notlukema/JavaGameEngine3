
package jge3.src3d.object.animation;

import jge3.math.Quaternionf;
import jge3.math.Vector2f;
import jge3.math.Vector3f;

public class Channel {
	
	private Node node;
	
	private Vector3f[] positions;
	private Quaternionf[] rotations;
	private Vector3f[] scalings;
	
	private float[] posTimeStamps;
	private float[] rotTimeStamps;
	private float[] scaleTimeStamps;
	
	public Channel(Node node) {
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}
	
	public void setNode(Node node) {
		this.node = node;
	}
	
	public void bindData(float tick) {
		if (node == null) {
			return;
		}
		
		Vector2f data;
		int i;
		if (posTimeStamps.length > 1) {
			data = getData(tick, posTimeStamps);
			i = (int)data.x;
			node.setTransform(new Vector3f(positions[i]).scale(positions[i+1], data.y));
		} else if (posTimeStamps.length == 1) {
			node.setTransform(positions[0].clone());
		}
		if (rotTimeStamps.length > 1) {
			data = getData(tick, rotTimeStamps);
			i = (int)data.x;
			node.setRotation(new Quaternionf(rotations[i]).slerp(rotations[i+1], data.y));
		} else if (rotTimeStamps.length == 1) {
			node.setRotation(new Quaternionf(rotations[0]));
		}
		if (scaleTimeStamps.length > 1) {
			data = getData(tick, scaleTimeStamps);
			i = (int)data.x;
			node.setScale(new Vector3f(scalings[i]).scale(scalings[i+1], data.y));
		} else if (scaleTimeStamps.length == 1) {
			node.setScale(scalings[0].clone());
		}
	}
	
	private Vector2f getData(float tick, float[] timeStamps) {
		int i = 0;
		if (tick > timeStamps[timeStamps.length-2]) {
			i = timeStamps.length - 2;
		} else {
			if (tick < 0) {
				tick = 0;
			}
			while (timeStamps[i] < tick) {
				i++;
			}
		}
		return new Vector2f(i, (tick-timeStamps[i])/(timeStamps[i+1]-timeStamps[i]));
	}
	
	public Vector3f[] getPositions() {
		return positions;
	}
	
	public void setPositions(Vector3f[] positions) {
		this.positions = positions;
	}
	
	public Quaternionf[] getRotations() {
		return rotations;
	}
	
	public void setRotations(Quaternionf[] rotations) {
		this.rotations = rotations;
	}
	
	public Vector3f[] getScalings() {
		return scalings;
	}
	
	public void setScalings(Vector3f[] scalings) {
		this.scalings = scalings;
	}
	
	public float[] getPositionTimeStamps() {
		return posTimeStamps;
	}
	
	public void setPositionTimeStamps(float[] timeStamps) {
		posTimeStamps = timeStamps;
	}
	
	public float[] getRotationTimeStamps() {
		return rotTimeStamps;
	}
	
	public void setRotationTimeStamps(float[] timeStamps) {
		rotTimeStamps = timeStamps;
	}
	
	public float[] getScalingTimeStamps() {
		return scaleTimeStamps;
	}
	
	public void setScalingTimeStamps(float[] timeStamps) {
		scaleTimeStamps = timeStamps;
	}
	
}
