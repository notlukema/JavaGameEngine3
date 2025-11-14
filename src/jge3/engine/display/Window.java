
package jge3.engine.display;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWNativeWin32.glfwGetWin32Window;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.windows.User32.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import jge3.common.image.Image;
import jge3.engine.audio.ALContext;
import jge3.engine.graphics.Graphics2d;
import jge3.engine.graphics.Graphics3d;
import jge3.engine.managers.DisplayManager;
import jge3.engine.managers.GraphicsManager;
import jge3.math.Vector2f;
import jge3.math.Vector2i;
import jge3.src2d.World2d;
import jge3.src3d.World3d;
import jge3.utils.ImageUtils;

public class Window {

	public static final int CURSOR_CAPTURED = GLFW_CURSOR_CAPTURED
							,CURSOR_DISABLED = GLFW_CURSOR_DISABLED
							,CURSOR_HIDDEN = GLFW_CURSOR_HIDDEN
							,CURSOR_NORMAL = GLFW_CURSOR_NORMAL
							,CURSOR_UNAVAILABLE = GLFW_CURSOR_UNAVAILABLE;
	
	private static boolean vSync = false;
	
	public static boolean isVSync() {
		return vSync;
	}
	
	public static void setVSync(boolean vSync) {
		Window.vSync = vSync;
		if (vSync) {
			glfwSwapInterval(1);
		} else {
			glfwSwapInterval(0);
		}
	}
	
	//
	
	private boolean terminated;
	private DisplayManager manager;
	private WindowHints extraHints;

	private GLFWWindowSizeCallback sizeCallback;
	private GLFWWindowPosCallback posCallback;
	
	private long id;
	private ALContext alContext;
	private long glContextID;
	private long alContextID;
	private int versionMajor;
	private int versionMinor;
	
	private int width;
	private int height;
	private int x;
	private int y;
	private String NAME;
	private GLFWImage.Buffer icons;
	
	private boolean fullscreen;

	private List<World2d> world2ds;
	private List<World3d> world3ds;
	
