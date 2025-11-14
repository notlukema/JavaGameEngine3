
package jge3.common.image;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jge3.math.Vector3f;
import jge3.math.Vector3i;
import jge3.math.Vector4f;
import jge3.math.Vector4i;
import jge3.utils.ImageUtils;

public class Texture {
	
    private Map<Long, Integer> textureIDs;
    private int textureUniformID;
    private String name;
    private Image image;

    public Texture(Image image) {
    	textureIDs = new HashMap<Long, Integer>();
    	this.image = image;
    }
    
    public Texture(Vector3i color) {
    	this(color.x, color.y, color.z, 255);
    }
    
    public Texture(Vector4i color) {
    	this(color.x, color.y, color.z, color.w);
    }
    
    public Texture(Vector3f color) {
    	this(color.x, color.y, color.z, 1f);
    }
    
    public Texture(Vector4f color) {
    	this(color.x, color.y, color.z, color.w);
    }
    
    public Texture(int r, int g, int b) {
    	this(r, g, b, 255);
    }
    
    public Texture(int r, int g, int b, int a) {
    	textureIDs = new HashMap<Long, Integer>();
    	image = ImageUtils.createImage(1, 1, r, g, b, a);
    }
    
    public Texture(float r, float g, float b) {
    	this(r, g, b, 1f);
    }
    
    public Texture(float r, float g, float b, float a) {
    	this((int)(r*255), (int)(g*255), (int)(b*255), (int)(a*255));
    }
    
    public Texture(int c) {
    	this(c, c, c, 255);
    }
    
    public Texture(float c) {
    	this(c, c, c, 1f);
    }
    
    public Image getImage() {
    	return image;
    }
    
    public void setImage(Image image) {
    	this.image = image;
    }
    
    public int getUniformID() {
    	return textureUniformID;
    }
    
    public String getName() {
    	return name;
    }

    public void bindTexture(int programID, String name, int value, long contextID) {
    	generateIfNot(contextID);
    	this.name = name;
		textureUniformID = glGetUniformLocation(programID, name);
		glUniform1i(textureUniformID, value);
		glActiveTexture(GL_TEXTURE0+value);
		glBindTexture(GL_TEXTURE_2D, textureIDs.getOrDefault(contextID, 0));
    }
    
    public void generateTexture(long contextID) {
    	if (textureIDs.containsKey(contextID)) {
    		glDeleteTextures(textureIDs.remove(contextID));
    	}
    	generateTexture(image.getWidth(), image.getHeight(), image.getBuffer(), contextID);
    }
    
    public void generateIfNot(long contextID) {
    	if (!textureIDs.containsKey(contextID)) {
    		generateTexture(image.getWidth(), image.getHeight(), image.getBuffer(), contextID);
    	}
    }
    
    private void generateTexture(int width, int height, ByteBuffer buffer, long contextID) {
    	int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        textureIDs.put(contextID, textureID);
    }
    
    public void destroy() {
    	for (Entry<Long, Integer> entry : textureIDs.entrySet()) {
    		glDeleteTextures(entry.getValue());
    	}
    	textureIDs.clear();
    }
}
