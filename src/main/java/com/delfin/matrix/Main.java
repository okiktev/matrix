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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.delfin.matrix.Settings.Position;

import static com.delfin.matrix.Utils.*;
import static com.delfin.matrix.Settings.*;

public class Main extends Frame {

	private static volatile boolean isClosed = false;

	private static Random random = new Random();

	private static Component canvas = new Component() {
		@Override
		public void paint(Graphics g) {

		}
	};

//	private static LinkedList<List<Line>> matrix = new LinkedList<>();
	private static List<Line> matrix = new ArrayList<>();

	public static void main(String argv[]) throws InterruptedException, ExecutionException {
		Main window = new Main();
		window.setVisible(true);
		//window.show();
		delay(100);
		
		
		//  ExecutorService executor = Executors.newWorkStealingPool(5);
		// ExecutorService executor = Executors.newWorkStealingPool(5);
//		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//		executor.schedule(() -> {
//			while (!isClosed) {
////				time(t -> System.out.println("$$$ ++++++++ drawn for " + t), () -> {
//						try {
//							drawMatrix(executor);
//						} catch (Exception e) {
//							throw new RuntimeException(e);
//						}
////						return true;
////					}
////				);
//			}
//		}, 50, TimeUnit.MILLISECONDS); 
		
		while (!isClosed) {
//			time(t -> System.out.println("$$$ ++++++++ drawn for " + t), () -> {
					try {
						drawMatrix(null);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
//					return true;
//				}
//			);
		}
		 // executor.shutdown();
		 window.dispose();
	}

	private static void drawMatrix(ExecutorService executor) throws InterruptedException, ExecutionException {

		Dimension dim = canvas.getSize();

//		Future<List<Line>> future = executor.submit(new Callable<List<Line>>() {
//			@Override
//			public List<Line> call() throws Exception {
//				return generateLines(dim);
//			}
//		});
		
		if (matrix.size() <= 0 ) {		
			
			List<Line> lines = generateLines(dim);
			matrix.addAll(lines);
		}

		Graphics g = canvas.getGraphics();

		Font f = new Font(FONT_NAME, Font.BOLD, 40);
		g.setFont(f);

//		time(t -> System.out.println("$$$ redraw for " + t), () -> {
			Image img = canvas.createImage(dim.width, dim.height);
			Graphics g2 = img.getGraphics();
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, dim.width, dim.height);
			g2.setFont(f);
			
			matrix.stream().forEach(l -> l.prePaint(g2));
			
//			for (int i = 0; i < matrix.size(); ++i) {
//				matrix.get(i).prePaint(g2);
//			}
			g.drawImage(img, 0, 0, canvas);
//			return true;
//		});


		
		// lines.add(new Line(50, 50));
//		Utils.time(t -> System.out.println("$$$ generate for " + t), () -> {
//			try {
//				lines.addAll(future.get());
//			} catch (Exception e) {
//				throw new RuntimeException();
//			}
//			return true;
//		});
		//g.setColor(new Color(0, 255, 0));

	// 	g.drawOval(100, 100, 100, 100);
		
//		time(t -> System.out.println("$$$ run for " + t), () -> {
//			matrix.stream().forEach(l -> {
//				executor.submit(() -> l.paint(g));
//			});
//			return true;
//		});
		
//		delay(10);
		delay(DRAW_BIT);
//		if (matrix.size() > Settings.MATRIX_DEEP) {
//			matrix.removeFirst();
//		}

		//System.out.println("tick " + new Date().getTime());
	}

	@SuppressWarnings("unchecked")
	private static List<Line> generateLines(Dimension dim) {
		int xLimit = dim.width;
		return (List<Line>) time(t -> {}, () -> {
			return Arrays.asList(new Position(25, new int[] { 20, 50 })).stream()
					.flatMap(p -> Stream.generate(() -> getRandomFrom(p.range))
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
		
	//	setMinimumSize(new Dimension(1800, 700));
		
		pack();
		
		System.out.println(getSize());
	}

}
