package com.delfin.matrix;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public class Settings {

	private static String fname = "jpn_boot";

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

	public static String FONT_NAME = fname;
	
	public static int[] FONT_SIZE_RANGE = {30, 30};
	public static int[] SYMBOLS_IN_LINE_RANGE = {20, 45};
	public static int[] SYMBOLS_RUN_SPEED_RANGE = {110, 170};
	public static int[] SYMBOLS_WAIT_SPEED_RANGE = {650, 900};

	public static int[] WAIT_TICKS_RANGE = {5, 20};
	public static int[] MOVE_LENGTH_RANGE = {10, 35};

	public static int[] PAIR_RANGE = {0, 22};

	public static long DRAW_BIT = 30;

	public static int MATRIX_DEEP = 10;

	public static Position TOP_POSITION = new Position(15, new int[] { -10, -10 });
	public static Position MID_POSITION = new Position(20, new int[] { 20, 50 });
	public static Position BOT_POSITION = new Position(25, new int[] { 0, 500 });

	public static class Position {
		public int lineNumbers;
		public int[] range;

		public Position(int lineNumbers, int[] range) {
			this.lineNumbers = lineNumbers;
			this.range = range;
		}
	}

}
