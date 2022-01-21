package com.delfin.matrix.$1999;

class Settings extends com.delfin.matrix.Settings {

	private int[] symbolsWaitSpeedRange;
	private int[] waitTicksRange;
	private int[] moveLengthRange;
	private int[] pairRange;
	private long drawBit;

	private static Settings instance;

	public static Settings getInstance() {
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
		initWaitTicksRange();
		initMoveLengthRange();
		initPairRange();
		initDrawBit();

	}

	public final int[] getSymbolsWaitSpeedRange() {
		return symbolsWaitSpeedRange;
	}

	public final int[] getWaitTicksRange() {
		return waitTicksRange;
	}

	public final int[] getMoveLengthRange() {
		return moveLengthRange;
	}

	public final int[] getPairRange() {
		return pairRange;
	}

	public final long getDrawBit() {
		return drawBit;
	}

	@Override
	protected String getMatrixType() {
		return "1999";
	}

	private void initSymbolsWaitSpeedRange() {
		symbolsWaitSpeedRange = parseRange(getProperty("symbol.wait.speed.range"));
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
