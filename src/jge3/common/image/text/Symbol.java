
package jge3.common.image.text;

import jge3.common.image.Image;
import jge3.math.Vector4i;

public class Symbol {
	
	private char character;
	private Image image;
	
	private float offsetX;
	private float offsetY;
	private float advance;
	
	public Symbol(char character, Image image, float offsetX, float offsetY, float advance) {
		this.character = character;
		this.image = image;
		
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.advance = advance;
	}
	
	public void drawCharacter(Image image, int x, int y, float scale, int r, int g, int b, boolean alias) {
		int w = (int)(this.image.getWidth()*scale);
		int h = (int)(this.image.getHeight()*scale);
		
		for (int dx=0;dx<w;dx++) {
			for (int dy=0;dy<h;dy++) {
				int rx = x+dx;
				int ry = y-h+dy;
				if (rx < 0 || rx >= image.getWidth() || ry < 0 || ry >= image.getHeight()) {
					continue;
				}
				float px = (int)((float)dx/w*this.image.getWidth());
				float py = (int)((float)dy/h*this.image.getHeight());
				Vector4i color = this.image.getPixelRGBAInt((int)px, (int)py);
				if (color.w <= 0) {
					continue;
				}
				if (alias) {
					if (color.w < 128) {
						continue;
					} else {
						color.w = 255;
					}
				}
				float br = (color.x+color.y+color.z)/3f/255f;
				image.drawPixel(rx, ry, new Vector4i((int)(r*br), (int)(g*br), (int)(b*br), color.w));
			}
		}
	}
	
	public char getCharacter() {
		return character;
	}
	
	public Image getImage() {
		return image;
	}
	
	public float getOffsetX() {
		return offsetX;
	}
	
	public float getOffsetY() {
		return offsetY;
	}
	
	public float getAdvance() {
		return advance;
	}
	
}
