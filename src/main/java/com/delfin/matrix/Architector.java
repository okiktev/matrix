package com.delfin.matrix;

import java.io.IOException;

import com.delfin.matrix.settings.Settings;

class Architector {

	static {
		new Settings() {
			{
				try {
					loadProperties();
				} catch (IOException e) {
					throw new MatrixException(e);
				}
			}

			@Override
			protected String getMatrixType() {
				return "";
			}

		};
	}

	static Matrix createMatrix() {
		Matrix.Type type = Settings.selectedMatrix();
		switch (type) {
		case $1999:
			return new com.delfin.matrix.$1999.Matrix();
		case $2021:
			return new com.delfin.matrix.$2021.Matrix();
		case VOLUNTARY:
			return new com.delfin.matrix.voluntary.Matrix();
		default:
			throw new MatrixException("Architector didn't create matrix [" + type + "] yet");
		}
	}

}
