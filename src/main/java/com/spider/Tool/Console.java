package com.spider.Tool;

public class Console {

    private static final Kernel32 kernel32 = Kernel32.INSTANCE;

    private static final int hStdout = kernel32.GetStdHandle(-11);


    public static void setTitle(String title) {
        kernel32.SetConsoleTitleA(title);
    }

    public static void clear() {
        PCONSOLE_SCREEN_BUFFER_INFO buff = new PCONSOLE_SCREEN_BUFFER_INFO();

        kernel32.GetConsoleScreenBufferInfo(hStdout, buff);

        int dwConSize = buff.dwSize.x * buff.dwSize.y;

        kernel32.FillConsoleOutputCharacterA(hStdout, 32, dwConSize, 0,
                new int[1]);

        kernel32.GetConsoleScreenBufferInfo(hStdout, buff);

        kernel32.FillConsoleOutputAttribute(hStdout, buff.wAttributes,
                dwConSize, 0, new int[1]);

        kernel32.SetConsoleCursorPosition(hStdout, 0);
    }


    public static void setColor(int color) {
        kernel32.SetConsoleTextAttribute(hStdout, color);
    }
}
