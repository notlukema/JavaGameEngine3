
package jge3.src2d.object.interactable;

import jge3.common.image.Image;
import jge3.common.image.Texture;
import jge3.math.Vector3f;
import jge3.src2d.object.Object2d;
import jge3.src2d.object.Sprite;
import jge3.src2d.object.Vertex2d;
import jge3.src2d.object.shapes.Poly;

public class PolyButton extends Button {
	
	private Vertex2d[] vertices;
	
	public PolyButton() {
		super();
		vertices = new Vertex2d[0];
	}
	
	public PolyButton(Vertex2d[] vertices, Vector3f color) {
		this(vertices, (int)color.x, (int)color.y, (int)color.z);
	}
	
	public PolyButton(Vertex2d[] vertices, int r, int g, int b) {
		this();
		this.vertices = vertices;
		
		normalTxt = new Texture(r, g, b);
		touchingTxt = new Texture((int)Math.min(255, r*1.1f), (int)Math.min(255, g*1.1f), (int)Math.min(255, b*1.1f));
		clickedTxt = new Texture((int)(r*0.8f), (int)(g*0.8f), (int)(b*0.8f));
	}
	
	public PolyButton(Texture texture) {
		this(texture.getImage());
	}
	
	public PolyButton(Image image) {
		this();
		vertices(image.getWidth(), image.getHeight());
		
		normalTxt = new Texture(image);
		touchingTxt = new Texture(brightness(image, 1.1f));
		clickedTxt = new Texture(brightness(image, 0.8f));
	}
	
	private void vertices(int width, int height) {
		vertices = new Vertex2d[] {
				new Vertex2d(0, 0, 0, 0),
				new Vertex2d(width, 0, 1, 0),
				new Vertex2d(width, height, 1, 1),
				new Vertex2d(0, height, 0, 1)
		};
	}
	
	public PolyButton(Texture normal, Texture touching, Texture clicked) {
		this();
		vertices(normal.getImage().getWidth(), normal.getImage().getHeight());
		
		normalTxt = normal;
		touchingTxt = touching;
		clickedTxt = clicked;
	}
	
	public PolyButton(Image normal, Image touching, Image clicked) {
		this();
		vertices(normal.getWidth(), normal.getHeight());
		
		normalTxt = new Texture(normal);
		touchingTxt = new Texture(touching);
		clickedTxt = new Texture(clicked);
	}
	
	public Vertex2d[] getVertices() {
		return vertices;
	}
	
	public void setVertices(Vertex2d[] vertices) {
		this.vertices = vertices;
	}
	
	@Override
	public Object2d create() {
		normal = new Sprite(normalTxt);
		normal.addObject(new Poly(vertices));
		touching = new Sprite(touchingTxt);
		touching.addObject(new Poly(vertices));
		clicked = new Sprite(clickedTxt);
		clicked.addObject(new Poly(vertices));
		
		return super.create();
	}
	
}
