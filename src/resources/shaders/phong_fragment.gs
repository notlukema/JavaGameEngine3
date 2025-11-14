
#version 330 core

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in vec3 g_position[];
in vec3 g_normal[];
in vec2 g_uv[];

out vec3 f_position;
out vec3 f_normal;
out vec2 f_uv;

vec3 triangle_normal;

void set(int i) {
	gl_Position = gl_in[i].gl_Position;
	if (g_normal[i] == vec3(0.0)) {
		f_normal = triangle_normal;
	} else {
		f_normal = g_normal[i];
	}
	//if (triangle_normal.z < 0) {
	//	f_normal = -f_normal;
	//}
	f_position = g_position[i];
	f_uv = g_uv[i];
}

void main() {
	
    vec3 a = g_position[0] - g_position[1];
    vec3 b = g_position[2] - g_position[1];
	triangle_normal = normalize(cross(a, b));
	
	set(0);
	EmitVertex();
	
	set(1);
	EmitVertex();
	
	set(2);
	EmitVertex();
	
	EndPrimitive();
}
