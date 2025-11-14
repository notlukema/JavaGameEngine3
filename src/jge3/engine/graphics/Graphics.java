
package jge3.engine.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import jge3.common.image.text.Font;
import jge3.engine.display.Window;
import jge3.math.Vector3f;
import jge3.math.Vector3i;
import jge3.math.Vector4f;
import jge3.math.Vector4i;
import jge3.utils.ColorUtils;

public abstract class Graphics extends Renderable {
	
	public static final int ALIASED = GL_NEAREST
						,ANTI_ALIASED = GL_LINEAR;
	
	//
	
	protected Window window;
	
	//protected boolean interactions;
	//protected boolean touching;
	//protected Cursor.GlobalCursor cursor;
	protected FloatBuffer floatBuffer;
	
	protected Vector4f color;
	protected Font font;
	protected int imageDrawMode;
	
	protected void init() {
		//interactions = false;
		//touching = false;
		//cursor = Cursor.get();
		floatBuffer = BufferUtils.createFloatBuffer(16);
		updateCamera();
		
		color = new Vector4f(0f, 0f, 0f, 1f);
		resetColor();
		font = Font.getDefault();
		imageDrawMode = ALIASED;
	}
	/*
	public boolean canInteract() {
		return interactions;
	}
	
	public void setInteractions(boolean interactions) {
		this.interactions = interactions;
	}
	
	public void resetTouching() {
		touching = false;
	}
	
	public boolean isTouching() {
		return touching;
	}
	*/
	public abstract void updateCamera();
	
	public Vector4f getColor() {
		return color.clone();
	}
	
	public void setColor(int color) {
		this.color = new Vector4f(ColorUtils.getRed(color)/255f, ColorUtils.getGreen(color)/255f, ColorUtils.getBlue(color)/255f, ColorUtils.getAlpha(color)/255f);
		resetColor();
	}
	
	public void setColor(Vector3i color) {
		setColor(color.x, color.y, color.z);
	}
	
	public void setColor(Vector4i color) {
		setColor(color.x, color.y, color.z, color.w);
	}
	
	public void setColor(Vector3f color) {
		setColor(color.x, color.y, color.z);
	}
	
	public void setColor(Vector4f color) {
		setColor(color.x, color.y, color.z, color.w);
	}
	
	public void setColor(int r, int g, int b) {
		color = new Vector4f(r/255f, g/255f, b/255f, 1f);
		resetColor();
	}
	
	public void setColor(int r, int g, int b, int a) {
		color = new Vector4f(r/255f, g/255f, b/255f, a/255f);
		resetColor();
	}
	
	public void setColor(float r, float g, float b) {
		color = new Vector4f(r, g, b, 1f);
		resetColor();
	}
	
	public void setColor(float r, float g, float b, float a) {
		color = new Vector4f(r, g, b, a);
		resetColor();
	}
	
	protected void resetColor() {
		glColor4f(color.x, color.y, color.z, color.w);
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public int getImageDrawMode() {
		return imageDrawMode;
	}
	
	public void setImageDrawMode(int mode) {
		imageDrawMode = mode;
	}
	
	public abstract Window getWindow();
	
}
