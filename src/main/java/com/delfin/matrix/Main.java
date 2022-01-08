package com.delfin.matrix;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Date;

public class Main extends Frame {

	private static volatile boolean isClosed = false;

//	private static List<Character> line = new ArrayList<Character>();
//	static {
//		line.add('r');
//		line.add('Y');
//		line.add('H');
//		line.add('j');
//		line.add('k');
//		line.add('o');
//		line.add('P');
//		line.add('s');
//	}

	static Component c = new Component() {
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			System.out.println("$$ executed");
//        //    new Line().paint(100, 10, g);
//            
//            ExecutorService executor = Executors.newCachedThreadPool();
//            executor.submit(() -> {
//            	new Line().paint(10, 0, g);
//            });
//            executor.submit(() -> {
//            	new Line().paint(100, 20, g);
//            });
//            executor.submit(() -> {
//            	new Line().paint(150, 50, g);
//            });
//            try {
//				executor.awaitTermination(1, TimeUnit.SECONDS);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

//            int offset = 0;
//            for (Character c : line) {
//                //String text = "" + new Date().getTime();
//            	String ch = new String(new char[] {c});
//            	
////                g.setFont(g.getFont().deriveFont(new Random().nextFloat()));
//                g.setFont(g.getFont().deriveFont(40f));
//
//                Rectangle2D b = g.getFontMetrics().getStringBounds(ch, g);
//
//                Rectangle2D bounds = new Rectangle2D.Double(0, offset, b.getWidth(), b.getHeight());
//                
//                g.setClip(bounds);
//                //g.drawLine(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight());
//                offset += (int) bounds.getHeight();
//                g.drawString(ch, 0, offset);
// //               g.drawString(ch, 0, 100);
//                System.out.print(ch);
//                delay(100);
//            }
//            System.out.println();

			// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

//          String text = "r";
//
//          g.setFont(g.getFont().deriveFont(40f));
//
//          Rectangle2D b = g.getFontMetrics().getStringBounds(text, g);
////          Rectangle2D bounds = new Rectangle2D.Double(b.getX(),
////                                                      b.getY(),
////                                                      b.getWidth(), 
////                                                      b.getHeight());
//          Rectangle2D bounds = new Rectangle2D.Double(0,0,
//                  b.getWidth(), 
//                  b.getHeight() * 2);
//          
//          g.setClip(bounds);
////          g.drawString(text, (int) bounds.getX(), 
////                             (int) (bounds.getMaxY() + b.getMaxY()));
//          g.drawString(text, 0, 100);
//          
//          
//          
//          text = "Y";
//          
//          g.setFont(g.getFont().deriveFont(40f));
//
//          b = g.getFontMetrics().getStringBounds(text, g);
////          Rectangle2D bounds = new Rectangle2D.Double(b.getX(),
////                                                      b.getY(),
////                                                      b.getWidth(), 
////                                                      b.getHeight());
//          bounds = new Rectangle2D.Double(0,0,
//                  b.getWidth(), 
//                  b.getHeight() * 2);
//          
//          g.setClip(bounds);
////          g.drawString(text, (int) bounds.getX(), 
////                             (int) (bounds.getMaxY() + b.getMaxY()));
//          g.drawString(text, 0, 100);

			// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

//            String text = "" + new Date().getTime();
//
//            g.setFont(g.getFont().deriveFont(40f));
//
//            Rectangle2D b = g.getFontMetrics().getStringBounds(text, g);
////            Rectangle2D bounds = new Rectangle2D.Double(b.getX(),
////                                                        b.getY(),
////                                                        b.getWidth(), 
////                                                        b.getHeight());
//            Rectangle2D bounds = new Rectangle2D.Double(0,0,
//                    b.getWidth(), 
//                    b.getHeight() * 2);
//            
//            g.setClip(bounds);
////            g.drawString(text, (int) bounds.getX(), 
////                               (int) (bounds.getMaxY() + b.getMaxY()));
//            g.drawString(text, 0, 100);

		}
	};

	static List<List<Line>> matrix = new ArrayList<>();

	public static void main(String argv[]) throws InterruptedException {
		new Main().show();
		while (!isClosed) {
			c.repaint();
			// c.paint(c.getGraphics());

			String[] fs = Toolkit.getDefaultToolkit().getFontList();

			Graphics g = c.getGraphics();
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(Color.WHITE);
			// Font f = Font.getFont(Font.SERIF);
			Font f = new Font(Font.SERIF, Font.BOLD, 40);

			g.setFont(f);

			for (int i = 0; i < matrix.size(); ++i) {
				int gradient = (256 / matrix.size()) * (matrix.size() - (i + 1)) + 1;
				System.out.println("gradient " + gradient);
				g.setColor(new Color(gradient, gradient, gradient));
				matrix.get(i).parallelStream().forEach(l -> {
					l.prePaint(g);
				});
			}

			Random random = new Random();

			int xLimit = 1800;
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

			g.setColor(Color.BLACK);

			ExecutorService executor = Executors.newCachedThreadPool();
			executor.invokeAll(lines.stream().map(l -> new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					l.paint(g);
					return null;
				}
			}).collect(Collectors.toList()));

			// executor.awaitTermination(500, TimeUnit.MILLISECONDS);
			if (matrix.size() > 10) {
				matrix.remove(0);
			}
			matrix.add(lines);
			
			g2d.dispose();

		}
	}

	Main() {
		c.setFont(Font.getFont("Times New Roman"));
		add(c);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isClosed = true;
				dispose();
			}
		});

		setMinimumSize(new Dimension(1800, 700));
		pack();
	}

}
