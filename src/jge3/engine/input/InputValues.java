
package jge3.engine.input;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class InputValues {
	
	private static HashMap<String, Integer> KEY_NAMES;
	private static HashMap<String, String> SHIFTED_KEYS;
	
	public static void initialize() {
		KEY_NAMES = new LinkedHashMap<String, Integer>();
		SHIFTED_KEYS = new LinkedHashMap<String, String>();
		
		KEY_NAMES.put("unknown", -1);
		KEY_NAMES.put("space", 32);
		KEY_NAMES.put(" ", 32);
		KEY_NAMES.put("apostrophe", 39);
		KEY_NAMES.put("\'", 39);
		KEY_NAMES.put("comma", 44);
		KEY_NAMES.put(",", 44);
		KEY_NAMES.put("minus", 45);
		KEY_NAMES.put("-", 45);
		KEY_NAMES.put("period", 46);
		KEY_NAMES.put(".", 46);
		KEY_NAMES.put("slash", 47);
		KEY_NAMES.put("/", 47);
		KEY_NAMES.put("0", 48);
		KEY_NAMES.put("1", 49);
		KEY_NAMES.put("2", 50);
		KEY_NAMES.put("3", 51);
		KEY_NAMES.put("4", 52);
		KEY_NAMES.put("5", 53);
		KEY_NAMES.put("6", 54);
		KEY_NAMES.put("7", 55);
		KEY_NAMES.put("8", 56);
		KEY_NAMES.put("9", 57);
		KEY_NAMES.put("semicolon", 59);
		KEY_NAMES.put(";", 59);
		KEY_NAMES.put("equal", 61);
		KEY_NAMES.put("=", 61);
		KEY_NAMES.put("a", 65);
		KEY_NAMES.put("b", 66);
		KEY_NAMES.put("c", 67);
		KEY_NAMES.put("d", 68);
		KEY_NAMES.put("e", 69);
		KEY_NAMES.put("f", 70);
		KEY_NAMES.put("g", 71);
		KEY_NAMES.put("h", 72);
		KEY_NAMES.put("i", 73);
		KEY_NAMES.put("j", 74);
		KEY_NAMES.put("k", 75);
		KEY_NAMES.put("l", 76);
		KEY_NAMES.put("m", 77);
		KEY_NAMES.put("n", 78);
		KEY_NAMES.put("o", 79);
		KEY_NAMES.put("p", 80);
		KEY_NAMES.put("q", 81);
		KEY_NAMES.put("r", 82);
		KEY_NAMES.put("s", 83);
		KEY_NAMES.put("t", 84);
		KEY_NAMES.put("u", 85);
		KEY_NAMES.put("v", 86);
		KEY_NAMES.put("w", 87);
		KEY_NAMES.put("x", 88);
		KEY_NAMES.put("y", 89);
		KEY_NAMES.put("z", 90);
		KEY_NAMES.put("left bracket", 91);
		KEY_NAMES.put("[", 91);
		KEY_NAMES.put("backslash", 92);
		KEY_NAMES.put("\\", 92);
		KEY_NAMES.put("right bracket", 93);
		KEY_NAMES.put("]", 93);
		KEY_NAMES.put("grave accent", 96);
		KEY_NAMES.put("`", 96);
		KEY_NAMES.put("world 1", 161);
		KEY_NAMES.put("world 2", 162);
		KEY_NAMES.put("escape", 256);
		KEY_NAMES.put("enter", 257);
		KEY_NAMES.put("tab", 258);
		KEY_NAMES.put("backspace", 259);
		KEY_NAMES.put("insert", 260);
		KEY_NAMES.put("delete", 261);
		KEY_NAMES.put("right", 262);
		KEY_NAMES.put("left", 263);
		KEY_NAMES.put("down", 264);
		KEY_NAMES.put("up", 265);
		KEY_NAMES.put("page up", 266);
		KEY_NAMES.put("page down", 267);
		KEY_NAMES.put("home", 268);
		KEY_NAMES.put("end", 269);
		KEY_NAMES.put("caps lock", 280);
		KEY_NAMES.put("scroll lock", 281);
		KEY_NAMES.put("num lock", 282);
		KEY_NAMES.put("print screen", 283);
		KEY_NAMES.put("pause", 284);
		KEY_NAMES.put("f1", 290);
		KEY_NAMES.put("f2", 291);
		KEY_NAMES.put("f3", 292);
		KEY_NAMES.put("f4", 293);
		KEY_NAMES.put("f5", 294);
		KEY_NAMES.put("f6", 295);
		KEY_NAMES.put("f7", 296);
		KEY_NAMES.put("f8", 297);
		KEY_NAMES.put("f9", 298);
		KEY_NAMES.put("f10", 299);
		KEY_NAMES.put("f11", 300);
		KEY_NAMES.put("f12", 301);
		KEY_NAMES.put("f13", 302);
		KEY_NAMES.put("f14", 303);
		KEY_NAMES.put("f15", 304);
		KEY_NAMES.put("f16", 305);
		KEY_NAMES.put("f17", 306);
		KEY_NAMES.put("f18", 307);
		KEY_NAMES.put("f19", 308);
		KEY_NAMES.put("f20", 309);
		KEY_NAMES.put("f22", 311);
		KEY_NAMES.put("f23", 312);
		KEY_NAMES.put("f24", 313);
		KEY_NAMES.put("f25", 314);
		KEY_NAMES.put("kp 0", 320);
		KEY_NAMES.put("kp 1", 321);
		KEY_NAMES.put("kp 2", 322);
		KEY_NAMES.put("kp 3", 323);
		KEY_NAMES.put("kp 4", 324);
		KEY_NAMES.put("kp 5", 325);
		KEY_NAMES.put("kp 6", 326);
		KEY_NAMES.put("kp 7", 327);
		KEY_NAMES.put("kp 8", 328);
		KEY_NAMES.put("kp 9", 329);
		KEY_NAMES.put("kp decimal", 330);
		KEY_NAMES.put("kp divide", 331);
		KEY_NAMES.put("kp multiply", 332);
		KEY_NAMES.put("kp subtract", 333);
		KEY_NAMES.put("kp add", 334);
		KEY_NAMES.put("kp enter", 335);
		KEY_NAMES.put("kp equal", 336);
		KEY_NAMES.put("left shift", 340);
		KEY_NAMES.put("left control", 341);
		KEY_NAMES.put("left alt", 342);
		KEY_NAMES.put("left super", 343);
		KEY_NAMES.put("right shift", 344);
		KEY_NAMES.put("right control", 345);
		KEY_NAMES.put("right alt", 346);
		KEY_NAMES.put("right super", 347);
		KEY_NAMES.put("menu", 348);

		SHIFTED_KEYS.put("a", "A");
		SHIFTED_KEYS.put("b", "B");
		SHIFTED_KEYS.put("c", "C");
		SHIFTED_KEYS.put("d", "D");
		SHIFTED_KEYS.put("e", "E");
		SHIFTED_KEYS.put("f", "F");
		SHIFTED_KEYS.put("g", "G");
		SHIFTED_KEYS.put("h", "H");
		SHIFTED_KEYS.put("i", "I");
		SHIFTED_KEYS.put("j", "J");
		SHIFTED_KEYS.put("k", "K");
		SHIFTED_KEYS.put("l", "L");
		SHIFTED_KEYS.put("m", "M");
		SHIFTED_KEYS.put("n", "N");
		SHIFTED_KEYS.put("o", "O");
		SHIFTED_KEYS.put("p", "P");
		SHIFTED_KEYS.put("q", "Q");
		SHIFTED_KEYS.put("r", "R");
		SHIFTED_KEYS.put("s", "S");
		SHIFTED_KEYS.put("t", "T");
		SHIFTED_KEYS.put("u", "U");
		SHIFTED_KEYS.put("v", "V");
		SHIFTED_KEYS.put("w", "W");
		SHIFTED_KEYS.put("x", "X");
		SHIFTED_KEYS.put("y", "Y");
		SHIFTED_KEYS.put("z", "Z");
		SHIFTED_KEYS.put("1", "!");
		SHIFTED_KEYS.put("2", "@");
		SHIFTED_KEYS.put("3", "#");
		SHIFTED_KEYS.put("4", "$");
		SHIFTED_KEYS.put("5", "%");
		SHIFTED_KEYS.put("6", "^");
		SHIFTED_KEYS.put("7", "&");
		SHIFTED_KEYS.put("8", "*");
		SHIFTED_KEYS.put("9", "(");
		SHIFTED_KEYS.put("0", ")");
		SHIFTED_KEYS.put("-", "_");
		SHIFTED_KEYS.put("=", "+");
		SHIFTED_KEYS.put("[", "{");
		SHIFTED_KEYS.put("]", "}");
		SHIFTED_KEYS.put("\\", "|");
		SHIFTED_KEYS.put(";", ":");
		SHIFTED_KEYS.put("\'", "\"");
		SHIFTED_KEYS.put(",", "<");
		SHIFTED_KEYS.put(".", ">");
		SHIFTED_KEYS.put("/", "?");
		SHIFTED_KEYS.put("`", "~");
	}
	
	public static final int LEFT_MOUSE = 0;
	public static final int RIGHT_MOUSE = 1;
	public static final int MIDDLE_MOUSE = 2;
	
	public static int mouseValueOf(String mouse) {
		String str = mouse.toLowerCase();
		if (str.equals("left") || str.equals("left mouse") || str.equals("left mouse button")) {
			return LEFT_MOUSE;
		}
		if (str.equals("right") || str.equals("right mouse") || str.equals("right mouse button")) {
			return RIGHT_MOUSE;
		}
		if (str.equals("middle") || str.equals("middle mouse") || str.equals("middle mouse button")) {
			return MIDDLE_MOUSE;
		}
		return UNKNOWN;
	}
	
	public static String mouseNameOf(int mouse) {
		if (mouse == 0) {
			return "left mouse button";
		}
		if (mouse == 1) {
			return "right mouse button";
		}
		if (mouse == 2) {
			return "middle mouse button";
		}
		
		for (Entry<String, Integer> entry : KEY_NAMES.entrySet()) {
			if (entry.getValue() == UNKNOWN) {
				return entry.getKey();
			}
		}
		
		return "";
	}
	
	public static int keyValueOf(String key) {
		String str = key.toLowerCase();
		if (KEY_NAMES.containsKey(str)) {
			return KEY_NAMES.get(str);
		}
		return UNKNOWN;
	}
	
	public static String keyNameOf(int key) {
		return keyNameOf(key, false);
	}
	
	public static String keySymbolOf(int key) {
		return keyNameOf(key, true);
	}
	
	public static String keyNameOf(int key, boolean symbol) {
		String unknown = "";
		String last = null;
		for (Entry<String, Integer> entry : KEY_NAMES.entrySet()) {
			int value = entry.getValue();
			if (key == value) {
				last = entry.getKey();
				if (!symbol) {
					return last;
				}
			} else if (value == UNKNOWN) {
				unknown = entry.getKey();
			}
		}
		if (last != null) {
			return last;
		} else {
			return unknown;
		}
	}
	
	public static boolean hasSymbol(int key) {
		for (Entry<String, Integer> entry : KEY_NAMES.entrySet()) {
			int value = entry.getValue();
			if (key == value) {
				if (entry.getKey().length() == 1) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static String shift(String key) {
		String shifted = SHIFTED_KEYS.get(key);
		return shifted == null ? key : shifted;
	}
	
	public static boolean isShiftPressed() {
		return (Input.get().isKeyPressed(RIGHT_SHIFT) || Input.get().isKeyPressed(LEFT_SHIFT));
	}
	
	public static String shiftIfShifting(String key) {
		if (isShiftPressed()) {
			return shift(key);
		} else {
			return key;
		}
	}
	
	// GLFW key values
	
	public static final int
			UNKNOWN = -1
			,SPACE = 32
			,APOSTROPHE = 39
			,COMMA = 44
			,MINUS = 45
			,PERIOD = 46
			,SLASH = 47
			,NUM_0 = 48
			,NUM_1 = 49
			,NUM_2 = 50
			,NUM_3 = 51
			,NUM_4 = 52
			,NUM_5 = 53
			,NUM_6 = 54
			,NUM_7 = 55
			,NUM_8 = 56
			,NUM_9 = 57
			,SEMICOLON = 59
			,EQUAL = 61
			,A = 65
			,B = 66
			,C = 67
			,D = 68
			,E = 69
			,F = 70
			,G = 71
			,H = 72
			,I = 73
			,J = 74
			,K = 75
			,L = 76
			,M = 77
			,N = 78
			,O = 79
			,P = 80
			,Q = 81
			,R = 82
			,S = 83
			,T = 84
			,U = 85
			,V = 86
			,W = 87
			,X = 88
			,Y = 89
			,Z = 90
			,LEFT_BRACKET = 91
			,BACKSLASH = 92
			,RIGHT_BRACKET = 93
			,GRAVE_ACCENT = 96
			,WORLD_1 = 161
			,WORLD_2 = 162
			,ESCAPE = 256
			,ENTER = 257
			,TAB = 258
			,BACKSPACE = 259
			,INSERT = 260
			,DELETE = 261
			,RIGHT = 262
			,LEFT = 263
			,DOWN = 264
			,UP = 265
			,PAGE_UP = 266
			,PAGE_DOWN = 267
			,HOME = 268
			,END = 269
			,CAPS_LOCK = 280
			,SCROLL_LOCK = 281
			,NUM_LOCK = 282
			,PRINT_SCREEN = 283
			,PAUSE = 284
			,F1 = 290
			,F2 = 291
			,F3 = 292
			,F4 = 293
			,F5 = 294
			,F6 = 295
			,F7 = 296
			,F8 = 297
			,F9 = 298
			,F10 = 299
			,F11 = 300
			,F12 = 301
			,F13 = 302
			,F14 = 303
			,F15 = 304
			,F16 = 305
			,F17 = 306
			,F18 = 307
			,F19 = 308
			,F20 = 309
			,F22 = 311
			,F23 = 312
			,F24 = 313
			,F25 = 314
			,KP_0 = 320
			,KP_1 = 321
			,KP_2 = 322
			,KP_3 = 323
			,KP_4 = 324
			,KP_5 = 325
			,KP_6 = 326
			,KP_7 = 327
			,KP_8 = 328
			,KP_9 = 329
			,KP_DECIMAL = 330
			,KP_DIVIDE = 331
			,KP_MULTIPLY = 332
			,KP_SUBTRACT = 333
			,KP_ADD = 334
			,KP_ENTER = 335
			,KP_EQUAL = 336
			,LEFT_SHIFT = 340
			,LEFT_CONTROL = 341
			,LEFT_ALT = 342
			,LEFT_SUPER = 343
			,RIGHT_SHIFT = 344
			,RIGHT_CONTROL = 345
			,RIGHT_ALT = 346
			,RIGHT_SUPER = 347
			,MENU = 348;
	
}
