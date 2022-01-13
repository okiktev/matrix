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
				throw new RuntimeException(e);
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
				List<Line> lines = generateLines(dim);
				matrix.addAll(lines);
			}

			int idxRedraw = Utils.getRandomFrom(idxRedrawRange);
			
			boolean redraw = false;
			for (int i = 0; i < matrix.size(); ++i) {
				if (idxRedraw % 2 == 0) {
					continue;
				}
				Line line = matrix.get(i);

				long now = System.currentTimeMillis();
				if (now - line.redrawn > line.redrawnSpeed || (!line.stopped() && now - line.drawn > line.drawnSpeed)) {
//					System.out.println((now - line.redrawn) + "_ " + line.redrawnSpeed + ":" + (now - line.drawn) + "_"
//							+ line.drawnSpeed);

					redraw = true;
					break;
				}
			}
			if (redraw) {
//				System.out.println("$ " + (System.currentTimeMillis() - prevRedraw));
				prevRedraw = System.currentTimeMillis();

//				g2.setColor(Color.BLACK);
//				g2.fillRect(0, 0, dim.width, dim.height);
				for (int i = 0; i < matrix.size(); ++i) {
					if (idxRedraw % 2 == 0) {
						continue;
					}
					
					matrix.get(i).prePaint(g2);
				}
				g.drawImage(img, 0, 0, canvas);
			}
			delay(DRAW_BIT);
		}
	}

	@SuppressWarnings("unchecked")
	private static List<Line> generateLines(Dimension dim) {
		int xLimit = dim.width;
		return (List<Line>) time(t -> {
		}, () -> {
			return Arrays.asList(new Position(70, new int[] { 20, 50 })).stream()
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
