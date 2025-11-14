
package jge3.src3d.object.lights;

import static org.lwjgl.opengl.GL20.*;

import jge3.math.Vector3f;

public class SpotLight extends PositionalLight {
	
	public static final int MAX_SPOT_LIGHTS = 150;
	
	public static final String STRUCTURE = "spotLights";
	
	public static final String INTENSITY = "intensity"
							,COLOR = "color"
							,POSITION = "pos"
							,CONSTANT_ATT = "constAtt"
							,LINEAR_ATT = "linAtt"
							,QUADRATIC_ATT = "quadAtt"
							,DIRECTION = "dir"
							,CONE_MIN = "coneMin"
							,CONE_MAX = "coneMax"
							,CONE_DAMP = "coneDamp";
	
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
	protected float coneAngleMin;
	protected float coneAngleMax;
	protected float coneDampening;
	
	protected int directionUniformID;
	protected int coneMinUniformID;
	protected int coneMaxUniformID;
	protected int coneDampeningUniformID;
	
	public SpotLight() {
		super();
		direction = new Vector3f(0, -1, 0);
		coneAngleMin = 0f;
		coneAngleMax = 45f;
		coneDampening = 1f;
	}
	
	public SpotLight(float x, float y, float z) {
		super();
		position = new Vector3f(x, y, z);
		direction = new Vector3f(1, 0, 0);
		coneAngleMin = 0f;
		coneAngleMax = 45f;
		coneDampening = 1f;
	}
	
	public SpotLight(Vector3f position, Vector3f direction) {
		super();
		this.position = position.clone();
		this.direction = direction.clone().safeNormalize();
		coneAngleMin = 0f;
		coneAngleMax = 45f;
		coneDampening = 1f;
	}
	
	public SpotLight(Vector3f position, Vector3f direction, float intensity) {
		super();
		this.position = position.clone();
		this.direction = direction.clone().safeNormalize();
		this.intensity = intensity;
		coneAngleMin = 0f;
		coneAngleMax = 45f;
		coneDampening = 1f;
	}
	
	public SpotLight(Vector3f position, Vector3f direction, float intensity, float ca, float la, float qa) {
		super();
		this.position = position.clone();
		this.direction = direction.clone().safeNormalize();
		this.intensity = intensity;
		constantAtt = ca;
		linearAtt = la;
		quadraticAtt = qa;
		coneAngleMin = 0f;
		coneAngleMax = 45f;
		coneDampening = 1f;
	}
	
	public SpotLight(Vector3f position, Vector3f direction, float intensity, float ca, float la, float qa, float coneAngle) {
		super();
		this.position = position.clone();
		this.direction = direction.clone().safeNormalize();
		this.intensity = intensity;
		constantAtt = ca;
		linearAtt = la;
		quadraticAtt = qa;
		coneAngleMin = 0f;
		coneAngleMax = coneAngle;
		coneDampening = 1f;
	}
	
	public SpotLight(Vector3f position, Vector3f direction, float intensity, float coneAngle) {
		super();
		this.position = position.clone();
		this.direction = direction.clone().safeNormalize();
		this.intensity = intensity;
		coneAngleMin = 0f;
		coneAngleMax = coneAngle;
		coneDampening = 1f;
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
	
	public float getConeAngleMin() {
		return coneAngleMin;
	}
	
	public float getConeAngleMax() {
		return coneAngleMax;
	}
	
	public float getConeAngle() {
		return coneAngleMax;
	}
	
	public float getConeDampening() {
		return coneDampening;
	}
	
	public void setConeAngleMin(float min) {
		coneAngleMin = min;
	}
	
	public void setConeAngleMax(float max) {
		coneAngleMax = max;
	}
	
	public void setConeAngle(float angle) {
		coneAngleMin = 0;
		coneAngleMax = angle;
	}
	
	public void setConeDampening(float dampening) {
		coneDampening = dampening;
	}
	
	private String uniform(String name) {
		return String.join("", STRUCTURE, end, ".", name);
	}
	
	@Override
	public void bindLight(int programID) {
		if (i >= MAX_SPOT_LIGHTS) {
			return;
		}
		end = "["+i+"]";
		intensityUniformID = glGetUniformLocation(programID, uniform(INTENSITY));
		colorUniformID = glGetUniformLocation(programID, uniform(COLOR));
		positionUniformID = glGetUniformLocation(programID, uniform(POSITION));
		constantUniformID = glGetUniformLocation(programID, uniform(CONSTANT_ATT));
		linearUniformID = glGetUniformLocation(programID, uniform(LINEAR_ATT));
		quadraticUniformID = glGetUniformLocation(programID, uniform(QUADRATIC_ATT));
		directionUniformID = glGetUniformLocation(programID, uniform(DIRECTION));
		coneMinUniformID = glGetUniformLocation(programID, uniform(CONE_MIN));
		coneMaxUniformID = glGetUniformLocation(programID, uniform(CONE_MAX));
		coneDampeningUniformID = glGetUniformLocation(programID, uniform(CONE_DAMP));
		i++;
		
	    glUniform1f(intensityUniformID, intensity);
	    glUniform4f(colorUniformID, r, g, b, a);
	    glUniform3f(positionUniformID, position.x, position.y, -position.z);
		glUniform1f(constantUniformID, constantAtt);
		glUniform1f(linearUniformID, linearAtt);
		glUniform1f(quadraticUniformID, quadraticAtt);
		glUniform3f(directionUniformID, -direction.x, -direction.y, direction.z);
	    glUniform1f(coneMinUniformID, coneAngleMin/2);
	    glUniform1f(coneMaxUniformID, coneAngleMax/2);
	    glUniform1f(coneDampeningUniformID, coneDampening);
	}
	
}
