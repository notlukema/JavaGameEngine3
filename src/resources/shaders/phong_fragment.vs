
#version 330 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec2 uv;
layout(location = 3) in ivec4 bones;
layout(location = 4) in vec4 weights;

uniform mat4 projectionMatrix;
uniform mat4 cameraMatrix;
uniform mat4 objectMatrix;

const int MAX_BONES_TOTAL = 200;
const int MAX_BONES = 4;
uniform mat4 boneMatrices[MAX_BONES_TOTAL];

out vec3 g_position;
out vec3 g_normal;
out vec2 g_uv;

void main() {
	mat4 viewMatrix = cameraMatrix * objectMatrix;
	vec4 position;

	if (bones[0] < 0) {
		position = viewMatrix * vec4(pos, 1.0);
		g_normal = mat3(viewMatrix) * normal;
	} else {
		mat4 boneMatrix = mat4(0.0);
		for (int i = 0; i < MAX_BONES; i++) {
			if (bones[i] < 0 || bones[i] > MAX_BONES_TOTAL) {
				continue;
			}
			boneMatrix += boneMatrices[bones[i]] * weights[i];
		}
		position = viewMatrix * boneMatrix * vec4(pos, 1.0);
		g_normal = mat3(viewMatrix) * mat3(boneMatrix) * normal;
	}

	g_position = position.xyz / position.w;
	gl_Position = projectionMatrix * position;
	g_uv = uv;
}
