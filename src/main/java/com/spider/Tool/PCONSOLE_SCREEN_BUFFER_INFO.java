package com.spider.Tool;

import com.sun.jna.Structure;

public class PCONSOLE_SCREEN_BUFFER_INFO extends Structure {

	public COORD dwSize;
	public COORD dwCursorPosition;
	public short wAttributes;
	public SMALL_RECT srWindow;
	public COORD dwMaximumWindowSize;
	
}
