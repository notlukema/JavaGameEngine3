
package resources.fonts;

import java.io.InputStream;

import resources.Resources;

public class FontResources extends Resources {
	
	public static InputStream getResource(String name) {
		return FontResources.class.getResourceAsStream(name);
	}
	
}
