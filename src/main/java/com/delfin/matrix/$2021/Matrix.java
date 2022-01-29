package com.delfin.matrix.$2021;

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
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.delfin.matrix.settings.Settings.Position;

public class Matrix implements com.delfin.matrix.Matrix {

	private static Random random = new Random();

	private List<Line> matrix = new ArrayList<>();
	private volatile boolean isDestroyed;
	private List<Integer> xAllocations = new ArrayList<>();

	private Position topPosition;
	private Position midPosition;
	private Position botPosition;

	@Override
	public void draw(Component canvas) {
		Settings settings = Settings.getInstance();

		Graphics g = canvas.getGraphics();

		Dimension dim = canvas.getSize();

		Image img = canvas.createImage(dim.width, dim.height);
		Graphics g2 = img.getGraphics();

		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, dim.width, dim.height);

		long drawBit = settings.getDrawBit();
		topPosition = settings.getTopPosition();
		midPosition = settings.getMidPosition();
		botPosition = settings.getBotPosition();

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
				matrix.addAll(generateLines(dim, count, g));
			}

			boolean redraw = false;
			for (int i = 0; i < matrix.size(); ++i) {
				Line line = matrix.get(i);

				long now = System.currentTimeMillis();
				if (now - line.drawn > line.drawnSpeed) {
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
			delay(drawBit);
		}
	}

	private List<Line> generateLines(Dimension dim, int lines, Graphics g) {
		int xLimit = dim.width;
		if (xAllocations.isEmpty()) {
			xAllocations = new ArrayList<>(xLimit);
			for (int i = 0; i < xLimit; ++i) {
				xAllocations.add(-1);
			}
		}
		List<Position> positions = new ArrayList<>();
		if (lines == -1) {
			positions.addAll(asList(topPosition, midPosition, botPosition));
		} else {
			if (lines == 1) {
				positions.addAll(asList(new Position(1, topPosition.range)));
			} else if (lines == 2) {
				positions.addAll(asList(new Position(1, topPosition.range), new Position(1, midPosition.range)));
			} else if (lines >= 3) {
				positions.addAll(asList(new Position(lines / 3, topPosition.range),
						new Position(lines / 3, midPosition.range), new Position(lines / 3, botPosition.range)));
			}
		}

		return time(t -> {
		}, () -> {
			return positions.stream()
					.flatMap(p -> Stream.generate(() -> getRandomFrom(p.range)).limit(p.lineNumbers).map(y -> {
						Line line = new Line(random.nextInt(xLimit), y);
						line.allocate(xAllocations, g);
						return line;
					})).collect(Collectors.toList());
		});
	}

	@Override
	public void destroy() {
		isDestroyed = true;
	}

	public static Properties settings(Properties properties) {
		Settings settings = Settings.getInstance();
		if (properties != null) {
			settings.getProperties().putAll(properties);
		}
		return settings.getProperties();
	}

}
