
package jge3.src2d.object.interactable;

import jge3.common.image.Image;
import jge3.common.image.Texture;
import jge3.common.image.text.Font;
import jge3.math.Vector2f;
import jge3.math.Vector3f;
import jge3.src2d.object.Object2d;
import jge3.src2d.object.Sprite;
import jge3.src2d.object.shapes.Rect;
import jge3.utils.ImageUtils;

public class RectButton extends Button {
	
	private Vector2f size;
	
	public RectButton() {
		super();
		size = new Vector2f(200, 100);
	}
	
	public RectButton(float width, float height, Vector3f color) {
		this(width, height, (int)color.x, (int)color.y, (int)color.z);
	}
	
	public RectButton(float width, float height, int r, int g, int b) {
		this();
		size = new Vector2f(width, height);
		
		normalTxt = new Texture(r, g, b);
		touchingTxt = new Texture((int)Math.min(255, r*1.1f), (int)Math.min(255, g*1.1f), (int)Math.min(255, b*1.1f));
		clickedTxt = new Texture((int)(r*0.8f), (int)(g*0.8f), (int)(b*0.8f));
	}
	
	public RectButton(String str, float size, Vector3f color) {
		this(str, Font.getDefault(), size, color);
	}
	
	public RectButton(String str, Font font, float size, Vector3f color) {
		this(str, font, size, (int)color.x, (int)color.y, (int)color.z);
	}
	
	public RectButton(String str, float size, int r, int g, int b) {
		this(str, Font.getDefault(), size, r, g, b);
	}
	
	public RectButton(String str, Font font, float size, int r, int g, int b) {
		int w = font.getStringWidth(str, (int)size);
		int h = font.getStringHeight(str, (int)size);
		Image image = ImageUtils.createImage(w+(int)size, h+(int)(size/2.5), r, g, b);
		font.drawString(str, image, (int)size/2, 0, (int)size, -1, -1, 255, 255, 255);
		
		this.size = new Vector2f(w, h);
		normalTxt = new Texture(image);
		touchingTxt = new Texture(brightness(image, 1.1f));
		clickedTxt = new Texture(brightness(image, 0.8f));
	}
	
	public RectButton(Texture texture) {
		this(texture.getImage());
	}
	
	public RectButton(Image image) {
		this();
		size = new Vector2f(image.getWidth(), image.getHeight());
		
		normalTxt = new Texture(image);
		touchingTxt = new Texture(brightness(image, 1.1f));
		clickedTxt = new Texture(brightness(image, 0.8f));
	}
	
	public RectButton(Texture normal, Texture touching, Texture clicked) {
		this();
		size = new Vector2f(normal.getImage().getWidth(), normal.getImage().getHeight());
		
		normalTxt = normal;
		touchingTxt = touching;
		clickedTxt = clicked;
	}
	
	public RectButton(Image normal, Image touching, Image clicked) {
		this();
		size = new Vector2f(normal.getWidth(), normal.getHeight());
		
		normalTxt = new Texture(normal);
		touchingTxt = new Texture(touching);
		clickedTxt = new Texture(clicked);
	}
	
	public int getWidth() {
		return (int)size.x;
	}
	
	public int getHeight() {
		return (int)size.y;
	}
	
	public Vector2f getSize() {
		return size.clone();
	}
	
	public void setWidth(int width) {
		size.x = width;
	}
	
	public void setHeight(int height) {
		size.y = height;
	}
	
	public void setSize(int width, int height) {
		size = new Vector2f(width, height);
	}
	
	public void setSize(Vector2f size) {
		this.size = size.clone();
	}
	
	@Override
	public Object2d create() {
		normal = new Sprite(normalTxt);
		normal.addObject(new Rect(size.x, size.y));
		touching = new Sprite(touchingTxt);
		touching.addObject(new Rect(size.x, size.y));
		clicked = new Sprite(clickedTxt);
		clicked.addObject(new Rect(size.x, size.y));
		
		return super.create();
	}
	
}
