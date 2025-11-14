
package jge3.src2d.object.interactable;

import jge3.common.image.Image;
import jge3.common.image.Texture;
import jge3.math.Vector4i;
import jge3.src2d.object.Object2d;
import jge3.src2d.object.Sprite;
import jge3.utils.ImageUtils;

public abstract class Button extends Object2d {
	
	protected Sprite normal;
	protected Sprite touching;
	protected Sprite clicked;
	
	protected Texture normalTxt;
	protected Texture touchingTxt;
	protected Texture clickedTxt;
	
	protected boolean mouseDown;
	protected boolean onMouseDown;
	protected boolean onMouseUp;
	
	public Button() {
		super();
		
		normal = null;
		touching = null;
		clicked = null;
		
		normalTxt = null;
		touchingTxt = null;
		clickedTxt = null;
		
		mouseDown = false;
		onMouseDown = false;
		onMouseUp = false;
	}
	
	protected Image brightness(Image image, float mul) {
		Image newImage = ImageUtils.cloneImage(image);
		ImageUtils.iterateImage(newImage, new ImageUtils.ImageIterator() {
			@Override
			public Vector4i pixel(int r, int g, int b, int a, int x, int y) {
				return new Vector4i((int)Math.min(255, r*mul), (int)Math.min(255, g*mul), (int)Math.min(255, b*mul), a);
			}
		});
		return newImage;
	}
	
	public Texture getNormalTexture() {
		return normalTxt;
	}
	
	public Texture getTouchingTexture() {
		return touchingTxt;
	}
	
	public Texture getClickedTexture() {
		return clickedTxt;
	}
	
	public void setNormalTexture(Texture texture) {
		normalTxt = texture;
	}
	
	public void setNormalTexture(Image image) {
		normalTxt = new Texture(image);
	}
	
	public void setTouchingTexture(Texture texture) {
		touchingTxt = texture;
	}
	
	public void setTouchingTexture(Image image) {
		touchingTxt = new Texture(image);
	}
	
	public void setClickedTexture(Texture texture) {
		clickedTxt = texture;
	}
	
	public void setClickedTexture(Image image) {
		clickedTxt = new Texture(image);
	}
	
	public boolean mouseDown() {
		return mouseDown;
	}
	
	public boolean onMouseDown() {
		if (onMouseDown) {
			onMouseDown = false;
			return true;
		}
		return false;
	}
	
	public boolean onMouseUp() {
		if (onMouseUp) {
			onMouseUp = false;
			return true;
		}
		return false;
	}
	
	@Override
	public void mouseEnter() {
		normal.setHidden(true);
		touching.setHidden(false);
		clicked.setHidden(true);
	}

	@Override
	public void mouseExit() {
		normal.setHidden(false);
		touching.setHidden(true);
		clicked.setHidden(true);
		mouseDown = false;
		onMouseDown = false;
		onMouseUp = false;
	}
	
	@Override
	public void mouseDown(int button) {
		normal.setHidden(true);
		touching.setHidden(true);
		clicked.setHidden(false);
		mouseDown = true;
		onMouseDown = true;
	}
	
	@Override
	public void mouseUp(int button) {
		normal.setHidden(true);
		touching.setHidden(false);
		clicked.setHidden(true);
		onMouseUp = mouseDown;
		mouseDown = false;
	}
	
	@Override
	public Object2d create() {
		addSprite(normal);
		addSprite(touching);
		addSprite(clicked);
		
		normal.setHidden(false);
		touching.setHidden(true);
		clicked.setHidden(true);
		
		return super.create();
	}
	
}
