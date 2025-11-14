
package jge3.src2d.object;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.system.MemoryStack;

import jge3.common.image.Image;
import jge3.common.image.Texture;
import jge3.common.interactable.Interactable;
import jge3.math.Matrix4f;
import jge3.math.Vector2f;
import jge3.math.Vector3f;
import jge3.src2d.object.shapes.Rect;
import jge3.src2d.object.shapes.Shape;
import jge3.src3d.object.audio.Source3d;

public class Object2d extends Object implements Interactable {
	
	public static final String OBJECT_MATRIX = "objectMatrix";
	
	public static final int USE_ONLY = 1
						,DONT_USE = 2;
	
	public static final int MAX_LAYERS = 500;
	
	//
	
	protected int[] vaos;
	protected List<Sprite> sprites;
	protected List<Source3d> sources;
	
	protected int layer;
	
	protected int objectUniformID;
	protected Matrix4f finalMatrix;
	
	protected boolean hidden;
	
	public Object2d() {
		super();
		resetLists();
		resetOther();
	}
	
	public Object2d(Sprite sprite) {
		this();
		addSprite(sprite);
	}
	
	public Object2d(Image image) {
		this(new Texture(image));
	}
	
	public Object2d(Texture texture) {
		this(new Rect(texture.getImage().getWidth(), texture.getImage().getHeight()), texture);
	}
	
	public Object2d(Shape shape, Texture texture) {
		this();
		Sprite sprite = new Sprite(texture);
		sprite.addObject(shape);
		addSprite(sprite);
	}
	
	public Object2d(Vector2f position) {
		super(position);
		resetLists();
		resetOther();
	}
	
	public Object2d(Vector2f position, float rotation) {
		super(position, rotation);
		resetLists();
		resetOther();
	}
	
	public Object2d(float x, float y) {
		position = new Vector2f(x, y);
		rotation = 0;
		resetTranslate();
		resetRotation();
		resetLists();
		resetOther();
	}
	
	public Object2d(float x, float y, float rot) {
		position = new Vector2f(x, y);
		rotation = rot;
		resetTranslate();
		resetRotation();
		resetLists();
		resetOther();
	}
	
	private void resetLists() {
		vaos = new int[0];
		sprites = new ArrayList<Sprite>();
		sources = new ArrayList<Source3d>();
	}
	
	private void resetOther() {
		layer = 0;
		
		hidden = false;
	}
	
	public boolean addSprite(Sprite sprite) {
		return sprites.add(sprite);
	}
	
	public boolean removeSprite(Sprite sprite) {
		return sprites.remove(sprite);
	}
	
	public boolean addSource(Source3d source) {
		return sources.add(source);
	}
	
	public boolean removeSource(Source3d source) {
		return sources.remove(source);
	}
	
	public void delete() {
		for (int i=0;i<vaos.length;i++) {
			glDeleteVertexArrays(vaos[i]);
		}
		vaos = new int[0];
		
		while (sources.size() > 0) {
			sources.remove(0).delete();
		}
	}
	
	public Object2d create() {
		long glContextID = glfwGetCurrentContext();
		long alContextID = alcGetCurrentContext();
		
		vaos = new int[sprites.size()];
		for (int i=0;i<vaos.length;i++) {
			vaos[i] = sprites.get(i).create(glContextID, layer);
		}
		
		int length = sources.size();
		for (int i=0;i<length;i++) {
			sources.get(i).create(alContextID, new Vector3f(position.x, position.y, 0));
		}
		
		return this;
	}
	
	public void updateVertexData() {
		for (int i=0;i<vaos.length;i++) {
			sprites.get(i).updateVertexData();
		}
	}
	
	public void updateSourceData(long contextID) {
		int length = sources.size();
		for (int i=0;i<length;i++) {
			sources.get(i).updateData(new Vector3f(position.x, position.y, 0), contextID);
		}
	}
	
	public void calculateObjectMatrix() {
		finalMatrix = transform.getObjectMatrix();
	}
	
	public Matrix4f getObjectMatrix() {
		return finalMatrix;
	}
	
	public void bindObjectMatrix(int programID) {
		objectUniformID = glGetUniformLocation(programID, OBJECT_MATRIX);
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        finalMatrix.get(fb);
	        glUniformMatrix4fv(objectUniformID, false, fb);
		}
	}
	
	public boolean touching(float x, float y) {
		for (int i=0;i<getSpriteCount();i++) {
			if (getSprite(i).isHidden()) {
				continue;
			}
			
			Shape[] shapes = getSprite(i).getObjects();
			for (int j=0;j<shapes.length;j++) {
				if (shapes[j].touching(x, y)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void mouseEnter() {}
	
	@Override
	public void mouseExit() {}
	
	@Override
	public void mouseDown(int button) {}
	
	@Override
	public void mouseUp(int button) {}
	
	public int[] getVAOs() {
		return vaos;
	}
	
	public int getVAO(int i) {
		return vaos[i];
	}
	
	public int getLayer() {
		return layer;
	}
	
	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public Sprite[] getSprites() {
		Sprite[] sprites = new Sprite[this.sprites.size()];
		for (int i=0;i<sprites.length;i++) {
			sprites[i] = this.sprites.get(i);
		}
		return sprites;
	}
	
	public int getSpriteCount() {
		return vaos.length;
	}
	
	public Sprite getSprite(int i) {
		return sprites.get(i);
	}
	
	public Source3d[] getSources() {
		Source3d[] sources = new Source3d[this.sources.size()];
		for (int i=0;i<sources.length;i++) {
			sources[i] = this.sources.get(i);
		}
		return sources;
	}
	
	public int getSourceCount() {
		return sources.size();
	}
	
	public Source3d getSource(int i) {
		return sources.get(i);
	}
	
}
