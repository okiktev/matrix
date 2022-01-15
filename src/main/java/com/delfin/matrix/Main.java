package com.delfin.matrix;

import static com.delfin.matrix.Settings.DRAW_BIT;
import static com.delfin.matrix.Utils.delay;
import static com.delfin.matrix.Utils.getRandomFrom;
import static com.delfin.matrix.Utils.time;

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
import java.util.Arrays;
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

		long prevRedraw = System.currentTimeMillis();

		int idxRedrawRange[] = {0, 2};
		
		while (!isClosed) {

			if (matrix.size() <= 0) {
				List<Line> lines = generateLines(dim, -1);
				matrix.addAll(lines);
			} else {
				int count = 0;
				for (Iterator<Line> it = matrix.iterator(); it.hasNext();) {
					Line line = it.next();
					if (line.isOutOfScreen(dim)) {
						it.remove();
						++count;
					}
				}
				matrix.addAll(generateLines(dim, count));
			}

			int idxRedraw = Utils.getRandomFrom(idxRedrawRange) + 1;
			
			boolean redraw = false;
			// System.out.println("$ sdfsdaf" + System.currentTimeMillis());
			for (int i = 0; i < matrix.size(); ++i) {
				Line line = matrix.get(i);
//				// if (idxRedraw % 2 == 0) {
//					if (line.stopped() && i % idxRedraw == 0) {
//					continue;
//				}

				long now = System.currentTimeMillis();
				if (now - line.redrawn > line.redrawnSpeed || (!line.stopped() && now - line.drawn > line.drawnSpeed)) {
//					System.out.println((now - line.redrawn) + "_ " + line.redrawnSpeed + ":" + (now - line.drawn) + "_"
//							+ line.drawnSpeed);

					redraw = true;
					break;
				}
			}
			if (redraw) {
				//System.out.println("$ " + (System.currentTimeMillis() - prevRedraw));
				prevRedraw = System.currentTimeMillis();

//				g2.setColor(Color.BLACK);
//				g2.fillRect(0, 0, dim.width, dim.height);
				
				for (int i = 0; i < matrix.size(); ++i) {
					Line line = matrix.get(i);
//					if (idxRedraw % 2 == 0) {
//						if (line.stopped() && i % idxRedraw == 0) {
//						// System.out.println("redraw 2 " );
//						continue;
//					}
					
					line.draw(g2);
				}
				g.drawImage(img, 0, 0, canvas);
			}
			delay(DRAW_BIT);
		}
	}

	@SuppressWarnings("unchecked")
	private static List<Line> generateLines(Dimension dim, int lines) {
		int xLimit = dim.width;
		List<Position> positions = new ArrayList<>();
		if (lines == -1) {
			positions.addAll(Arrays.asList(Settings.TOP_POSITION, Settings.MID_POSITION, Settings.BOT_POSITION));
		} else {
			if (lines == 1) {
				positions.addAll(Arrays.asList(new Position(1, Settings.TOP_POSITION.range)));
			} else if (lines == 2) {
				positions.addAll(Arrays.asList(new Position(1, Settings.TOP_POSITION.range)
						, new Position(1, Settings.MID_POSITION.range)));
			} else if (lines >= 3) {
				positions.addAll(Arrays.asList(new Position(lines / 3, Settings.TOP_POSITION.range)
						, new Position(lines / 3, Settings.MID_POSITION.range)
						, new Position(lines / 3, Settings.BOT_POSITION.range)));
			}

		}
		
		return (List<Line>) time(t -> {
		}, () -> {
			return positions.stream()
//					return Arrays.asList(new Position(70, new int[] { 20, 50 })).stream()
			//return Arrays.asList(new Position(1, new int[] { 20, 50 })).stream()
//			return Arrays.asList(Settings.TOP_POSITION, Settings.MID_POSITION, Settings.BOT_POSITION).stream()
					.flatMap(p -> Stream.generate(() -> getRandomFrom(p.range)).limit(p.lineNumbers)
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
				executor.shutdown();
				dispose();
			}
		});

		URL u = Main.class.getClassLoader().getResource("icons/matrix.png");
		Image icon = Toolkit.getDefaultToolkit().getImage(u);
		setIconImage(icon);

		setTitle("matrix");

		setExtendedState(MAXIMIZED_BOTH);

		pack();

		System.out.println(getSize());
	}

}
