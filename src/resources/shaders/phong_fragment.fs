
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

struct AmbientLight
{
	float intensity;
	vec4 color;
};

struct DirectionalLight
{
	vec3 dir;
	float intensity;
	vec4 color;
};

struct PointLight
{
	vec3 pos;
	float intensity;
	vec4 color;

	float constAtt;
	float linAtt;
	float quadAtt;
};

struct SpotLight
{
	vec3 pos;
	vec3 dir;
	float intensity;
	vec4 color;
	float coneMin;
	float coneMax;
	float coneDamp;
	
	float constAtt;
	float linAtt;
	float quadAtt;
};

const float ONE_MAX = 1.0;

const int MAX_AMBIENT_LIGHTS = 50;
const int MAX_DIRECTIONAL_LIGHTS = 10;
const int MAX_POINT_LIGHTS = 300;
const int MAX_SPOT_LIGHTS = 150;

uniform mat4 cameraMatrix;

uniform Material material;

uniform int ambientLightCount;
uniform int directionalLightCount;
uniform int pointLightCount;
uniform int spotLightCount;

uniform AmbientLight ambientLights[MAX_AMBIENT_LIGHTS];
uniform DirectionalLight directionalLights[MAX_DIRECTIONAL_LIGHTS];
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];

out vec4 frag_color;

vec3 viewDir = vec3(1.0, 0.0, 0.0);

float diffuse(vec3 lightDir, vec3 normal) {
	return max(0.0, dot(lightDir, normal));
}

float phong(vec3 viewDir, vec3 lightDir, vec3 normal) {
	return max(0.0, dot(viewDir, normalize(reflect(-lightDir, normal))));
}

float blinnPhong(vec3 viewDir, vec3 lightDir, vec3 normal) {
	return max(0.0, dot(normalize(viewDir+lightDir), normal));
}

//float invAttenuation(float constant, float linear, float quadratic, float distance) {
//	return 1.0/(constant + linear*distance + quadratic*distance*distance);
//}

vec4 calcAmbientLight(AmbientLight light, vec3 position, vec4 diffuse, float ambient) {
	return diffuse * ambient * light.color * light.intensity;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal, vec4 diffuse, float specular) {
	vec4 diffuseColor = vec4(0, 0, 0, 0);
	vec4 specularColor = vec4(0, 0, 0, 0);
	vec3 lightDir = normalize(mat3(cameraMatrix) * light.dir);
	
	//diffuseColor = diffuse * light.color * light.intensity * diffuse(lightDir, normal);
	diffuseColor = diffuse * light.color * light.intensity * max(0.0, dot(lightDir, normal));
    specularColor = specular * light.color * light.intensity * pow(blinnPhong(viewDir, lightDir, normal), material.specular);
	
	return diffuseColor + specularColor;
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal, vec4 diffuse, float specular) {
	vec4 diffuseColor = vec4(0, 0, 0, 0);
	vec4 specularColor = vec4(0, 0, 0, 0);
	vec3 lightPos = (cameraMatrix * vec4(light.pos, 1.0)).xyz;
	
	vec3 lightRelative = lightPos - position;
	float d = length(lightRelative);
	vec3 lightDir = lightRelative/d;
	
	//diffuseColor = diffuse * light.color * light.intensity * diffuse(lightDir, normal);
	diffuseColor = diffuse * light.color * light.intensity * max(0.0, dot(lightDir, normal));
    specularColor = specular * light.color * light.intensity * pow(blinnPhong(viewDir, lightDir, normal), material.specular);
    
	return (diffuseColor+specularColor)*(1.0/(light.constAtt + light.linAtt*d + light.quadAtt*d*d));
	//return (diffuseColor+specularColor)*invAttenuation(light.constAtt, light.linAtt, light.quadAtt, d);
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal, vec4 diffuse, float specular) {
	vec4 diffuseColor = vec4(0, 0, 0, 0);
	vec4 specularColor = vec4(0, 0, 0, 0);
	vec3 lightPos = (cameraMatrix * vec4(light.pos, 1.0)).xyz;
	vec3 lightDir0 = normalize(mat3(cameraMatrix) * light.dir);
	
	vec3 lightRelative = lightPos - position;
	float d = length(lightRelative);
	vec3 lightDir = lightRelative/d;
	
	float angle = degrees(acos(dot(lightDir, lightDir0)));
	if (angle > light.coneMax || angle < light.coneMin) {
		return diffuseColor;
	}
	
	float p;
	if (light.coneMin == 0) {
		p = cos(angle/light.coneMax*1.57)*light.coneDamp + (1-light.coneDamp);
	} else {
		p = cos((angle - 0.5*(light.coneMin+light.coneMax))/(light.coneMax-light.coneMin)*3.14)*light.coneDamp + (1-light.coneDamp);
	}
	
	//diffuseColor = diffuse * light.color * light.intensity * diffuse(lightDir, normal);
	diffuseColor = diffuse * light.color * light.intensity * max(0.0, dot(lightDir, normal));
    specularColor = specular * light.color * light.intensity * pow(blinnPhong(viewDir, lightDir, normal), material.specular);
    
	return (diffuseColor+specularColor)*(1.0/(light.constAtt + light.linAtt*d + light.quadAtt*d*d))*p;
	//return (diffuseColor+specularColor)*invAttenuation(light.constAtt, light.linAtt, light.quadAtt, d)*p;
}

void main() {
	vec4 diffuseColor = texture(material.diffuseTxt, f_uv);
	if (diffuseColor.a <= 0) {
		discard;
	}
	viewDir = normalize(-f_position);
	vec3 normal = normalize(f_normal);
	float ambient = texture(material.ambientTxt, f_uv).r * ONE_MAX;
	float specularReflect = texture(material.specularTxt, f_uv).r * ONE_MAX;
	
	vec3 color = vec3(0.0, 0.0, 0.0);
	
	for (int i=0;i<ambientLightCount;i++) {
		if (ambientLights[i].intensity > 0) {
			color += calcAmbientLight(ambientLights[i], f_position, diffuseColor, ambient).rgb;
		}
	}
	for (int i=0;i<directionalLightCount;i++) {
		if (directionalLights[i].intensity > 0) {
			color += calcDirectionalLight(directionalLights[i], f_position, normal, diffuseColor, specularReflect).rgb;
		}
	}
	for (int i=0;i<pointLightCount;i++) {
		if (pointLights[i].intensity > 0) {
			color += calcPointLight(pointLights[i], f_position, normal, diffuseColor, specularReflect).rgb;
		}
	}
	for (int i=0;i<spotLightCount;i++) {
		if (spotLights[i].intensity > 0) {
			color += calcSpotLight(spotLights[i], f_position, normal, diffuseColor, specularReflect).rgb;
		}
	}

	frag_color = vec4(color, diffuseColor.a);
}
