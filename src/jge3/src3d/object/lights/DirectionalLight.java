
package jge3.src3d.object.lights;

import static org.lwjgl.opengl.GL20.*;

import jge3.math.Vector3f;
import jge3.math.Vector4f;

public class DirectionalLight extends Light3d {
	
	public static final int MAX_DIRECTIONAL_LIGHTS = 10;
	
	public static final String STRUCTURE = "directionalLights";
	
	public static final String INTENSITY = "intensity"
							,COLOR = "color"
							,DIRECTION = "dir";
	
	public static void reset() {
		i = 0;
	}
	
	public static int getCount() {
		return i;
	}
	
	private static int i;
	
	//
	
	private String end;
	
	protected Vector3f direction;
	
	protected int directionUniformID;
	
	public DirectionalLight() {
		super();
		direction = new Vector3f(0, -1, 0);
	}
	
	public DirectionalLight(float dx, float dy, float dz) {
		super();
		direction = new Vector3f(dx, dy, dz).safeNormalize();
	}
	
	public DirectionalLight(Vector3f direction) {
		super();
		this.direction = direction.clone().safeNormalize();
	}
	
	public DirectionalLight(Vector3f direction, float intensity) {
		super();
		this.direction = direction.clone().safeNormalize();
		this.intensity = intensity;
	}
	
	public DirectionalLight(Vector4f color, float intensity) {
		this();
		r = color.x;
		g = color.y;
		b = color.z;
		a = color.w;
		this.intensity = intensity;
	}
	
	public DirectionalLight(float r, float g, float b, float a, float intensity) {
		this();
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.intensity = intensity;
	}
	
	public Vector3f getDirection() {
		return direction;
	}
	
	public void setDirection(Vector3f direction) {
		this.direction = direction.clone().safeNormalize();
	}
	
	public void setDirection(float dx, float dy, float dz) {
		direction = new Vector3f(dx, dy, dz).safeNormalize();
	}
	
	private String uniform(String name) {
		return String.join("", STRUCTURE, end, ".", name);
	}
	
	@Override
	public void bindLight(int programID) {
		if (i >= MAX_DIRECTIONAL_LIGHTS) {
			return;
		}
		end = "["+i+"]";
		intensityUniformID = glGetUniformLocation(programID, uniform(INTENSITY));
		colorUniformID = glGetUniformLocation(programID, uniform(COLOR));
		directionUniformID = glGetUniformLocation(programID, uniform(DIRECTION));
		i++;
		
	    glUniform1f(intensityUniformID, intensity);
	    glUniform4f(colorUniformID, r, g, b, a);
		glUniform3f(directionUniformID, -direction.x, -direction.y, direction.z);
	}
	
}
