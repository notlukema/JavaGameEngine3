
package jge3.src3d.object.lights;

import static org.lwjgl.opengl.GL20.*;

import jge3.math.Vector4f;

public class AmbientLight extends Light3d {
	
	public static final int MAX_AMBIENT_LIGHTS = 50;
	
	public static final String STRUCTURE = "ambientLights";
	
	public static final String INTENSITY = "intensity"
							,COLOR = "color";
	
	public static void reset() {
		i = 0;
	}
	
	public static int getCount() {
		return i;
	}
	
	private static int i;
	
	//
	
	private String end;
	
	public AmbientLight() {
		super();
	}
	
	public AmbientLight(Vector4f color, float intensity) {
		super();
		r = color.x;
		g = color.y;
		b = color.z;
		a = color.w;
		this.intensity = intensity;
	}
	
	public AmbientLight(float r, float g, float b, float a, float intensity) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.intensity = intensity;
	}
	
	private String uniform(String name) {
		return String.join("", STRUCTURE, end, ".", name);
	}
	
	@Override
	public void bindLight(int programID) {
		if (i >= MAX_AMBIENT_LIGHTS) {
			return;
		}
		end = "["+i+"]";
		intensityUniformID = glGetUniformLocation(programID, uniform(INTENSITY));
		colorUniformID = glGetUniformLocation(programID, uniform(COLOR));
		i++;

	    glUniform1f(intensityUniformID, intensity);
	    glUniform4f(colorUniformID, r, g, b, a);
	}
	
}
