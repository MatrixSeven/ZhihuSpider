package com.spider.Tool;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface Kernel32 extends StdCallLibrary {

	Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("Kernel32",Kernel32.class);
	
	int GetStdHandle(int a);
	
	boolean SetConsoleTextAttribute(int hStdout, int color);
	
	boolean GetConsoleScreenBufferInfo(int hStdout, PCONSOLE_SCREEN_BUFFER_INFO buff);

	boolean FillConsoleOutputCharacterA(int hStdout, int cCharacter, int nLength, int dwWriteCoord, int[] lpNumberOfCharsWritten);

	boolean FillConsoleOutputAttribute(int hStdout, short wAttribute, int nLength, int dwWriteCoord, int[] lpNumberOfAttrsWritten);
	
	boolean SetConsoleCursorPosition(int hStdout, int dwCursorPosition);
	
	boolean SetConsoleTitleA(String title);
	
	void CloseHandle(int hObj);
	
}
