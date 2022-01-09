package com.delfin.matrix;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.util.Arrays;

class Settings {
	static String fname = "jpn_boot";
//	static String fname = "kor_boot";
//	static String fname = "constan";
	// static String fname = "camriab";
	static {
		try {
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts", fname + ".ttf")));
		     //ge.preferLocaleFonts();
		     
//		     System.out.println(new Font("jpn_boot", Font.BOLD, 10));
		     
		     //System.out.println(Font.getFont("jpn_boot"));
		     
//		     String[] fs = Toolkit.getDefaultToolkit().getFontList();
//		     Arrays.stream(fs).forEach(System.out::println);
		     
		} catch (Exception e) {
		     //Handle exception
		}
	}
	
// 	String FONT_NAME = Font.MONOSPACED;
//	String FONT_NAME = Font.SERIF;
//	String FONT_NAME = Font.SANS_SERIF;
//	String FONT_NAME = Font.DIALOG;
//	 static String FONT_NAME = Font.DIALOG_INPUT;
	static String FONT_NAME = fname;
	
}
