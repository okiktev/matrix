package com.delfin.matrix.voluntary;

class Settings extends com.delfin.matrix.Settings {

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

	@Override
	protected String getMatrixType() {
		return "voluntary";
	}

}
