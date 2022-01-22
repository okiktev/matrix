package com.delfin.matrix.$2021;

class Settings extends com.delfin.matrix.Settings {

	private int[] symbolsWaitSpeedRange;
	private long drawBit;

	private static Settings instance;

	static Settings getInstance() {
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

	protected Settings() {
		load();
	}

	@Override
	protected void load() {
		super.load();

		initSymbolsWaitSpeedRange();
		initDrawBit();
	}

	int[] getSymbolsWaitSpeedRange() {
		return symbolsWaitSpeedRange;
	}

	long getDrawBit() {
		return drawBit;
	}

	@Override
	protected String getMatrixType() {
		return "2021";
	}

	private void initSymbolsWaitSpeedRange() {
		symbolsWaitSpeedRange = parseRange(getProperty("symbols.wait.speed.range"));
	}

	private void initDrawBit() {
		drawBit = Long.parseLong(getProperty("draw.bit"));
	}

}
