
package resources;

import java.io.InputStream;

public class Resources {
	
	public static InputStream getResource(String name) {
		return Resources.class.getResourceAsStream(name);
	}
	
}
