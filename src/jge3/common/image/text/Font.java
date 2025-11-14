
package jge3.common.image.text;

import java.util.HashMap;

import jge3.common.image.Image;
import jge3.math.Vector3f;
import jge3.math.Vector3i;
import jge3.math.Vector4f;
import jge3.math.Vector4i;
import jge3.utils.FontUtils;
import jge3.utils.ImageUtils;

import resources.fonts.FontResources;

public class Font {
	
	private static final Font defaultFont = FontUtils.loadTrueType(FontResources.getResource("times_new_roman.ttf"));
	
	public static Font getDefault() {
		return defaultFont;
	}
	
	//
	
	private String name;
	private HashMap<Character, Symbol> characterMap;
	
	private int pixelScale;
	
	private float ascent;
	private float descent;
	private float lineGap;
	
	public Font(String name, Symbol[] glyphs, int pixelScale, float ascent, float descent, float lineGap) {
		this.name = name;
		characterMap = new HashMap<Character, Symbol>();
		for (int i=0;i<glyphs.length;i++) {
			characterMap.put(glyphs[i].getCharacter(), glyphs[i]);
		}
		
		this.pixelScale = pixelScale;
		
		this.ascent = ascent;
		this.descent = descent;
		this.lineGap = lineGap;
	}
	
	public int getStringWidth(String string, int size) {
		return getStringWidth(string, (float)size/pixelScale);
	}
	
	public int getStringWidth(String string, float scale) {
		float width = 0;
		String[] strings = string.split("\n");
		for (int i=0;i<strings.length;i++) {
			float w = 0;
			int length = strings[i].length();
			for (int j=0;j<length;j++) {
				Symbol symbol = getCharacter(strings[i].charAt(j));
				if (symbol != null) {
					if (j == length-1) {
						w += symbol.getImage().getWidth()*scale;
					} else {
						w += symbol.getAdvance()*scale;
					}
				}
			}
			width = Math.max(width, w);
		}
		return (int)Math.ceil(width);
	}
	
	public int getStringHeight(String string, int size) {
		return getStringHeight(string, (float)size/pixelScale);
	}
	
	public int getStringHeight(String string, float scale) {
		float height = 0;
		int length = string.length();
		for (int i=0;i<length;i++) {
			if (string.charAt(i) == '\n') {
				height += (ascent-descent+lineGap)*scale;
			}
		}
		return (int)Math.ceil(height+(ascent-descent+lineGap)*scale);
	}
	
	public void drawString(String string, Image image, int size) {
		drawString(string, image, image.getWidth()/2, image.getHeight()/2, size, 0, 0);
	}
	
	public void drawString(String string, Image image, int x, int y, int size) {
		drawString(string, image, x, y, size, -1, -1);
	}
	
	public void drawString(String string, Image image, int x, int y, int size, int alignX, int alignY) {
		drawString(string, image, x, y, size, alignX, alignY, 0, 0, 0, false);
	}
	
	public void drawString(String string, Image image, int x, int y, int size, int r, int g, int b) {
		drawString(string, image, x, y, size, -1, -1, r, g, b, false);
	}
	
	public void drawString(String string, Image image, int x, int y, int size, boolean alias) {
		drawString(string, image, x, y, size, -1, -1, 0, 0, 0, alias);
	}
	
	public void drawString(String string, Image image, int x, int y, int size, int r, int g, int b, boolean alias) {
		drawString(string, image, x, y, size, -1, -1, r, g, b, alias);
	}
	
	public void drawString(String string, Image image, int x, int y, int size, int alignX, int alignY, int r, int g, int b) {
		drawString(string, image, x, y, size, alignX, alignY, r, g, b, false);
	}
	
	public void drawString(String string, Image image, int x, int y, int size, int alignX, int alignY, boolean alias) {
		drawString(string, image, x, y, size, alignX, alignY, 0, 0, 0, alias);
	}
	
	public void drawString(String string, Image image, int x, int y, int size, int alignX, int alignY, int r, int g, int b, boolean alias) {
		float scale = (float)size/pixelScale;
		
		float cx;
		float cy;
		int h = getStringHeight(string, scale);
		if (alignY < 0) {
			cy = y;
		} else if (alignY > 0) {
			cy = y-h;
		} else {
			cy = y-h/2f;
		}
		cy += descent*scale;
		
		String[] strings = string.split("\n");
		for (int i=0;i<strings.length;i++) {
			String str = strings[i];
			
			int w = getStringWidth(str, scale);
			if (alignX < 0) {
				cx = x;
			} else if (alignX > 0) {
				cx = x-w;
			} else {
				cx = x-w/2f;
			}
			cy += (ascent-descent+lineGap)*scale;
			
			int length = str.length();
			for (int j=0;j<length;j++) {
				Symbol symbol = getCharacter(str.charAt(j));
				if (symbol != null) {
					symbol.drawCharacter(image, (int)(cx+symbol.getOffsetX()*scale), (int)(cy+symbol.getOffsetY()*scale), scale, r, g, b, alias);
					cx += symbol.getAdvance()*scale;
				}
			}
		}
	}
	
	public Image createTextImage(String string, Vector3i color, int size, int alignX, boolean alias) {
		return createTextImage(string, color.x, color.y, color.z, size, alignX, alias);
	}
	
	public Image createTextImage(String string, Vector4i color, int size, int alignX, boolean alias) {
		return createTextImage(string, color.x, color.y, color.z, size, alignX, alias);
	}
	
	public Image createTextImage(String string, Vector3f color, int size, int alignX, boolean alias) {
		return createTextImage(string, (int)(color.x*255), (int)(color.y*255), (int)(color.z*255), size, alignX, alias);
	}
	
	public Image createTextImage(String string, Vector4f color, int size, int alignX, boolean alias) {
		return createTextImage(string, (int)(color.x*255), (int)(color.y*255), (int)(color.z*255), size, alignX, alias);
	}
	
	public Image createTextImage(String string, int r, int g, int b, int size, int alignX, boolean alias) {
		int w = getStringWidth(string, size);
		int h = getStringHeight(string, size);
		float hw = w/2f;
		Image image = ImageUtils.createImage(w, h, 0, 0, 0, 0);
		drawString(string, image, (int)(hw+hw*alignX), 0, size, alignX, -1, r, g, b, alias);
		return image;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean containsCharacter(char character) {
		return characterMap.containsKey(character);
	}
	
	public Symbol getCharacter(char character) {
		return characterMap.get(character);
	}
	
	public int getPixelScale() {
		return pixelScale;
	}
	
	public float getAscent() {
		return ascent;
	}
	
	public float getDescent() {
		return descent;
	}
	
	public float getLineGap() {
		return lineGap;
	}
	
}
