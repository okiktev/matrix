package com.delfin.matrix.settings;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

class ImageButton extends Panel {

	private static final long serialVersionUID = -4174281483097297653L;

	private static final ClassLoader LOADER = ImageButton.class.getClassLoader();
	private static final List<ImageButton> BUTTONS = new ArrayList<>(3);

	private Image img;
	private boolean onHover;
	private String name;
	boolean isSelected;

	ImageButton(String imgPath, String name) throws IOException {
		img = ImageIO.read(LOADER.getResourceAsStream(imgPath));
		this.name = name;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				onHover = true;
				repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				onHover = false;
				repaint();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				((ImageButton)e.getSource()).setSelected(true);
			}
		});
		BUTTONS.add(this);
	}

	@Override
	public void paint(Graphics g) {
		Image image = this.createImage(this.getWidth(), this.getHeight());
		Graphics g2 = image.getGraphics();
	    ((Graphics2D)g2).setStroke(new BasicStroke(5));
		g2.drawImage(img, 0, 0, this);

		if (isSelected && !onHover) {
			g2.setColor(Color.WHITE);
		}
		if (onHover) {
			g2.setColor(Color.GREEN);
		}
		if (isSelected || onHover) {
			g2.setFont(SettingsDlg.FONT);
			g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight());
			g2.drawString(name, 20, this.getHeight() - 20);
		}
		g.drawImage(image, 0, 0, this);
	}

	void setSelected(boolean isSelected) {
		BUTTONS.stream().filter(b -> b.isSelected).forEach(b -> {
			b.isSelected = false;
			b.repaint();
		});
		this.isSelected = isSelected;
		repaint();
	}

}
