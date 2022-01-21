package com.delfin.matrix.voluntary;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.delfin.matrix.Utils;

public class Matrix implements com.delfin.matrix.Matrix {

	private static Random random = new Random();

	private static LinkedList<List<Line>> matrix = new LinkedList<>();

	private volatile boolean isDestroyed;

	@Override
	public void draw(Component canvas) {
		Graphics g = canvas.getGraphics();

		Dimension dim = canvas.getSize();

		Image img = canvas.createImage(dim.width, dim.height);
		Graphics g2 = img.getGraphics();

		ExecutorService executor = Executors.newCachedThreadPool();

		try {
			while (!isDestroyed && !Thread.interrupted()) {
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, dim.width, dim.height);

				Future<List<Line>> future = executor.submit(new Callable<List<Line>>() {
					@Override
					public List<Line> call() throws Exception {
						return generateLines(dim);
					}
				});

				for (int i = 0; i < matrix.size(); ++i) {
					int gradient = (256 / matrix.size()) * (i + 1) + 1;
					if (gradient > 255) {
						gradient = 250;
					}
					g2.setColor(new Color(0, gradient, 0));
					matrix.get(i).stream().forEach(l -> l.prePaint(g2));
				}
				g.drawImage(img, 0, 0, canvas);

				List<Line> lines = future.get();
				g.setColor(new Color(0, 255, 0));

				executor.invokeAll(lines.stream().map(l -> new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						l.paint(g);
						return null;
					}
				}).collect(Collectors.toList()));

				if (matrix.size() > Settings.MATRIX_DEEP) {
					matrix.removeFirst();
				}
				matrix.add(lines);
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		executor.shutdown();

	}

	@SuppressWarnings("unchecked")
	private static List<Line> generateLines(Dimension dim) {
		int xLimit = dim.width;
		return (List<Line>) Utils.time(t -> {
		}, () -> {
			return Arrays.asList(Settings.TOP_POSITION, Settings.MID_POSITION, Settings.BOT_POSITION).stream()
					.flatMap(p -> Stream.generate(() -> Utils.getRandomFrom(p.range)).limit(p.lineNumbers)
							.map(y -> new Line(random.nextInt(xLimit), y)))
					.collect(Collectors.toList());
		});
	}

	@Override
	public void destroy() {
		isDestroyed = true;
	}

}
