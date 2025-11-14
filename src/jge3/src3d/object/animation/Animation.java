
package jge3.src3d.object.animation;

import jge3.math.Matrix4f;

public class Animation {
	
	private String name;
	private Node rootNode;
	
	private float tick;
	private float duration;
	private float ticksPerSecond;
	private Channel[] channels;
	
	public Animation(String name, Node rootNode) {
		this.name = name;
		this.rootNode = rootNode;
		
		tick = 0f;
		duration = 0f;
		ticksPerSecond = 0f;
		channels = new Channel[0];
	}
	
	public void play(float delta) {
		tick += delta*ticksPerSecond;
		if (tick > duration) {
			tick = 0f;
		}
		
		for (int i=0;i<channels.length;i++) {
			channels[i].bindData(tick);
		}
		
		rootNode.createBoneMatrix(new Matrix4f());
	}
	
	public float getTick() {
		return tick;
	}
	
	public void setTick(float tick) {
		this.tick = tick;
	}
	
	public String getName() {
		return name;
	}
	
	public Node getRootNode() {
		return rootNode;
	}
	
	public float getDuration() {
		return duration;
	}
	
	public void setDuration(float duration) {
		this.duration = duration;
	}
	
	public float getTicksPerSecond() {
		return ticksPerSecond;
	}
	
	public void setTicksPerSecond(float ticksPerSecond) {
		this.ticksPerSecond = ticksPerSecond;
	}
	
	public Channel[] getChannels() {
		return channels;
	}
	
	public void setChannels(Channel[] channels) {
		this.channels = channels;
	}
	
}
