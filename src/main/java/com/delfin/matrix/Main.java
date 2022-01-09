package com.delfin.matrix;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class Main extends Frame {

	private static volatile boolean isClosed = false;
	
	private static Random random = new Random();

	private static volatile boolean isRepainted = false;

	static Component c = new Component() {
		@Override
		public void paint(Graphics g) {
			// super.paint(g);
			System.out.println("$$ executed");
			isRepainted  = true;
			
			if (!doRepaint) {
				return;
			}
			
//			Graphics2D g2d = (Graphics2D) g.create();
//			g2d.setColor(Color.WHITE);
//			g2d.dispose();
			
		}
	};

	static LinkedList<List<Line>> matrix = new LinkedList<>();

	private static boolean doRepaint;

	
	private static int matrixDeep = 10;
	
	public static void main(String argv[]) throws InterruptedException, ExecutionException {
		c.setBackground(Color.BLACK);
		
		
		
		Main window = new Main();
		window.setVisible(true);
		doRepaint = true;
		ExecutorService executor = Executors.newCachedThreadPool();
		while (!isClosed) {
			if (!isRepainted) {				
				c.repaint();
			}
			isRepainted = false;
			// c.paint(c.getGraphics());

			Dimension dim = c.getSize();
			
			Future<List<Line>> future = executor.submit(new Callable<List<Line>>() {
				@Override
				public List<Line> call() throws Exception {
					return generateLines(dim);
				}
			});
			
			String[] fs = Toolkit.getDefaultToolkit().getFontList();

			 Graphics g = c.getGraphics();
//			Graphics2D g2d = (Graphics2D) g.create();
//			g2d.setColor(Color.WHITE);
			
			// Font f = Font.getFont(Font.SERIF);
			Font f = new Font(Settings.FONT_NAME, Font.BOLD, 40);

			g.setFont(f);

			// Graphics2D g2d = (Graphics2D) g.create();
			//  Graphics g2 = g;
			
            
                Image img =  c.createImage(dim.width, dim.height);   
                Graphics g2 = img.getGraphics();      
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, dim.width, dim.height) ;                           
                g2.setFont(f);
            /////////////////////
            // Paint Offscreen //
            /////////////////////
            // renderOffScreen(g2);
            
			
			
			
			for (int i = 0; i < matrix.size(); ++i) {
//				if (i == 0 && matrix.size() == matrixDeep + 1) {
//					g.setColor(Color.WHITE);
//				} else {
					//int gradient = (256 / matrix.size()) * ((matrix.size()) - (i +1)) + 1;
					int gradient = (256 / matrix.size()) * ((i + 1)) + 1;
					if (gradient > 200) {
						gradient = 200;
					}
					System.out.println("gradient " + gradient);
					g2.setColor(new Color(0, gradient, 0));
//				}
				matrix.get(i).stream().forEach(l -> {
					l.prePaint(g2);
//					int u = 9;
				});
			}
//			g2.drawLine(10, 10, 1000, 10);
			g.drawImage(img, 0, 0, c);
			// g2.dispose();
			
			
//			List<Line> lines = generateLines(dim);
			List<Line> lines = future.get();

			g.setColor(new Color(0, 255, 0));


			try {
				executor.invokeAll(lines.stream().map(l -> new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						l.paint(g);
						return null;
					}
				}).collect(Collectors.toList()));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			g.drawLine(10, 10, 1000, 100);

			
			// executor.awaitTermination(500, TimeUnit.MILLISECONDS);
			if (matrix.size() > matrixDeep) {
				matrix.removeFirst();
			}
			// System.out.println("$$$$$$$$$$$ size " + matrix.size());
			matrix.add(lines);
			
			// g2d.dispose();
			
		}
		System.out.println("$$$$$$$$ close");
		executor.shutdown();
		window.dispose();
	}

	private static List<Line> generateLines(Dimension dim) {
		long start = System.currentTimeMillis();
		
		int xLimit = dim.width;
		int yLimit = 500;

		List<Line> lines = new ArrayList<>();
		lines.add(new Line(random.nextInt(xLimit), random.nextInt(10)));
		lines.add(new Line(random.nextInt(xLimit), random.nextInt(10)));
		lines.add(new Line(random.nextInt(xLimit), -random.nextInt(10)));
		lines.add(new Line(random.nextInt(xLimit), -random.nextInt(10)));
		lines.add(new Line(random.nextInt(xLimit), -random.nextInt(10)));

		lines.add(new Line(random.nextInt(xLimit), 20 + random.nextInt(30)));
		lines.add(new Line(random.nextInt(xLimit), 20 + random.nextInt(30)));
		lines.add(new Line(random.nextInt(xLimit), 20 + random.nextInt(30)));

		lines.add(new Line(random.nextInt(xLimit), random.nextInt(yLimit)));
		lines.add(new Line(random.nextInt(xLimit), random.nextInt(yLimit)));
		lines.add(new Line(random.nextInt(xLimit), random.nextInt(yLimit)));
		lines.add(new Line(random.nextInt(xLimit), random.nextInt(yLimit)));
		lines.add(new Line(random.nextInt(xLimit), random.nextInt(yLimit)));
		lines.add(new Line(random.nextInt(xLimit), random.nextInt(yLimit)));
		
		System.out.println("Generated " + (System.currentTimeMillis() - start) + "ms");
		
		return lines;
	}

	Main() {
		// c.setFont(Font.getFont("Times New Roman"));
		add(c);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isClosed = true;
				// dispose();
			}
		});

		setMinimumSize(new Dimension(1800, 700));
		pack();
	}

}