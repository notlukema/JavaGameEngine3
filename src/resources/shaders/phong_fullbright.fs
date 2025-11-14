
#version 330 core

in vec3 f_position;
in vec3 f_normal;
in vec2 f_uv;

struct Material
{
	sampler2D diffuseTxt;
	sampler2D ambientTxt;
	sampler2D specularTxt;
	sampler2D normalTxt;

	float emission;
	float specular;
};

const float ONE_MAX = 1.0;

uniform Material material;

out vec4 frag_color;

void main() {
	vec4 diffuseColor = texture(material.diffuseTxt, f_uv);
	if (diffuseColor.a <= 0) {
		discard;
	}
	frag_color = diffuseColor;
}
