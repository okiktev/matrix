package com.delfin.matrix;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Settings {

	private static final Logger log = Logger.getLogger("settings");

	protected static Properties properties = new Properties();

	protected String fontName;
	protected int[] fontSizeRange;
	protected int[] symbolsInLineRange;
	protected int[] symbolsRunSpeedRange;
	protected int matrixDeep;
	protected Position topPosition;
	protected Position midPosition;
	protected Position botPosition;

	protected void load() {

		ClassLoader cl = Settings.class.getClassLoader();
		InputStream is = null;
		try {
			loadProperties(is, cl);

			initFontName(cl);
			initFontSizeRange();
			initSymbolsInLineRange();
			initSymbolsRunSpeedRange();
			initMatrixDeep();
			initTopPosition();
			initMidPosition();
			initBotPosition();

		} catch (Exception e) {
			log.log(Level.SEVERE, "Unable load settings", e);
			throw new MatrixException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.log(Level.WARNING, "Unable to close stream", e);
				}
			}
		}
	}

	protected void loadProperties(InputStream is, ClassLoader cl) throws IOException {
		if (!properties.isEmpty()) {
			return;
		}
		String cfgFile = "matrix.conf";
		try {
			is = new FileInputStream(cfgFile);
			log.info("Loading properties from local");
		} catch (FileNotFoundException e) {
			log.log(Level.WARNING, "Unable to load config from local. Loading defaults...");
			is = cl.getResourceAsStream(cfgFile);
		}
		properties.load(is);
		try {
			is.close();
		} catch (IOException e) {
			log.log(Level.WARNING, "Unable to close stream to config file", e);
		}
	}

	public final String getFontName() {
		return fontName;
	}

	public final int[] getFontSizeRange() {
		return fontSizeRange;
	}

	public final int[] getSymbolsInLineRange() {
		return symbolsInLineRange;
	}

	public final int[] getSymbolsRunSpeedRange() {
		return symbolsRunSpeedRange;
	}

	public final int getMatrixDeep() {
		return matrixDeep;
	}

	public final Position getTopPosition() {
		return topPosition;
	}

	public final Position getMidPosition() {
		return midPosition;
	}

	public final Position getBotPosition() {
		return botPosition;
	}

	private void initFontName(ClassLoader cl) throws FontFormatException, IOException {
		fontName = getProperty("font.name");
		InputStream is = cl.getResourceAsStream("fonts/" + fontName + ".ttf");
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, is));
	}

	private void initFontSizeRange() {
		fontSizeRange = parseRange(getProperty("font.size.range"));
	}

	private void initSymbolsInLineRange() {
		symbolsInLineRange = parseRange(getProperty("symbols.in.line.range"));
	}

	private void initSymbolsRunSpeedRange() {
		symbolsRunSpeedRange = parseRange(getProperty("symbols.run.speed.range"));
	}

	private void initMatrixDeep() {
		matrixDeep = Integer.parseInt(getProperty("matrix.deep"));
	}

	private void initTopPosition() {
		String propValue = getProperty("top.position");
		String[] position = propValue.split(";");
		topPosition = new Position(Integer.parseInt(position[0]), parseRange(position[1]));
	}

	private void initMidPosition() {
		String propValue = getProperty("mid.position");
		String[] position = propValue.split(";");
		midPosition = new Position(Integer.parseInt(position[0]), parseRange(position[1]));
	}

	private void initBotPosition() {
		String propValue = getProperty("bot.position");
		String[] position = propValue.split(";");
		botPosition = new Position(Integer.parseInt(position[0]), parseRange(position[1]));
	}

	protected int[] parseRange(String propValue) {
		String[] range = propValue.trim().split(",");
		return new int[] { Integer.parseInt(range[0]), Integer.parseInt(range[1])};
	}

	protected String getProperty(String propName) {
		 String prop = properties.getProperty(getMatrixType() + '.' + propName);
		 if (prop == null || prop.isEmpty()) {
			 prop = properties.getProperty(propName);
		 }
		 if (prop == null || prop.isEmpty()) {
			 throw new MatrixException("Unable to load property " + propName);
		 }
		 return prop;
	}

	protected abstract String getMatrixType();

	public static class Position {
		public int lineNumbers;
		public int[] range;

		public Position(int lineNumbers, int[] range) {
			this.lineNumbers = lineNumbers;
			this.range = range;
		}
	}

}
