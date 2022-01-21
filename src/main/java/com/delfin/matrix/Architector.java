package com.delfin.matrix;

import java.io.IOException;
import java.io.InputStream;

class Architector {

	private static class ArchitectorSettings extends Settings {

		ArchitectorSettings() {
			ClassLoader cl = Settings.class.getClassLoader();
			InputStream is = null;
			try {
				loadProperties(is, cl);
			} catch (IOException e) {
				throw new MatrixException(e);
			}
		}

		@Override
		protected String getMatrixType() {
			return "";
		}

		String matrixType() {
			return getProperty("matrix.type");
		}

	};

	private static ArchitectorSettings settings = new ArchitectorSettings();

	static Matrix createMatrix() {
		String type = settings.matrixType();
		switch (type.toLowerCase()) {
		case "1999":
			return new com.delfin.matrix.$1999.Matrix();
		case "voluntary":
			return new com.delfin.matrix.voluntary.Matrix();
		default:
			throw new MatrixException("Architector didn't create matrix [" + type + "] yet");
		}
	}

}
