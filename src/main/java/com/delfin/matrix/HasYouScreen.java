package com.delfin.matrix;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class HasYouScreen {

	private static final Font FONT = new Font("Courier New", Font.BOLD, 28);
	private static final Color COLOR = new Color(128, 255, 255);
	private static final List<String> data = new ArrayList<>();
	static {
		data.add("Wake up, Neo...");
		data.add("The Matrix has you...");
		data.add("Follow the white rabbit.");
		data.add("Knock, knock, Neo.");
	}

	private static int x = 10;
	private static int y = 100;

	public static void draw(Graphics g, Dimension dim) {
		Font prevFont = g.getFont();
		Color prevColor = g.getColor();

		g.setFont(FONT);
		for (int i = 0; i < data.size(); ++i) {
			clr(g, dim);
			int offset = 0;
			String line = data.get(i);
			if (i == data.size() - 1) {
				offset = (int) g.getFontMetrics().getStringBounds(Character.toString(line.charAt(0)), g).getWidth();
				g.drawString(line, x + offset, y);
			} else {
				for (char c : line.toCharArray()) {
					String ch = Character.toString(c);
					offset += (int) g.getFontMetrics().getStringBounds(ch, g).getWidth();
					g.drawString(ch, x + offset, y);
					Utils.delay(150);
				}
			}
			Utils.delay(2000);
		}

		g.setFont(prevFont);
		g.setColor(prevColor);
	}

	private static void clr(Graphics g, Dimension dim) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, dim.width, dim.height);
		g.setColor(COLOR);
	}

}
