
package jge3.src3d.object.material;

import static org.lwjgl.opengl.GL20.*;

import jge3.common.image.Texture;
import jge3.utils.ImageUtils;

public class Material {
	
	public static final String STRUCTURE = "material";
	
	public static final String DIFFUSE = STRUCTURE+".diffuseTxt"
							,NORMAL = STRUCTURE+".normalTxt"
							,AMBIENT = STRUCTURE+".ambientTxt"
							,SPECULAR = STRUCTURE+".specularTxt"
							,EMISSION = STRUCTURE+".emission"
							,SPEC_POW = STRUCTURE+".specular";
	
	public static final float ONE_MAX = 1f;
	
	//
	
	private Texture diffuseMap;
	private Texture normalMap;
	private Texture ambientMap;
	private Texture specularMap;
	private float emission;
	private float specularPower;
	
	private int emissionUniformID;
	private int specularPowerUniformID;
	
	public Material() {
		diffuseMap = new Texture(0f, 0f, 0f);
		normalMap = new Texture(ImageUtils.normalToImage(0, 0, 1));
		ambientMap = new Texture(1f);
		specularMap = new Texture(0.8f);
		emission = 0f;
		specularPower = 32f*4f;
	}
	
	public Material(Texture diffuse) {
		diffuseMap = diffuse;
		normalMap = new Texture(ImageUtils.normalToImage(0, 0, 1));
		ambientMap = new Texture(1f);
		specularMap = new Texture(0.8f);
		emission = 0f;
		specularPower = 32f*4f;
	}
	
	public void bindMaterial(int programID) {
		emissionUniformID = glGetUniformLocation(programID, EMISSION);
		specularPowerUniformID = glGetUniformLocation(programID, SPEC_POW);
		glUniform1f(emissionUniformID, emission);
		glUniform1f(specularPowerUniformID, specularPower);
	}
	
	public Texture getDiffuseMap() {
		return diffuseMap;
	}
	
	public Texture getNormalMap() {
		return normalMap;
	}
	
	public Texture getAmbientMap() {
		return ambientMap;
	}
	
	public Texture getSpecularMap() {
		return specularMap;
	}
	
	public float getEmission() {
		return emission;
	}
	
	public float getSpecularPower() {
		return specularPower;
	}
	
	public void setDiffuseMap(Texture diffuseMap) {
		this.diffuseMap = diffuseMap;
	}
	
	public void setNormalMap(Texture normalMap) {
		this.normalMap = normalMap;
	}
	
	public void setAmbientMap(Texture ambientMap) {
		this.ambientMap = ambientMap;
	}
	
	public void setSpecularMap(Texture specularMap) {
		this.specularMap = specularMap;
	}
	
	public void setEmission(float emission) {
		this.emission = emission;
	}
	
	public void setSpecularPower(float specularPower) {
		this.specularPower = specularPower;
	}
	
}
