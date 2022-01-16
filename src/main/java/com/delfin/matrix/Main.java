package com.delfin.matrix;

import static com.delfin.matrix.Settings.*;
import static com.delfin.matrix.Utils.delay;
import static com.delfin.matrix.Utils.getRandomFrom;
import static com.delfin.matrix.Utils.time;
import static java.util.Arrays.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.delfin.matrix.Settings.Position;

public class Main extends Frame {

	private static volatile boolean isClosed = false;

	private static Random random = new Random();

	private static Component canvas = new Component() {
		@Override
		public void paint(Graphics g) {

		}
	};

	private static List<Line> matrix = new ArrayList<>();

	static ExecutorService executor = Executors.newScheduledThreadPool(1);

	public static void main(String argv[]) throws InterruptedException, ExecutionException {
		Main window = new Main();
		window.setVisible(true);
		// to maximize window
		delay(100);

		executor.submit(() -> {

			try {
				drawMatrix(null);
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}

		});

	}

	private static void drawMatrix(ExecutorService executor) throws InterruptedException, ExecutionException {
		Graphics g = canvas.getGraphics();
		Dimension dim = canvas.getSize();
		
		Image img = canvas.createImage(dim.width, dim.height);
		Graphics g2 = img.getGraphics();
		
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, dim.width, dim.height);

		while (!isClosed) {

			if (matrix.size() <= 0) {
				List<Line> lines = generateLines(dim, -1);
				matrix.addAll(lines);
			} else {
				int count = 0;
				for (Iterator<Line> it = matrix.iterator(); it.hasNext();) {
					Line line = it.next();
					if (line.isOutOfScreen(dim)) {
						xAllocations.set(line.x, -1);
						it.remove();
						++count;
					}
				}
				matrix.addAll(generateLines(dim, count));
			}
//			System.out.println(xAllocations);

			boolean redraw = false;
			for (int i = 0; i < matrix.size(); ++i) {
				Line line = matrix.get(i);

				long now = System.currentTimeMillis();
				if (now - line.redrawn > line.redrawnSpeed || (!line.stopped() && now - line.drawn > line.drawnSpeed)) {
					redraw = true;
					break;
				}
			}
			if (redraw) {
				
				for (int i = 0; i < matrix.size(); ++i) {
					Line line = matrix.get(i);
					
					line.draw(g2);
				}
				g.drawImage(img, 0, 0, canvas);
			}
			//System.out.println("$$ bit " + System.currentTimeMillis());
			delay(DRAW_BIT);
		}
		// System.out.println("$$ closed " + System.currentTimeMillis());
	}

	
	static List<Integer> xAllocations = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	private static List<Line> generateLines(Dimension dim, int lines) {
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
								xAllocations.set(x, x);
								return new Line(x, y);
							}))
					.collect(Collectors.toList());
		});
	}

	private static int allocate(int x, int xLimit, boolean direction) {
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
		if (a != -2) {
			return a;
		}
		return x;
	}

	Main() {
		add(canvas);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isClosed = true;
				executor.shutdown();
				dispose();
			}
		});

		URL u = Main.class.getClassLoader().getResource("icons/matrix.png");
		Image icon = Toolkit.getDefaultToolkit().getImage(u);
		setIconImage(icon);

		setTitle("matrix");

		setExtendedState(MAXIMIZED_BOTH);
		setPreferredSize(new Dimension(500, 500));

		pack();

	}

}
