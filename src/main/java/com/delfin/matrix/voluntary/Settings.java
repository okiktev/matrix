package com.delfin.matrix.voluntary;

import com.delfin.matrix.Matrix.Type;

class Settings extends com.delfin.matrix.settings.Settings {

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
		super.load();
	}

	@Override
	protected String getMatrixType() {
		return Type.VOLUNTARY.toString();
	}

}
