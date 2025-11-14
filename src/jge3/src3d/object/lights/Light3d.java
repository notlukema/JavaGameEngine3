
package jge3.src3d.object.lights;

import static org.lwjgl.opengl.GL20.*;

import jge3.math.Vector3f;
import jge3.math.Vector3i;
import jge3.math.Vector4f;
import jge3.math.Vector4i;
import jge3.utils.ColorUtils;

public abstract class Light3d {
	
	public static void reset() {
		AmbientLight.reset();
		DirectionalLight.reset();
		PointLight.reset();
		SpotLight.reset();
	}
	
	public static final String AMBIENT_COUNT = "ambientLightCount"
							,DIRECTIONAL_COUNT = "directionalLightCount"
							,POINT_COUNT = "pointLightCount"
							,SPOT_COUNT = "spotLightCount";
	
	private static int ambientCountUniformID;
	private static int directionalCountUniformID;
	private static int pointCountUniformID;
	private static int spotCountUniformID;
	
	public static void bindLightCount(int programID) {
		ambientCountUniformID = glGetUniformLocation(programID, AMBIENT_COUNT);
		directionalCountUniformID = glGetUniformLocation(programID, DIRECTIONAL_COUNT);
		pointCountUniformID = glGetUniformLocation(programID, POINT_COUNT);
		spotCountUniformID = glGetUniformLocation(programID, SPOT_COUNT);
		
		glUniform1i(ambientCountUniformID, AmbientLight.getCount());
		glUniform1i(directionalCountUniformID, DirectionalLight.getCount());
		glUniform1i(pointCountUniformID, PointLight.getCount());
		glUniform1i(spotCountUniformID, SpotLight.getCount());
	}
	
	//

	protected float intensity;
	protected float r, g, b, a;
	protected int intensityUniformID;
	protected int colorUniformID;
	
	public Light3d() {
		r = 1f;
		g = 1f;
		b = 1f;
		a = 1f;
		intensity = 1f;
	}
	
	public abstract void bindLight(int programID);
	
	public float getIntensity() {
		return intensity;
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	public Vector4f getColor() {
		return new Vector4f(r, g, b, a);
	}
	
	public void setColor(int color) {
		setColor(ColorUtils.getRed(color), ColorUtils.getGreen(color), ColorUtils.getBlue(color), ColorUtils.getAlpha(color));
	}
	
	public void setColor(Vector3i color) {
		setColor(color.x, color.y, color.z, 255);
	}
	
	public void setColor(Vector4i color) {
		setColor(color.x, color.y, color.z, color.w);
	}
	
	public void setColor(Vector3f color) {
		setColor(color.x, color.y, color.z, 1f);
	}
	
	public void setColor(Vector4f color) {
		setColor(color.x, color.y, color.z, color.w);
	}
	
	public void setColor(int r, int g, int b) {
		setColor(r, g, b, 255);
	}
	
	public void setColor(int r, int g, int b, int a) {
		this.r = r/255f;
		this.g = g/255f;
		this.b = b/255f;
		this.a = a/255f;
	}
	
	public void setColor(float r, float g, float b) {
		setColor(r, g, b, 1f);
	}
	
	public void setColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
}
