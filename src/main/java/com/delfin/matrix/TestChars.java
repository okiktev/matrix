package com.delfin.matrix;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
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
import java.util.Date;
import java.util.LinkedList;

public class TestChars extends Frame {

	private static volatile boolean isClosed = false;
	
	private static Random random = new Random();

	private static volatile boolean isRepainted = false;

	static Component c = new Component() {
		@Override
		public void paint(Graphics g) {
			 // Graphics g = c.getGraphics();
//				Graphics2D g2d = (Graphics2D) g.create();
//				g2d.setColor(Color.WHITE);
				
				// Font f = Font.getFont(Font.SERIF);
				Font f = new Font(Font.MONOSPACED, Font.BOLD, 20);
			     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			     try {
					ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts", "jpn_boot.ttf")));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			     f = new Font("jpn_boot", Font.BOLD, 30);
				
				
				g.setFont(f);

//				for (int i = 0; i < Line.CHARS.size(); ++i) {
//					int shiftx = 0;
//					int shifty = 0;					
//					//if (i > 30) {
//						shiftx = i / 30;
////						shifty = i / 30 + i % 30;
//						shifty = i % 30;
//						System.out.println(shiftx + " " + shifty);
//					//}
//					g.drawString(i + "=" + Line.CHARS.get(i), shiftx*70, (shifty )*20);
//				}
				
				for (int i = 0; i < 500; ++i) {
					int shiftx = 0;
					int shifty = 0;					
					//if (i > 30) {
						shiftx = i / 30 ;
//						shifty = i / 30 + i % 30;
						shifty = i % 30;
						System.out.println(shiftx + " " + shifty);
					//}
					g.drawString(i + "=" + (char) i, shiftx*110, (shifty )*20);
				}
		}
	};

	static LinkedList<List<Line>> matrix = new LinkedList<>();

	private static boolean doRepaint;

	
	private static int matrixDeep = 10;
	
	public static void main(String argv[]) throws InterruptedException, ExecutionException {
		TestChars window = new TestChars();
		window.setVisible(true);
		doRepaint = true;

			String[] fs = Toolkit.getDefaultToolkit().getFontList();



		//window.dispose();
	}



	TestChars() {
		// c.setFont(Font.getFont("Times New Roman"));
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
