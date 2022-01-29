package com.delfin.matrix.$2021;

import com.delfin.matrix.Matrix.Type;

class Settings extends com.delfin.matrix.settings.Settings {

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

		initDrawBit();
	}

	long getDrawBit() {
		return drawBit;
	}

	@Override
	protected String getMatrixType() {
		return Type.$2021.toString();
	}

	private void initDrawBit() {
		drawBit = Long.parseLong(getProperty("draw.bit"));
	}

}
