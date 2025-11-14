
package jge3.engine.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import jge3.common.object.World;

public abstract class Renderer extends Renderable {
	
	protected List<Program> programs;
	protected Program boundProgram;
	protected int boundProgramID;
	protected String boundProgramName;
	protected Program.Filter filter;
	protected boolean isGlobal;
	
	protected boolean bindMaterial;
	
	@Override
	public void freeUsing() {
		terminated = true;
		while (programs.size() > 0) {
			programs.remove(0).freeUsing();
		}
	}
	
	public Program createProgram(String name) {
		return createProgram(name, true);
	}
	
	public Program createProgram(String name, boolean global) {
		Program program = new Program(this, name, global);
		programs.add(program);
		return program;
	}
	
	public boolean destroyProgram(Program program) {
		if (programs.remove(program)) {
			program.freeUsing();
			return true;
		} else {
			return false;
		}
	}
	
	protected void setSettings(Program.Settings s) {
		bindMaterial = s.USE_MATERIAL;
		
		glPolygonMode(GL_FRONT_AND_BACK, s.getDrawMode());
		glPolygonOffset(s.getOffsetFactor(), s.getOffsetUnits());
		glDepthFunc(GL_LESS);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		//glEnable(GL_TEXTURE_2D);
		
		if (s.DEPTH_TEST) {
			glEnable(GL_DEPTH_TEST);
		} else {
			glDisable(GL_DEPTH_TEST);
		}
		if (s.CULL_FACE) {
			glEnable(GL_CULL_FACE);
		} else {
			glDisable(GL_CULL_FACE);
		}
		if (s.BLEND) {
			glEnable(GL_BLEND);
		} else {
			glDisable(GL_BLEND);
		}
		
		if (s.getSettingsCallback() != null) {
			s.getSettingsCallback().settings();
		}
	}
	
	public void setFilter(Program.Filter filter) {
		int length = programs.size();
		for (int i=0;i<length;i++) {
			programs.get(i).setFilter(filter);
		}
	}
	
	public Program[] getPrograms() {
		Program[] programs = new Program[this.programs.size()];
		for (int i=0;i<programs.length;i++) {
			programs[i] = this.programs.get(i);
		}
		return programs;
	}
	
	public Program getProgram(int i) {
		return programs.get(i);
	}
	
	public abstract World getWorld();
	
}
