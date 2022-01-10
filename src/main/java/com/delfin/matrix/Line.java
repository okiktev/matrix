package com.delfin.matrix;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Line {

	static final List<Character> CHARS = Chars.getAll();

	private List<Symbol> data = new ArrayList<>();
	private int x;
	private int y;
	private Font font;

	Line(int x, int y) {
		font = new Font(Settings.FONT_NAME, Font.BOLD, Utils.getRandomFrom(Settings.FONT_SIZE_RANGE));

		this.x = x;
		this.y = y;
		generateData();
	}

	private void generateData() {
		Random random = new Random();
		data = Stream.generate(() -> CHARS.get(random.nextInt(CHARS.size())))
				.limit(Utils.getRandomFrom(Settings.SYMBOLS_IN_LINE_RANGE))
				.map(Symbol::new)
				.collect(Collectors.toList());
	}

	void paint(Graphics g) {
		long speed = Utils.getRandomFrom(Settings.SYMBOLS_SPEED_RANGE);
		int offset = 0;
		for (int i = 0; i < data.size(); ++i) {
			synchronized (g) {
				g.setFont(font);
				if (i != 0) {
					// redraw previous
					Symbol prev = data.get(i - 1);
					g.setColor(new Color(0, 255, 0));
					String ch = new String(Character.toChars(prev.ch));
					g.drawString(ch, x, y + prev.offset);
					if (i == data.size() - 1) {
						continue;
					}
				}
				g.setColor(Color.WHITE);
				String ch = new String(Character.toChars(data.get(i).ch));
				offset += (int) g.getFontMetrics().getStringBounds(ch, g).getHeight();
				data.get(i).offset = offset;
				g.drawString(ch, x, y + offset);
			}
			Utils.delay(speed);
		}

	}

	void prePaint(Graphics g) {
		g.setFont(font);
		for (Symbol s : data) {
			g.drawString(new String(Character.toChars(s.ch)), x, y + s.offset);
		}
	}

	private static class Symbol {
		Character ch;
		int offset;
		Symbol(Character ch) {
			this.ch = ch;
		}
	}

}
