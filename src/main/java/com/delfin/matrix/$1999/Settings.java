package com.delfin.matrix.$1999;

import com.delfin.matrix.Matrix.Type;

class Settings extends com.delfin.matrix.settings.Settings {

	private int[] symbolsWaitSpeedRange;
	private int[] waitTicksRange;
	private int[] moveLengthRange;
	private int[] pairRange;
	private long drawBit;

	private static Settings instance;

	static Settings getInstance() {
		if (com.delfin.matrix.settings.Settings.doReload) {
			instance = null;
		}
		if (instance == null) {
			init();
		}
		return instance;
	}

	private synchronized static void init() {
		if (instance == null) {
			instance = new Settings();
		}
	}

	private Settings() {
		load();
	}

	@Override
	protected void load() {
		super.load();

		initSymbolsWaitSpeedRange();
		initWaitTicksRange();
		initMoveLengthRange();
		initPairRange();
		initDrawBit();

	}

	final int[] getSymbolsWaitSpeedRange() {
		return symbolsWaitSpeedRange;
	}

	final int[] getWaitTicksRange() {
		return waitTicksRange;
	}

	final int[] getMoveLengthRange() {
		return moveLengthRange;
	}

	final int[] getPairRange() {
		return pairRange;
	}

	final long getDrawBit() {
		return drawBit;
	}

	@Override
	protected String getMatrixType() {
		return Type.$1999.toString();
	}

	private void initSymbolsWaitSpeedRange() {
		symbolsWaitSpeedRange = parseRange(getProperty("symbols.wait.speed.range"));
	}

	private void initWaitTicksRange() {
		waitTicksRange = parseRange(getProperty("wait.ticks.range"));
	}

	private void initMoveLengthRange() {
		moveLengthRange = parseRange(getProperty("move.length.range"));
	}

	private void initPairRange() {
		pairRange = parseRange(getProperty("pair.range"));
	}

	private void initDrawBit() {
		drawBit = Long.parseLong(getProperty("draw.bit"));
	}

}
