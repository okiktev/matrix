package com.delfin.matrix;

import static com.delfin.matrix.Utils.delay;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	private static final Logger log = Logger.getLogger("main");

	public static void main(String argv[]) {
		try {
			Window window = new Window();
			window.setVisible(true);
			// to maximize window
			delay(100);
			window.draw();
		} catch (Exception e) {
			log.log(Level.SEVERE, "One has broken matrix", e);
		}

	}

}
