package com.delfin.matrix;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Line {
	//static final Font DEFAULT_FONT = new Font(Settings.FONT_NAME, Font.BOLD, 10);
	static final List<Character> CHARS = Chars.getAll();


	private List<Symbol> data = new ArrayList<>();
	private int x;
	private int y;
	private int fontSize;
	private Font font;
	
	
	Line(int x, int y) {
//		fontSize = new Random().nextInt(20) + 20;
		fontSize = 30;
		font = new Font(Settings.FONT_NAME, Font.BOLD, fontSize);
//		font = new Font(Settings.FONT_NAME, Font.PLAIN, fontSize);
		
		// font = new Font(Settings.FONT_NAME, Font.BOLD, fontSize);
		this.x = x;
		this.y = y;
		generateData();
	}
	
	private void generateData() {
		Random random = new Random();
		data = Stream.generate(() -> {
			
			return CHARS.get(random.nextInt(CHARS.size() - 1));
			
//			Character ret = ' ';
//			while (ret == ' ' || ret == '\n' || ret == '\t' || ret == '\r') {
//				//return new Character((char) new Random().nextInt());
//				ret = new Character((char) random.nextInt(1000));
//				//new Random().nextInt(bound)
//				//ret = '$';
//			}
//			return ret;
			}).limit(5 + random.nextInt(20)).map(Symbol::new).collect(Collectors.toList());
	}
	
	void paint(Graphics g) {
		long speed = new Random().nextInt(70) + 30;
		
        int offset = 0;
        
        long start = System.currentTimeMillis();

        for (Symbol s : data) {
        	synchronized (g) {
        		g.setFont(font);
                //String text = "" + new Date().getTime();
            	String ch = new String(Character.toChars(s.ch));
            	
//                g.setFont(g.getFont().deriveFont(new Random().nextFloat()));

                Rectangle2D b = g.getFontMetrics().getStringBounds(ch, g);

                //Rectangle2D bounds = new Rectangle2D.Double(x, y + offset, b.getWidth(), b.getHeight());
                
                // g.setClip(bounds);
                //g.drawLine(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight());
                offset += (int) b.getHeight();
                s.offset = offset;
                g.drawString(ch, x, y + offset);
//                   g.drawString(ch, 0, 100);
                //System.out.print(ch);
			}

            Utils.delay(speed);
        }
        
         // System.out.println("paint line " + (System.currentTimeMillis() - start) + "ms");
        
        //System.out.println();
	}

	public void prePaint(Graphics g) {
        // int offset = 0;
        //System.out.print("prep$$  ");
        g.setFont(font);
        for (Symbol s : data) {
       // 	synchronized (g) {
                //String text = "" + new Date().getTime();
            	// String ch = ;
            	
//                g.setFont(g.getFont().deriveFont(new Random().nextFloat()));
            	// Font f = g.getFont().deriveFont(fontSize);
                // g.setColor(Color.GRAY);

                //Rectangle2D b = g.getFontMetrics().getStringBounds(ch, g);

                //Rectangle2D bounds = new Rectangle2D.Double(x, y + offset, b.getWidth(), b.getHeight());
                
                //g.setClip(bounds);
                //g.drawLine(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight());
                //offset += (int) bounds.getHeight();
                g.drawString(new String(Character.toChars(s.ch)), x, y + s.offset);
//                   g.drawString(ch, 0, 100);
                //System.out.print(ch);
	//		}
        }
        //System.out.println();
	}
	
	private static class Symbol {
		// Character ch;
		Character ch;
		int offset;
		Symbol(Character ch) {
			this.ch = ch;
		}
	}

}
