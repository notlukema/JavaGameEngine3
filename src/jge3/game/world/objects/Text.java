
package jge3.game.world.objects;

import jge3.math.Vector3f;
import jge3.src3d.object.Object3d;
import jge3.src3d.object.custom.BillboardObject;

public class Text extends Object {
	
	private Vector3f position;
	private float size;
	private String text;
	
	public Text(float x, float y, float z, String text, float size) {
		position = new Vector3f(x, y, z);
		this.text = text;
		this.size = size;
		hidden = false;
		
		resetText();
	}
	
	private void resetText() {
		Object3d obj = new BillboardObject();
		objects = new Object3d[] {obj};
		/*Image image = PlatformerGame.gameFont.createTextImage(text, 255, 255, 255, (int)size, 0, true);
		Material material = new Material();
		material.setDiffuseMap(new Texture(image));
		Mesh mesh = new Mesh(material);
		obj.addMesh(mesh);*/
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		resetText();
		objects[0].create();
	}
	
	@Override
	public void update(float delta) {
		objects[0].setPosition(position);
	}
	
	@Override
	public Vector3f getPosition() {
		return position;
	}
	
	public float getSize() {
		return size;
	}
	
	public void setSize(float size) {
		this.size = size;
	}
	
}
