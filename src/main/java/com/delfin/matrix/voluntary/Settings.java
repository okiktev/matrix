package com.delfin.matrix.voluntary;

import com.delfin.matrix.Settings.Position;

class Settings  {

	static int[] FONT_SIZE_RANGE = {30, 30};
	static int[] SYMBOLS_IN_LINE_RANGE = {5, 25};
	static int[] SYMBOLS_RUN_SPEED_RANGE = {30, 100};

	static int MATRIX_DEEP = 10;

	static Position TOP_POSITION = new Position(5, new int[] { -10, -10 });
	static Position MID_POSITION = new Position(3, new int[] { 20, 50 });
	static Position BOT_POSITION = new Position(6, new int[] { 0, 500 });

}
