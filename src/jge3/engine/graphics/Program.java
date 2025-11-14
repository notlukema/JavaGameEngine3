
package jge3.engine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jge3.math.Vector2f;
import jge3.src2d.object.Object2d;
import jge3.src3d.object.Object3d;
import jge3.utils.FileUtils;

import resources.shaders.ShaderResources;

public class Program {
	
	public static final String PHONG_VERTEX_SHADER = "phong_fragment.vs";
	public static final String PHONG_GEOMETRY_SHADER = "phong_fragment.gs";
	public static final String PHONG_FRAGMENT_SHADER = "phong_fragment.fs";
	public static final String FULLBRIGHT_PHONG_FRAGMENT_SHADER = "phong_fullbright.fs";
	
	public static final String RENDER_2D_VERTEX_SHADER = "render_2d.vs";
	public static final String RENDER_2D_FRAGMENT_SHADER = "render_2d.fs";
	
	public static void PHONG_SHADERS(Program program) {
		program.attachVertexShader("Phong Vertex Shader", ShaderResources.getResource(PHONG_VERTEX_SHADER));
		program.attachGeometryShader("Phong Geometry Shader", ShaderResources.getResource(PHONG_GEOMETRY_SHADER));
		program.attachFragmentShader("Phong Fragment Shader", ShaderResources.getResource(PHONG_FRAGMENT_SHADER));
		program.linkProgram();
	}
	
	public static void RENDER_2D_SHADERS(Program program) {
		program.attachVertexShader("2D Vertex Shader", ShaderResources.getResource(RENDER_2D_VERTEX_SHADER));
		program.attachFragmentShader("2D Fragment Shader", ShaderResources.getResource(RENDER_2D_FRAGMENT_SHADER));
		program.linkProgram();
	}
	
	//
	
	private boolean terminated;
	private Renderer parent;
	
	private int programID;
	private String programName;
	private boolean global;
	private Settings settings;
	private Filter filter;
	
	private Map<String, Integer> shaders;
	
	public abstract class SettingsCallback {
		public abstract void settings();
	}
	
	public class Settings {
		
		public static int FILL = GL_FILL;
		public static int LINE = GL_LINE;
		
		private int drawMode;
		private float offsetFactor;
		private float offsetUnits;
		
		public boolean USE_MATERIAL;
		
		public boolean DEPTH_TEST
					,CULL_FACE
					,BLEND;
		
		private SettingsCallback callback;
		
		public Settings() {
			defaultSettings();
		}
		
		public void defaultSettings() {
			drawMode = FILL;
			offsetFactor = 0f;
			offsetUnits = 0f;

			USE_MATERIAL = true;
			
			DEPTH_TEST = true;
			CULL_FACE = false;
			BLEND = true;
			
			callback = null;
		}

		public int getDrawMode() {
			return drawMode;
		}
		
		public void setDrawMode(int drawMode) {
			this.drawMode = drawMode;
		}
		
		public float getOffsetFactor() {
			return offsetFactor;
		}
		
		public void setOffsetFactor(float offsetFactor) {
			this.offsetFactor = offsetFactor;
		}
		
		public float getOffsetUnits() {
			return offsetUnits;
		}
		
		public void setOffsetUnits(float offsetUnits) {
			this.offsetUnits = offsetUnits;
		}
		
		public Vector2f getDrawOffset() {
			return new Vector2f(offsetFactor, offsetUnits);
		}
		
		public void setDrawOffset(float factor, float units) {
			offsetFactor = factor;
			offsetUnits = units;
		}
		
		public SettingsCallback getSettingsCallback() {
			return callback;
		}
		
		public void setSettingsCallback(SettingsCallback callback) {
			this.callback = callback;
		}
		
	}
	
	public static abstract class Filter {
		public abstract boolean block(Object2d object);
		public abstract boolean block(Object3d object);
	}
	
	public Program(Renderer parent, String name, boolean global) {
		terminated = false;
		this.parent = parent;
		bindParentContext();
		
		programID = glCreateProgram();
		programName = name;
		this.global = global;
		shaders = new HashMap<String, Integer>();
		
		settings = new Settings();
		filter = null;
		
		linkProgram();
	}
	
	public String[] getShaderNames() {
		String[] names = new String[shaders.size()];
		int i = 0;
		for (Entry<String, Integer> entry : shaders.entrySet()) {
			names[i] = entry.getKey();
			i++;
		}
		return names;
	}
	
	public int[] getShaderIDs() {
		int[] ids = new int[shaders.size()];
		int i = 0;
		for (Entry<String, Integer> entry : shaders.entrySet()) {
			ids[i] = entry.getValue();
			i++;
		}
		return ids;
	}
	
	public void freeUsing() {
		terminated = true;
		int[] ids = getShaderIDs();
		for (int i=0;i<ids.length;i++) {
			glDetachShader(programID, ids[i]);
			glDeleteShader(ids[i]);
		}
		glDeleteProgram(programID);
	}
	
	public void attachVertexShader(String name, String path) {
		attachShader(name, FileUtils.loadFile(path), GL_VERTEX_SHADER);
	}
	
	public void attachGeometryShader(String name, String path) {
		attachShader(name, FileUtils.loadFile(path), GL_GEOMETRY_SHADER);
	}
	
	public void attachFragmentShader(String name, String path) {
		attachShader(name, FileUtils.loadFile(path), GL_FRAGMENT_SHADER);
	}
	
	public void attachVertexShader(String name, InputStream stream) {
		attachShader(name, FileUtils.loadResource(stream), GL_VERTEX_SHADER);
	}
	
	public void attachGeometryShader(String name, InputStream stream) {
		attachShader(name, FileUtils.loadResource(stream), GL_GEOMETRY_SHADER);
	}
	
	public void attachFragmentShader(String name, InputStream stream) {
		attachShader(name, FileUtils.loadResource(stream), GL_FRAGMENT_SHADER);
	}
	
	private void bindParentContext() {
		parent.getWorld().getParent().setGraphicsContext();
	}
	
	public void attachShader(String name, String shader, int type) {
		bindParentContext();
		
		int id = glCreateShader(type);
		
		glShaderSource(id, shader);
		glCompileShader(id);
		
		if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException("Couldn't compile shader \""+name+"\"\n"+
					glGetShaderInfoLog(id, glGetShaderi(id, GL_INFO_LOG_LENGTH)));
		}
		glAttachShader(programID, id);
		shaders.put(name, id);
		
		// remember to link your program!
	}
	
	public void removeShader(String name) {
		Integer i = shaders.get(name);
		int id = i;
		glDetachShader(programID, id);
		glDeleteShader(id);
		shaders.remove(name);
		
		linkProgram();
	}
	
	public Program linkProgram() {
		bindParentContext();
		glLinkProgram(programID);
		return this;
	}
	
	public void bindProgram() {
		glUseProgram(programID);
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public Filter getFilter() {
		return filter;
	}
	
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	public int getID() {
		return programID;
	}
	
	public String getName() {
		return programName;
	}
	
	public boolean isGlobal() {
		return global;
	}
	
	public void setGlobal(boolean global) {
		this.global = global;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	public boolean terminated() {
		return terminated;
	}
	
	public void destroy() {
		parent.destroyProgram(this);
	}
	
}