	public Window(DisplayManager manager, int width, int height, String name, long monitor, WindowHints extraHints) {
		terminated = false;
		this.manager = manager;
		this.extraHints = extraHints;
		
		icons = null;
		
		fullscreen = false;
		
        glfwDefaultWindowHints();
		createWindow(width, height, name, monitor);
        
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        x = (vidMode.width()-width)/2;
        y = (vidMode.height()-height)/2;
        glfwSetWindowPos(id, x, y);
        
        glfwMakeContextCurrent(id);
        GL.createCapabilities();
        glContextID = glfwGetCurrentContext();
        
        alContext = new ALContext();
        alContextID = alContext.getContextID();
        
        world3ds = new ArrayList<World3d>();
        world2ds = new ArrayList<World2d>();
        
        Window w = this;
        sizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long windowID, int width, int height) {
				if (id == windowID) {
					w.width = width;
					w.height = height;
				}
			}
		};
		posCallback = new GLFWWindowPosCallback() {
			@Override
			public void invoke(long windowID, int x, int y) {
				if (id == windowID) {
					w.x = x;
					w.y = y;
				}
			}
		};
	}
	
	public Window(int width, int height, String name) {
		this(width, height, name, NULL, new WindowHints());
	}
	
	public Window(int width, int height, String name, WindowHints extraHints) {
		this(width, height, name, NULL, extraHints);
	}
	
	public Window(int width, int height, String name, long monitor) {
		this(width, height, name, monitor, new WindowHints());
	}
	
	public Window(int width, int height, String name, long monitor, WindowHints extraHints) {
		this(DisplayManager.get(), width, height, name, monitor, extraHints);
		DisplayManager.get().addWindow(this);
	}
	
	private void createWindow(int width, int height, String name, long monitor) {
		this.width = width;
		this.height = height;
		NAME = name;
		
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        
		if (monitor != NULL) {
			glfwWindowHint(GLFW_RED_BITS, vidMode.redBits());
			glfwWindowHint(GLFW_GREEN_BITS, vidMode.greenBits());
			glfwWindowHint(GLFW_BLUE_BITS, vidMode.blueBits());
			glfwWindowHint(GLFW_REFRESH_RATE, vidMode.refreshRate());
		}
		
        glfwWindowHint(GLFW_SAMPLES, 4);
        
		int[] hints = extraHints.getHints();
		int[] values = extraHints.getHintValues();
		for (int i=0;i<hints.length;i++) {
			glfwWindowHint(hints[i], values[i]);
		}
		
        // create window
        id = glfwCreateWindow(width, height, name, monitor, NULL);
        if (id == NULL) {
        	throw new RuntimeException("Couldn't create window");
        }
        if (icons != null) {
            glfwSetWindowIcon(id, icons);
        }
        
        int[] minor = new int[1];
        int[] major = new int[1];
        int[] rev = new int[1];
        glfwGetVersion(minor, major, rev);
        versionMajor = major[0];
        versionMinor = minor[0];
        
        glfwSetWindowSizeCallback(id, sizeCallback);
        glfwSetWindowPosCallback(id, posCallback);
	}
	
	public void freeUsing() {
		terminated = true;
		alContext.free();
		sizeCallback.free();
		posCallback.free();
	}
	
	public long getID() {
		return id;
	}
	
	public ALContext getALContext() {
		return alContext;
	}
	
	public long getGLContextID() {
		return glContextID;
	}
	
	public long getALContextID() {
		return alContextID;
	}
	
	public int getVersionMajor() {
		return versionMajor;
	}
	
	public int getVersionMinor() {
		return versionMinor;
	}
	
	public String getVersion() {
		return versionMajor+"."+versionMinor;
	}
	
	public void clearWindowHints() {
		extraHints = new WindowHints();
	}
	
	public void setWindowHints(WindowHints extraHints) {
		this.extraHints = extraHints;
	}
	
	public World2d createWorld2d() {
		World2d world = new World2d(this);
		world2ds.add(world);
		return world;
	}
	
	public World3d createWorld3d() {
		World3d world = new World3d(this);
		world3ds.add(world);
		return world;
	}
	
	public Graphics2d createGraphics2d() {
		return GraphicsManager.get().createGraphics2d(this);
	}
	
	public Graphics3d createGraphics3d() {
		return GraphicsManager.get().createGraphics3d(this);
	}
	
	public GLFWImage.Buffer getIcons() {
		return icons;
	}
	
	public Image[] getIconImages() {
		Image[] images = new Image[icons.limit()];
		for (int i=0;i<images.length;i++) {
			images[i] = ImageUtils.toImage(icons.get(i));
		}
		return images;
	}
	
	public int getIconCount() {
		return icons.limit();
	}
	
	public GLFWImage getIcon(int i) {
		return icons.get(i);
	}
	
	public Image getIconImage(int i) {
		return ImageUtils.toImage(icons.get(i));
	}
	
	public void setIcon(Image image) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			GLFWImage.Buffer buffer = new GLFWImage.Buffer(stack.malloc(16));
			buffer.put(0, ImageUtils.toGLFWImage(image));
			setIcon(buffer);
		}
	}
	
	public void setIcon(GLFWImage image) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			GLFWImage.Buffer buffer = new GLFWImage.Buffer(stack.malloc(16));
			buffer.put(0, image);
			setIcon(buffer);
		}
	}
	
	public void setIcon(Image[] images) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			GLFWImage.Buffer buffer = new GLFWImage.Buffer(stack.malloc(images.length*16));
			for (int i=0;i<images.length;i++) {
				buffer.put(i, ImageUtils.toGLFWImage(images[i]));
			}
			setIcon(buffer);
		}
	}
	
	public void setIcon(GLFWImage[] images) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			GLFWImage.Buffer buffer = new GLFWImage.Buffer(stack.malloc(images.length*16));
			for (int i=0;i<images.length;i++) {
				buffer.put(i, images[i]);
			}
			setIcon(buffer);
		}
	}
	
	public void setIcon(GLFWImage.Buffer images) {
		icons = images;
		glfwSetWindowIcon(id, images);
	}
	
	public boolean isFullscreen() {
		return fullscreen;
	}
	
	public void toggleFullscreen() {
		toggleFullscreen(DisplayManager.get().getPrimaryMonitor());
	}
	
	public void toggleFullscreen(Monitor monitor) {
		if (fullscreen) {
			exitFullscreen();
		} else {
			enterFullscreen(monitor);
		}
	}
	
	public void enterFullscreen(Monitor monitor) {
		GLFWVidMode vidmode = glfwGetVideoMode(monitor.getID());
		glfwSetWindowMonitor(id, monitor.getID(), 0, 0, vidmode.width(), vidmode.height(), vidmode.refreshRate());
		fullscreen = true;
	}
	
	public void exitFullscreen() {
		glfwSetWindowMonitor(id, NULL, x, y, width, height, 0);
		fullscreen = false;
	}
	
	private Vector2i getSizeInternal() {
		int[] width = new int[1];
		int[] height = new int[1];
		glfwGetWindowSize(id, width, height);
		return new Vector2i(width[0], height[0]);
	}
	
	private Vector2i getPosInternal() {
		int[] x = new int[1];
		int[] y = new int[1];
		glfwGetWindowPos(id, x, y);
		return new Vector2i(x[0], y[0]);
	}
	
	public void bind() {
		setGraphicsContext();
		//GL.createCapabilities();
		setAudioContext();
        //AL.createCapabilities();
	}
	
	public long setGraphicsContext() {
		if (glfwGetCurrentContext() != glContextID) {
			glfwMakeContextCurrent(glContextID);
		}
		return glContextID;
	}
	
	public long setAudioContext() {
		if (alcGetCurrentContext() != alContextID) {
			alcMakeContextCurrent(alContextID);
		}
		return alContextID;
	}
	
	public void unbind() {
		glfwMakeContextCurrent(0);
        alcMakeContextCurrent(0);
	}
	
	public void hideCursor() {
		glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
	}
	
	public void showCursor() {
		glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}
	
	public void setCursorMode(int mode) {
		glfwSetInputMode(id, GLFW_CURSOR, mode);
	}
	
	public void defaultCursor() {
		glfwSetCursor(id, glfwCreateStandardCursor(Cursor.TYPE_DEFAULT));
	}
	
	public void setCursor(Cursor cursor) {
		glfwSetCursor(id, cursor.getID());
	}
	
	public void setCursor(int standardType) {
		Cursor cursor = new Cursor(standardType);
		glfwSetCursor(id, cursor.getID());
	}
	
	public boolean isDecorated() {
		return glfwGetWindowAttrib(id, GLFW_DECORATED) == GLFW_TRUE;
	}
	
	public void setDecorated(boolean decorated) {
		glfwSetWindowAttrib(id, GLFW_DECORATED, decorated?GLFW_TRUE:GLFW_FALSE);
	}
	
	public boolean isResizable() {
		return glfwGetWindowAttrib(id, GLFW_RESIZABLE) == GLFW_TRUE;
	}
	
	public void setResizable(boolean resizable) {
		glfwSetWindowAttrib(id, GLFW_RESIZABLE, resizable?GLFW_TRUE:GLFW_FALSE);
	}
	
	public boolean isVisible() {
		return glfwGetWindowAttrib(id, GLFW_VISIBLE) == GLFW_TRUE;
	}
	
	public void setVisible(boolean visible) {
		if (visible) {
			show();
		} else {
			hide();
		}
	}
	
	public void show() {
		glfwShowWindow(id);
	}
	
	public void hide() {
		glfwHideWindow(id);
	}
	
    public void windowsHideTaskbarIcon() {
        // Windows only
        SetWindowLongPtr(null, glfwGetWin32Window(id), GWL_EXSTYLE, WS_EX_TOOLWINDOW);
    }
	
	public boolean isFocused() {
		return glfwGetWindowAttrib(id, GLFW_FOCUSED) == GLFW_TRUE;
	}
	
	public boolean isIconified() {
		return glfwGetWindowAttrib(id, GLFW_ICONIFIED) == GLFW_TRUE;
	}
	/*
	public int getwidth() {
		if (fullscreen) {
			return (int)getSizeInternal().x;
		}
		return width;
	}
	
	public int getheight() {
		if (fullscreen) {
			return (int)getSizeInternal().y;
		}
		return height;
	}
	
	public Vector2f getSize() {
		if (fullscreen) {
			return getSizeInternal();
		}
		return new Vector2f(width, height);
	}
	*/
	public int getWidth() {
		return getSizeInternal().x;
	}
	
	public int getHeight() {
		return getSizeInternal().y;
	}
	
	public Vector2i getSize() {
		return getSizeInternal();
	}
	
	public Vector2i getSavedSize() {
		return new Vector2i(width, height);
	}
	/*
	public int getx() {
		if (fullscreen) {
			return (int)getPosInternal().x;
		}
		return x;
	}
	
	public int gety() {
		if (fullscreen) {
			return (int)getPosInternal().y;
		}
		return y;
	}
	
	public Vector2i getPos() {
		if (fullscreen) {
			return getPosInternal();
		}
		return new Vector2i(x, y);
	}
	*/
	public int getX() {
		return getPosInternal().x;
	}
	
	public int getY() {
		return getPosInternal().y;
	}
	
	public Vector2i getPos() {
		return getPosInternal();
	}
	
	public Vector2i getSavedPos() {
		return new Vector2i(x, y);
	}
	
	public String getName() {
		return NAME;
	}
	
	public void setWidth(int width) {
		this.width = width;
		glfwSetWindowSize(id, width, height);
	}
	
	public void setHeight(int height) {
		this.height = height;
		glfwSetWindowSize(id, width, height);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		glfwSetWindowSize(id, width, height);
	}
	
	public void setX(int x) {
		this.x = x;
		glfwSetWindowPos(id, x, y);
	}
	
	public void setY(int y) {
		this.y = y;
		glfwSetWindowPos(id, x, y);
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		glfwSetWindowPos(id, x, y);
	}
	
	public void setName(String name) {
		NAME = name;
		glfwSetWindowTitle(id, NAME);
	}
	
	public void setCursorPosition(Vector2f pos) {
		glfwSetCursorPos(id, (int)pos.x, (int)pos.y);
	}
	
	public void setCursorPosition(int x, int y) {
		glfwSetCursorPos(id, x, y);
	}
	
	public void renderToScreen() {
		if (glfwGetCurrentContext() != glContextID) {
			glfwMakeContextCurrent(id);
		}
		glfwSwapBuffers(id);
		glfwMakeContextCurrent(0);
	}
	
	public boolean closeRequested() {
		return glfwWindowShouldClose(id);
	}
	
	public void setCloseRequested(boolean requested) {
		glfwSetWindowShouldClose(id, requested);
	}
	
	public boolean terminated() {
		return terminated;
	}
	
	public void destroy() {
		manager.destroy(this);
	}
	
	public void setCharCallback(GLFWCharCallback callback) {
		glfwSetCharCallback(id, callback);
	}
	
	public void setCharModsCallback(GLFWCharModsCallback callback) {
		glfwSetCharModsCallback(id, callback);
	}
	
	public void setCursorEnterCallback(GLFWCursorEnterCallback callback) {
		glfwSetCursorEnterCallback(id, callback);
	}
	
	public void setCursorPosCallback(GLFWCursorPosCallback callback) {
		glfwSetCursorPosCallback(id, callback);
	}
	
	public void setDropCallback(GLFWDropCallback callback) {
		glfwSetDropCallback(id, callback);
	}
	
	public void setFramebufferSizeCallback(GLFWFramebufferSizeCallback callback) {
		glfwSetFramebufferSizeCallback(id, callback);
	}
	
	public void setKeyCallback(GLFWKeyCallback callback) {
		glfwSetKeyCallback(id, callback);
	}
	
	public void setMouseButtonCallback(GLFWMouseButtonCallback callback) {
		glfwSetMouseButtonCallback(id, callback);
	}
	
	public void setScrollCallback(GLFWScrollCallback callback) {
		glfwSetScrollCallback(id, callback);
	}
	
	public void setWindowCloseCallback(GLFWWindowCloseCallback callback) {
		glfwSetWindowCloseCallback(id, callback);
	}
	
	public void setWindowContentScaleCallback(GLFWWindowContentScaleCallback callback) {
		glfwSetWindowContentScaleCallback(id, callback);
	}
	
	public void setWindowFocusCallback(GLFWWindowFocusCallback callback) {
		glfwSetWindowFocusCallback(id, callback);
	}
	
	public void setWindowIconifyCallback(GLFWWindowIconifyCallback callback) {
		glfwSetWindowIconifyCallback(id, callback);
	}
	
	public void setWindowMaximizeCallback(GLFWWindowMaximizeCallback callback) {
		glfwSetWindowMaximizeCallback(id, callback);
	}
	
	public void setWindowPosCallback(GLFWWindowPosCallback callback) {
		glfwSetWindowPosCallback(id, callback);
	}
	
	public void setWindowRefreshCallback(GLFWWindowRefreshCallback callback) {
		glfwSetWindowRefreshCallback(id, callback);
	}
	
	public void setWindowSizeCallback(GLFWWindowSizeCallback callback) {
		glfwSetWindowSizeCallback(id, callback);
	}
	
}
