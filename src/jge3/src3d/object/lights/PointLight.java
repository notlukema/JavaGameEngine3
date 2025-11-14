
package jge3.src3d.object.lights;

import static org.lwjgl.opengl.GL20.*;

import jge3.math.Vector3f;

public class PointLight extends PositionalLight {
	
	public static final int MAX_POINT_LIGHTS = 300;
	
	public static final String STRUCTURE = "pointLights";
	
	public static final String INTENSITY = "intensity"
							,COLOR = "color"
							,POSITION = "pos"
							,CONSTANT_ATT = "constAtt"
							,LINEAR_ATT = "linAtt"
							,QUADRATIC_ATT = "quadAtt";
	
	public static void reset() {
		i = 0;
	}
	
	public static int getCount() {
		return i;
	}
	
	private static int i;
	
	//
	
	private String end;
	
	public PointLight() {
		super();
	}
	
	public PointLight(float x, float y, float z) {
		super();
		position = new Vector3f(x, y, z);
	}
	
	public PointLight(Vector3f position) {
		super();
		this.position = position.clone();
	}
	
	public PointLight(Vector3f position, float intensity) {
		super();
		this.position = position.clone();
		this.intensity = intensity;
	}
	
	public PointLight(Vector3f position, float intensity, float ca, float la, float qa) {
		super();
		this.position = position.clone();
		this.intensity = intensity;
		constantAtt = ca;
		linearAtt = la;
		quadraticAtt = qa;
	}
	
	private String uniform(String name) {
		return String.join("", STRUCTURE, end, ".", name);
	}
	
	@Override
	public void bindLight(int programID) {
		if (i >= MAX_POINT_LIGHTS) {
			return;
		}
		end = "["+i+"]";
		intensityUniformID = glGetUniformLocation(programID, uniform(INTENSITY));
		colorUniformID = glGetUniformLocation(programID, uniform(COLOR));
		positionUniformID = glGetUniformLocation(programID, uniform(POSITION));
		constantUniformID = glGetUniformLocation(programID, uniform(CONSTANT_ATT));
		linearUniformID = glGetUniformLocation(programID, uniform(LINEAR_ATT));
		quadraticUniformID = glGetUniformLocation(programID, uniform(QUADRATIC_ATT));
		i++;
		
	    glUniform1f(intensityUniformID, intensity);
	    glUniform4f(colorUniformID, r, g, b, a);
	    glUniform3f(positionUniformID, position.x, position.y, -position.z);
		glUniform1f(constantUniformID, constantAtt);
		glUniform1f(linearUniformID, linearAtt);
		glUniform1f(quadraticUniformID, quadraticAtt);
	}
	
}
