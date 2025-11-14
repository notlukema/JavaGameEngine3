
package resources.shaders;

import java.io.InputStream;

import resources.Resources;

public class ShaderResources extends Resources {
	
	public static InputStream getResource(String name) {
		return ShaderResources.class.getResourceAsStream(name);
	}
	
}
