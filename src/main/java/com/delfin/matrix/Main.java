package com.delfin.matrix;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main extends Frame {

	private static volatile boolean isClosed = false;

	private static Random random = new Random();

	private static Component canvas = new Component() {
		@Override
		public void paint(Graphics g) {

		}
	};

	private static LinkedList<List<Line>> matrix = new LinkedList<>();

	public static void main(String argv[]) throws InterruptedException, ExecutionException {
		Main window = new Main();
		window.setVisible(true);
		ExecutorService executor = Executors.newCachedThreadPool();
		while (!isClosed) {
			drawMatrix(executor);
		}
		executor.shutdown();
		window.dispose();
	}

	private static void drawMatrix(ExecutorService executor) throws InterruptedException, ExecutionException {

		Dimension dim = canvas.getSize();

		Future<List<Line>> future = executor.submit(new Callable<List<Line>>() {
			@Override
			public List<Line> call() throws Exception {
				return generateLines(dim);
			}
		});

		Graphics g = canvas.getGraphics();

		Font f = new Font(Settings.FONT_NAME, Font.BOLD, 40);
		g.setFont(f);

		Image img = canvas.createImage(dim.width, dim.height);
		Graphics g2 = img.getGraphics();
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, dim.width, dim.height);
		g2.setFont(f);

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

	@SuppressWarnings("unchecked")
	private static List<Line> generateLines(Dimension dim) {
		int xLimit = dim.width;
		return (List<Line>) Utils.time(t -> {}, () -> {
			return Arrays.asList(Settings.TOP_POSITION, Settings.MID_POSITION, Settings.BOT_POSITION).stream()
					.flatMap(p -> Stream.generate(() -> Utils.getRandomFrom(p.range))
							.limit(p.lineNumbers)
							.map(y -> new Line(random.nextInt(xLimit), y)))
					.collect(Collectors.toList());
		});
	}

	Main() {
		add(canvas);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isClosed = true;
			}
		});

		URL u = Main.class.getClassLoader().getResource("icons/matrix.png");
		Image icon = Toolkit.getDefaultToolkit().getImage(u);
		setIconImage(icon);

		setTitle("matrix");

		setExtendedState(MAXIMIZED_BOTH);
		
		pack();
	}

}
