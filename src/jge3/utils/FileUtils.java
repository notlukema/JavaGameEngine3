
package jge3.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
	
	public static final String DIRECTORY = System.getProperty("user.dir");
	public static final String[] DIRECTORY_COMPONENTS = DIRECTORY.split("\\\\");
	public static final int SYSTEM = 0
						,USERS = 1
						,USER = 2;
	public static final String USER_DIRECTORY = DIRECTORY_COMPONENTS[SYSTEM]+"\\"+DIRECTORY_COMPONENTS[USERS]+"\\"+DIRECTORY_COMPONENTS[USER];
	
	public static final Charset CHARSET = Charset.defaultCharset();
	public static final String CHARSET_STRING = Charset.defaultCharset().toString();
	
	public static String getClassPath(Object object) {
		return object.getClass().getPackageName();
	}
	
	public static String loadResource(Object object, String name) {
		return loadResource(object.getClass().getResourceAsStream(name));
	}
	
	public static String loadResource(InputStream stream) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String file = "";
			String line;
		    while ((line = reader.readLine()) != null) {
		    	file += line+"\n";
		    }
	    	reader.close();
	    	return file;
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load resource:\n"+e.getLocalizedMessage());
		}
	}
	
	public static byte[] loadResourceBytes(Object object, String name) {
		return loadResourceBytes(object.getClass().getResourceAsStream(name));
	}
	
	public static byte[] loadResourceBytes(InputStream stream) {
		try {
			return stream.readAllBytes();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load resource:\n"+e.getLocalizedMessage());
		}
	}
	
	public static String loadFile(String filePath) {
		return loadFile(filePath, CHARSET);
	}
	
	public static String loadFile(String filePath, Charset charset) {
		try {
			return Files.readString(Path.of(filePath), charset);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load file \""+filePath+"\":\n"+e.getLocalizedMessage());
		}
	}
	
	public static byte[] loadFileBytes(String filePath) {
		try {
			return Files.readAllBytes(Path.of(filePath));
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load file \""+filePath+"\":\n"+e.getLocalizedMessage());
		}
	}
	
	public static abstract class FileIterator {
		public abstract void iterate(String line);
	}
	
	public static void iterateFile(String filePath, FileIterator iterator) {
		try {
			FileInputStream stream = new FileInputStream(filePath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line;
		    while ((line = reader.readLine()) != null) {
		    	iterator.iterate(line);
		    }
	    	reader.close();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load file \""+filePath+"\":\n"+e.getLocalizedMessage());
		}
	}
	
	public static File getFile(String filePath) {
		return new File(filePath);
	}
	
	public static File createFile(String filePath) {
		File file = new File(filePath);
		try {
			if (!file.createNewFile()) {
				return null;
			}
		} catch (IOException e) {
			throw new RuntimeException("Couldn't create file \""+filePath+"\":\n"+e.getLocalizedMessage());
		}
		return file;
	}
	
	public static File getOrCreate(String filePath) {
		File file = new File(filePath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't create file \""+filePath+"\":\n"+e.getLocalizedMessage());
		}
		return file;
	}
	
	public static void writeFile(String filePath, String text) {
		writeFile(getOrCreate(filePath), text.getBytes(CHARSET));
	}
	
	public static void writeFile(File file, String text) {
		writeFile(file, text.getBytes(CHARSET));
	}
	
	public static void writeFile(String filePath, byte[] bytes) {
		writeFile(getOrCreate(filePath), bytes);
	}
	
	public static void writeFile(File file, byte[] bytes) {
		try {
			FileOutputStream stream = new FileOutputStream(file);
			stream.write(bytes);
			stream.close();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't write to file \""+file.getPath()+"\":\n"+e.getLocalizedMessage());
		}
	}
	
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (!file.delete()) {
			throw new RuntimeException("Couldn't delete file \""+filePath+"\"");
		}
	}
	
}
