
#version 330 core

in vec2 f_uv;

uniform sampler2D spriteTexture;

out vec4 frag_color;

void main() {
	frag_color = texture(spriteTexture, f_uv);
}
