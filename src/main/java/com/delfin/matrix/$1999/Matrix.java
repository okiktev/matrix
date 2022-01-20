package com.delfin.matrix.$1999;

import static com.delfin.matrix.Settings.BOT_POSITION;
import static com.delfin.matrix.Settings.DRAW_BIT;
import static com.delfin.matrix.Settings.MID_POSITION;
import static com.delfin.matrix.Settings.TOP_POSITION;
import static com.delfin.matrix.Settings.PAIR_RANGE;
import static com.delfin.matrix.Utils.delay;
import static com.delfin.matrix.Utils.getRandomFrom;
import static com.delfin.matrix.Utils.time;
import static java.util.Arrays.asList;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.delfin.matrix.Settings.Position;

public class Matrix implements com.delfin.matrix.Matrix {

	private static Random random = new Random();

	private List<Line> matrix = new ArrayList<>();

	private volatile boolean isDestroyed;

	private List<Integer> xAllocations = new ArrayList<>();

	@Override
	public void draw(Component canvas) {
		Graphics g = canvas.getGraphics();
		
		Dimension dim = canvas.getSize();
		
		Image img = canvas.createImage(dim.width, dim.height);
		Graphics g2 = img.getGraphics();
		
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, dim.width, dim.height);

		List<Line> accumulator = new ArrayList<>();
		
		while (!isDestroyed && !Thread.interrupted()) {
			int count = -1;
			if (matrix.size() > 0) {
				count = 0;
				for (Iterator<Line> it = matrix.iterator(); it.hasNext();) {
					Line line = it.next();
					if (line.isOutOfScreen(dim)) {
						xAllocations.set(line.x, -1);
						it.remove();
						++count;
					}
				}
			}
			if (count != 0) {
				List<Line> lines = generateLines(dim, count);
				boolean doPair = getRandomFrom(PAIR_RANGE) % 3 == 0;
				if (doPair) {
					if (lines.size() == 1 && accumulator.isEmpty()) {
						accumulator.addAll(lines);
						lines.clear();
					} else {
						lines.addAll(accumulator);
						accumulator.clear();
						xAllocations.set(lines.get(0).x, -1);
						lines.get(0).pairTo(lines.get(1), dim.width);
						xAllocations.set(lines.get(0).x, lines.get(0).x);
					}
				}
				matrix.addAll(lines);
			}

			boolean redraw = false;
			for (int i = 0; i < matrix.size(); ++i) {
				Line line = matrix.get(i);

				long now = System.currentTimeMillis();
				if (now - line.redrawn > line.redrawnSpeed || 
						(!line.stopped() && now - line.drawn > line.drawnSpeed)) {
					redraw = true;
					break;
				}
			}
			if (redraw) {
				for (int i = 0; i < matrix.size(); ++i) {
					matrix.get(i).draw(g2);
				}
				g.drawImage(img, 0, 0, canvas);
			}
			delay(DRAW_BIT);
		}
	}

	@SuppressWarnings("unchecked")
	private List<Line> generateLines(Dimension dim, int lines) {
		int xLimit = dim.width;
		if (xAllocations.isEmpty()) {
			xAllocations = new ArrayList<>(xLimit);
			for (int i = 0; i < xLimit; ++i) {
				xAllocations.add(-1);
			}
		}
		List<Position> positions = new ArrayList<>();
		if (lines == -1) {
			positions.addAll(asList(TOP_POSITION, MID_POSITION, BOT_POSITION));
		} else {
			if (lines == 1) {
				positions.addAll(asList(new Position(1, TOP_POSITION.range)));
			} else if (lines == 2) {
				positions.addAll(asList(new Position(1, TOP_POSITION.range)
						, new Position(1, MID_POSITION.range)));
			} else if (lines >= 3) {
				positions.addAll(asList(new Position(lines / 3, TOP_POSITION.range)
						, new Position(lines / 3, MID_POSITION.range)
						, new Position(lines / 3, BOT_POSITION.range)));
			}
		}

		return (List<Line>) time(t -> {}, () -> {
			return positions.stream()
					.flatMap(p -> Stream.generate(() -> getRandomFrom(p.range))
							.limit(p.lineNumbers)
							.map(y -> {
								int x = random.nextInt(xLimit);
								x = allocate(x, xLimit, true);
								if (x >= xAllocations.size()) {
									x = xAllocations.size() - 1;
								}
								xAllocations.set(x, x);
								return new Line(x, y);
							}))
					.collect(Collectors.toList());
		});
	}

	private int allocate(int x, int xLimit, boolean direction) {
		if (x > xLimit || x < 0) {
			return -2;
		}
		boolean free = true;
		for (int i = x - 10; i < x + 10; ++i) {
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
			int a = allocate(x + 20, xLimit, true);
			if (a != -2) {
				return a;
			}
		}
		int a = allocate(x - 20, xLimit, false);
		return a != -2 ? a : x;
	}

	@Override
	public void destroy() {
		isDestroyed = true;
	}

}
