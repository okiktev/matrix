package com.delfin.matrix;

import static com.delfin.matrix.Utils.delay;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Main extends Frame {

	private static final long serialVersionUID = -7918449234997731418L;

	private static Component canvas = new Component() {

		private static final long serialVersionUID = -8817172458721655376L;

		@Override
		public void paint(Graphics g) {

		}
	};

	static Thread thread;
	static Matrix matrix;
	static boolean doRedraw;
	static long resized;
	static boolean isClosed;

	public static void main(String argv[]) throws InterruptedException, ExecutionException {
		Main window = new Main();
		window.setVisible(true);
		// to maximize window
		delay(100);

		while (!isClosed) {
			if (doRedraw && System.currentTimeMillis() - resized > 500) {
				if (thread != null) {
					thread.interrupt();
				}
				thread = new Thread(() -> {
					if (matrix != null) {
						matrix.destroy();
					}
					matrix = Architector.createMatrix();
					matrix.draw(canvas);
				});
				thread.start();
				doRedraw = false;
			} else {
				delay();
			}
		}

	}

	Main() {
		add(canvas);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				doRedraw = true;
				resized = System.currentTimeMillis();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isClosed = true;
				matrix.destroy();
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
