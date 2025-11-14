
package jge3.src2d.object.interactable;

import jge3.common.image.Image;
import jge3.common.image.Texture;
import jge3.math.Vector3f;
import jge3.src2d.object.Object2d;
import jge3.src2d.object.Sprite;
import jge3.src2d.object.shapes.Circle;

public class RoundButton extends Button {
	
	private float radius;
	
	public RoundButton() {
		super();
		radius = 50f;
	}
	
	public RoundButton(float radius, Vector3f color) {
		this(radius, (int)color.x, (int)color.y, (int)color.z);
	}
	
	public RoundButton(float radius, int r, int g, int b) {
		this();
		this.radius = radius;
		
		normalTxt = new Texture(r, g, b);
		touchingTxt = new Texture((int)Math.min(255, r*1.1f), (int)Math.min(255, g*1.1f), (int)Math.min(255, b*1.1f));
		clickedTxt = new Texture((int)(r*0.8f), (int)(g*0.8f), (int)(b*0.8f));
	}
	
	public RoundButton(Texture texture) {
		this(texture.getImage());
	}
	
	public RoundButton(Image image) {
		this();
		radius = Math.min(image.getWidth()/2f, image.getHeight()/2f);
		
		normalTxt = new Texture(image);
		touchingTxt = new Texture(brightness(image, 1.1f));
		clickedTxt = new Texture(brightness(image, 0.8f));
	}
	
	public RoundButton(Texture normal, Texture touching, Texture clicked) {
		this();
		radius = Math.min(normal.getImage().getWidth()/2f, normal.getImage().getHeight()/2f);
		
		normalTxt = normal;
		touchingTxt = touching;
		clickedTxt = clicked;
	}
	
	public RoundButton(Image normal, Image touching, Image clicked) {
		this();
		radius = Math.min(normal.getWidth(), normal.getHeight());
		
		normalTxt = new Texture(normal);
		touchingTxt = new Texture(touching);
		clickedTxt = new Texture(clicked);
	}
	
	public float getRadius() {
		return radius;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	@Override
	public Object2d create() {
		normal = new Sprite(normalTxt);
		normal.addObject(new Circle(radius));
		touching = new Sprite(touchingTxt);
		touching.addObject(new Circle(radius));
		clicked = new Sprite(clickedTxt);
		clicked.addObject(new Circle(radius));
		
		return super.create();
	}
	
}
