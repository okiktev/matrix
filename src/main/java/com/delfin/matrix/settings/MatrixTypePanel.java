package com.delfin.matrix.settings;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.WEST;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

class MatrixTypePanel extends Panel {

	private static final long serialVersionUID = -1932952205731963811L;

	private static final Logger log = Logger.getLogger("settings");

	private static final Insets INSETS = new Insets(0, 0, 0, 0);
	
	private ImageButton btnVoluntary;
	private ImageButton btn1999;
	private ImageButton btn2021;

	MatrixTypePanel() {

		setLayout(new GridBagLayout());

		try {
			btnVoluntary = new ImageButton("icons/mmv.png", "Matrix Voluntary");
			btn1999 = new ImageButton("icons/mm1.png", "Matrix 1999");
			btn2021 = new ImageButton("icons/mm2.png", "Matrix 2021");

			add(btnVoluntary, new GridBagConstraints(0, 0, 1, 1, 1, 1, NORTH, BOTH, INSETS, 0, 0));
			add(btn1999, new GridBagConstraints(0, 1, 1, 1, 1, 1, WEST, BOTH, INSETS, 0, 0));
			add(btn2021, new GridBagConstraints(0, 2, 1, 1, 1, 1, WEST, BOTH, INSETS, 0, 0));

			switch (Settings.selectedMatrix()) {
			case $1999:
				btn1999.setSelected(true);
				break;
			case $2021:
				btn2021.setSelected(true);
				break;
			case VOLUNTARY:
				btnVoluntary.setSelected(true);
				break;
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, "Unable to create matrix type panel", e);
		}

	}

	Properties getProperties() {
		String matrixTypeProperty = "unknown";
		if (btnVoluntary.isSelected) {
			matrixTypeProperty = "voluntary";
		} else if (btn1999.isSelected) {
			matrixTypeProperty = "1999";
		} else if (btn2021.isSelected) {
			matrixTypeProperty = "2021";
		}
		Properties ret = new Properties();
		ret.put("app.matrix.type", matrixTypeProperty);
		return ret;
	}

}
