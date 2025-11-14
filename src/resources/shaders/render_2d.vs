
#version 330 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 uv;

uniform mat4 projectionMatrix;
uniform mat4 cameraMatrix;
uniform mat4 objectMatrix;

out vec2 f_uv;

void main() {
	mat4 viewMatrix = cameraMatrix * objectMatrix;
	
	gl_Position = projectionMatrix * viewMatrix * vec4(pos, 1.0);
	f_uv = uv;
}
