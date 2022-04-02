package com.delfin.matrix.settings;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.delfin.matrix.Matrix;
import com.delfin.matrix.MatrixException;

public abstract class Settings {

	private static final Logger log = Logger.getLogger("settings");
	private static final String CFG_FILE_NAME = "matrix.conf";
	private static final ClassLoader CLASS_LOADER = Settings.class.getClassLoader();

	protected static Properties properties = new Properties();

	protected String fontName;
	protected int[] fontSizeRange;
	protected int[] symbolsInLineRange;
	protected int[] symbolsRunSpeedRange;
	protected int matrixDeep;
	protected Position topPosition;
	protected Position midPosition;
	protected Position botPosition;
	private boolean isShowMatrixHasYouScreen;

	protected static volatile boolean doReload;

	public Properties getProperties() {
		return properties;
	}

	protected void load() {
		try {
			loadProperties();

			initFontName();
			initFontSizeRange();
			initSymbolsInLineRange();
			initSymbolsRunSpeedRange();
			initMatrixDeep();
			initTopPosition();
			initMidPosition();
			initBotPosition();
			initShowMatrixHasYouScreen();

			doReload = false;
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unable load settings", e);
			throw new MatrixException(e);
		}
	}

	@SuppressWarnings("resource")
	protected void loadProperties() throws IOException {
		if (!properties.isEmpty()) {
			return;
		}
		InputStream is;
		try {
			log.info("Loading properties from local");
			is = new FileInputStream(CFG_FILE_NAME);
		} catch (FileNotFoundException e) {
			log.log(Level.WARNING, "Unable to load config from local. Loading defaults...");
			is = CLASS_LOADER.getResourceAsStream(CFG_FILE_NAME);
		}
		try {
			properties.load(is);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				log.log(Level.WARNING, "Unable to close stream to config file", e);
			}
		}
	}

	static void loadDefaults() throws IOException {
		properties.clear();
		properties.load(Settings.class.getClassLoader().getResourceAsStream(CFG_FILE_NAME));
	}

	static void setAndSave(Properties props) throws IOException {
		properties.forEach((k, v) -> {
			if (!props.keySet().contains(k)) {
				props.put(k,  v);
			}
		});
		properties.clear();
		properties.putAll(props);
		try (OutputStream stream = new FileOutputStream(CFG_FILE_NAME)) {			
			properties.store(stream, "Matrix properties");
		}
		doReload = true;
	}

	public static Matrix.Type selectedMatrix() {
		String type = properties.getProperty("app.matrix.type");
		switch (type.toLowerCase()) {
		case "1999":
			return Matrix.Type.$1999;
		case "2021":
			return Matrix.Type.$2021;
		case "voluntary":
			return Matrix.Type.VOLUNTARY;
		default:
			throw new MatrixException("Architector didn't create matrix [" + type + "] yet");
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

	public boolean isShowMatrixHasYouScreen() {
		return isShowMatrixHasYouScreen;
	}

	private void initFontName() throws FontFormatException, IOException {
		fontName = getProperty("font.name");
		InputStream is = CLASS_LOADER.getResourceAsStream("fonts/" + fontName + ".ttf");
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

	private void initShowMatrixHasYouScreen() {
		String propValue = getProperty("show.matrix.has.you");
		isShowMatrixHasYouScreen = Boolean.parseBoolean(propValue);
	}

	protected int[] parseRange(String propValue) {
		String[] range = propValue.trim().split(",");
		return new int[] { Integer.parseInt(range[0]), Integer.parseInt(range[1]) };
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
