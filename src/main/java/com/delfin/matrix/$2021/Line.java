package com.delfin.matrix.$2021;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.delfin.matrix.Chars;

import static com.delfin.matrix.Utils.*;
import static java.lang.System.currentTimeMillis;

class Line {

	private static Settings settings = Settings.getInstance();

	private static final List<String> CHARS;
	static {
		CHARS = Chars.getAll().stream().map(ch -> new String(Character.toChars(ch))).collect(Collectors.toList());
	}
	private static final Map<Integer, Color> GRADIENTS = new HashMap<>();

	private List<Symbol> data = new ArrayList<>();
	int x;
	private int y;
	private Font font;
	long redrawn = currentTimeMillis();
	long redrawnSpeed;
	long drawn = currentTimeMillis();
	long drawnSpeed;
	private int matrixDeep;
	private int drawnIndx;
	private Integer lineWidth;

	Line(int x, int y) {
		font = new Font(settings.getFontName(), Font.BOLD, getRandomFrom(settings.getFontSizeRange()));
		redrawnSpeed = getRandomFrom(settings.getSymbolsWaitSpeedRange());
		drawnSpeed = getRandomFrom(settings.getSymbolsRunSpeedRange());
		matrixDeep = settings.getMatrixDeep();
		this.x = x;
		this.y = y;
		generateData();
	}

	private void generateData() {
		data = Stream.generate(() -> new Symbol())
				.limit(getRandomFrom(settings.getSymbolsInLineRange()))
				.collect(Collectors.toList());
	}

	void draw(Graphics g) {
		g.setFont(font);
		if (currentTimeMillis() - drawn < drawnSpeed) {
			return;
		}
		if (drawnIndx >= data.size()) {
			for (int i = 0; i < data.size(); ++i) {
				Symbol symbol = data.get(i);
				g.setColor(Color.BLACK);
				g.drawString(symbol.prevCh, x, y + symbol.offset);
			}
			y += 20;
			for (int i = 0; i < data.size(); ++i) {
				g.setColor(i == data.size() - 1 ? Color.WHITE : calcGradient(i));
				Symbol symbol = data.get(i);
				g.drawString(symbol.getChar(), x, y + symbol.offset);
			}
		} else {
			if (drawnIndx != 0) {
				for (int i = 0; i < drawnIndx - 1; ++i) {
					Symbol s = data.get(i);
					g.setColor(Color.BLACK);
					g.drawString(s.prevCh, x, y + s.offset);
					g.setColor(calcGradient(i));
					g.drawString(s.getChar(), x, y + s.offset);
				}
				
				// redraw previous
				Symbol prevSymbol = data.get(drawnIndx - 1);
				String ch = prevSymbol.prevCh;
				if (ch == null) {
					ch = prevSymbol.getChar();
				}
				g.setColor(calcGradient(drawnIndx));
				g.drawString(ch, x, y + prevSymbol.offset);
			}
			
			Symbol symbol = data.get(drawnIndx);
			String ch = symbol.prevCh;
			if (ch == null) {
				ch = symbol.getChar();
			}
			g.setColor(Color.WHITE);
			int offset = (int) (g.getFontMetrics().getStringBounds(ch, g).getHeight() *0.75 * (drawnIndx + 1));
			symbol.offset = offset;
			g.drawString(ch, x, y + offset);
		}

		++drawnIndx;
		drawn = currentTimeMillis();
	}

	private Color calcGradient(int i) {
		int gradient = (200 / matrixDeep) * i;
		if (gradient > 255) {
			gradient = 255;
		}
		return GRADIENTS.computeIfAbsent(gradient, k -> new Color(0, k, 0));
	}

	boolean isOutOfScreen(Dimension dim) {
		return y > dim.height;
	}

	void allocate(List<Integer> xAllocations, Graphics g) {
		if (lineWidth == null) {
			lineWidth = (int) g.getFontMetrics(font).getStringBounds(data.get(0).getChar(), g).getWidth();
		}
		x = allocate(x, lineWidth, true, xAllocations);
		if (x >= xAllocations.size()) {
			x = xAllocations.size() - 1;
		}
		xAllocations.set(x, x);
	}

	private static int allocate(int x, int lineWidth, boolean direction, List<Integer> xAllocations) {
		if (x > xAllocations.size() || x < 0) {
			return -2;
		}
		boolean free = true;
		for (int i = x - lineWidth/2; i < x + lineWidth/2; ++i) {
			if (i < 0 || i >= xAllocations.size()) {
				continue;
			}
			int j = xAllocations.get(i);
			if (j != -1) {
				free = false;
				break;
			}
		}
		if (free) {
			return x;
		}
		if (direction) {
			int a = allocate(x + lineWidth/2 + 1, lineWidth, true, xAllocations);
			if (a != -2) {
				return a;
			}
		}
		int a = allocate(x - lineWidth/2 - 1, lineWidth, false, xAllocations);
		return a != -2 ? a : x;
	}

	private static class Symbol {
		int offset;
		String prevCh;
		
		String getChar() {
			return prevCh = CHARS.get(getRandomFrom(CHARS.size()));
		}
	}

}
