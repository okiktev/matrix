package com.delfin.matrix;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.delfin.matrix.Utils.*;
import static com.delfin.matrix.Settings.*;
import static java.lang.System.currentTimeMillis;

class Line {

	static final List<Character> CHARS = Chars.getAll();
	private static final Map<Integer, Color> GRADIENTS = new HashMap<>();
	private static final int[] GRADIENT_RANGE = new int[] { 0, MATRIX_DEEP };

	private List<Symbol> data = new ArrayList<>();
	int x;
	private int y;
	private Font font;
	long redrawn = currentTimeMillis();
	long redrawnSpeed;
	long drawn = currentTimeMillis();
	long drawnSpeed;

	private int stopIndex;
	private int drawnIndx;

	private int waitIdx;
	private int waitLim;
	private int moveSpeed;
	private int moveSpeedIdx;

	Line(int x, int y) {
		font = new Font(FONT_NAME, Font.BOLD, getRandomFrom(FONT_SIZE_RANGE));
		redrawnSpeed = getRandomFrom(SYMBOLS_WAIT_SPEED_RANGE);
		drawnSpeed = getRandomFrom(SYMBOLS_RUN_SPEED_RANGE);
		waitLim = getRandomFrom(WAIT_TICKS_RANGE);
		moveSpeed = getRandomFrom(MOVE_SPEED_RANGE);
		this.x = x;
		this.y = y;
		generateData();
		stopIndex = data.size();
	}

	private void generateData() {
		Random random = new Random();
		data = Stream.generate(() -> CHARS.get(random.nextInt(CHARS.size())))
				.limit(getRandomFrom(SYMBOLS_IN_LINE_RANGE))
				.map(Symbol::new)
				.collect(Collectors.toList());
	}

	
	void draw(Graphics g) {
		g.setFont(font);
		
		if (waitIdx == waitLim) {
			moveSpeedIdx++;
			if (moveSpeedIdx == moveSpeed) {
				waitIdx = 0;
			}
			for (int i = 0; i < drawnIndx; ++i) {
				Symbol symbol = data.get(i);
				g.setColor(Color.BLACK);
				g.drawString(symbol.ch, x, y + symbol.offset);
			}
			y += 20;
			for (int i = 0; i < drawnIndx; ++i) {
				if (i == drawnIndx - 1) {
					g.setColor(Color.WHITE);
				} else {
					int gradientIndx = getRandomFrom(GRADIENT_RANGE);
					int gradient = (255 / MATRIX_DEEP) * (gradientIndx + 1) + 1;

					g.setColor(GRADIENTS.computeIfAbsent(gradient, k -> new Color(0, k, 0)));
				}
				Symbol symbol = data.get(i);
				symbol.color = g.getColor();
				g.drawString(symbol.ch, x, y + symbol.offset);
			}
			drawn = currentTimeMillis();
			
			
			return;
		}
		
//		System.out.println("start paint");
//		for (int i = 0; i < drawnIndx; ++i) {
//			Symbol symbol = data.get(i);
//			g.setColor(symbol.color);
//			g.drawString(symbol.ch, x, y + symbol.offset);
//		}

		if (!stopped()) {
			drawFront(g);
		}
		if (currentTimeMillis() - redrawn < redrawnSpeed) {
			return;
		}
//		System.out.println("do paint");
		for (int i = 0; i < drawnIndx; ++i) {
			if (i == drawnIndx - 1 && !stopped()) {
				g.setColor(Color.WHITE);
			} else {
				int gradientIndx = getRandomFrom(GRADIENT_RANGE);
				int gradient = (255 / MATRIX_DEEP) * (gradientIndx + 1) + 1;

				g.setColor(GRADIENTS.computeIfAbsent(gradient, k -> new Color(0, k, 0)));
			}
			Symbol symbol = data.get(i);
			symbol.color = g.getColor();
			g.drawString(symbol.ch, x, y + symbol.offset);
		}
		redrawn = currentTimeMillis();
		
		if (stopped()) {			
			++waitIdx;
		}

	}

	boolean stopped() {
		return redrawnWhite && drawnIndx >= stopIndex;
	}

	private void drawFront(Graphics g) {
		if (stopped() || currentTimeMillis() - drawn < drawnSpeed) {
			return;
		}

		if (drawnIndx != 0) {
			// redraw previous
			Symbol prevSymbol = data.get(drawnIndx - 1);
			g.setColor(prevSymbol.color = GRADIENTS.computeIfAbsent(255, k -> new Color(0, k, 0)));
			g.drawString(prevSymbol.ch, x, y + prevSymbol.offset);
			if (drawnIndx == stopIndex) {
				redrawnWhite = true;
			}
			if (stopped()) {
				drawn = currentTimeMillis();
//				if (drawnIndx == data.size()) {
				return;
			}
		}
		Symbol symbol = data.get(drawnIndx);
		g.setColor(symbol.color = Color.WHITE);
		String ch = symbol.ch;
		int offset = (int) g.getFontMetrics().getStringBounds(ch, g).getHeight() * (drawnIndx + 1);
		symbol.offset = offset;
		g.drawString(ch, x, y + offset);

		++drawnIndx;
		drawn = currentTimeMillis();

	}
	
	boolean redrawnWhite;

	private static class Symbol {
		public Color color;
		String ch;
		int offset;

		Symbol(Character ch) {
			this.ch = new String(Character.toChars(ch));
		}
	}

	boolean isOutOfScreen(Dimension dim) {
		return y > dim.height;
	}

}
