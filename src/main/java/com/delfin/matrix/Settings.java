package com.delfin.matrix;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

class Settings {
	static String fname = "jpn_boot";

	static {
		InputStream in = null;
		try {
			in = Settings.class.getClassLoader().getResourceAsStream("fonts/" + fname + ".ttf");
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, in));
		} catch (Exception e) {
			e.printStackTrace(System.err);
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					ex.printStackTrace(System.err);
				}
			}
		}
	}

	static String FONT_NAME = fname;
	
	static int[] FONT_SIZE_RANGE = {30, 30};
	static int[] SYMBOLS_IN_LINE_RANGE = {5, 25};
	static int[] SYMBOLS_SPEED_RANGE = {30, 100};

	static int MATRIX_DEEP = 10;

	static Position TOP_POSITION = new Position(5, new int[] { -10, -10 });
	static Position MID_POSITION = new Position(3, new int[] { 20, 50 });
	static Position BOT_POSITION = new Position(6, new int[] { 0, 500 });

	static class Position {
		int lineNumbers;
		int[] range;

		Position(int lineNumbers, int[] range) {
			this.lineNumbers = lineNumbers;
			this.range = range;
		}
	}

}
