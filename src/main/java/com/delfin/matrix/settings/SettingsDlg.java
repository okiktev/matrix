package com.delfin.matrix.settings;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.EAST;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.NORTHWEST;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Properties;

import com.delfin.matrix.MatrixException;
import com.delfin.matrix.Window;

public class SettingsDlg extends Dialog {

	private static final long serialVersionUID = 946963857445794122L;

	static final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, 17);

	private MatrixTypePanel matrixTypePanel;
	private SettingsPanel voluntaryPanel;
	private SettingsPanel $199Panel;
	private SettingsPanel $2021Panel;

	private Button btnReset = new Button("Reset to Defaults");
	private Button btnApply = new Button("Apply");

	private Window window;

	public SettingsDlg(Frame frame) {
		super(frame, "Settings", false);

		window = (Window) frame;
		
		setSize(450, 700);
		Point p = new Point((window.getWidth() - getWidth()) / 2, (window.getHeight() - getHeight()) / 2);
		setLocation(p);
		
		setBackground(Color.BLACK);

		initComponents();

		initActions(btnApply, btnReset);

		setVisible(true);
	}

	private void initComponents() {
		matrixTypePanel = new MatrixTypePanel();
		voluntaryPanel = new VoluntaryPanel();
		$199Panel = new $1999Panel();
		$2021Panel = new $2021Panel();

		setLayout(new GridBagLayout());

		TabbedPane tabbedPane = new TabbedPane();
		tabbedPane.addTab("Matrix Type", matrixTypePanel);
		tabbedPane.addTab("Voluntary", voluntaryPanel);
		tabbedPane.addTab("1999", $199Panel);
		tabbedPane.addTab("2021", $2021Panel);
		add(tabbedPane, new GridBagConstraints(0, 0, 2, 1, 1, 1, NORTHWEST, BOTH, new Insets(0, 0, 0, 0), 0, 0));

		btnReset.setFont(FONT);
		btnReset.setBackground(Color.BLACK);
		btnReset.setForeground(Color.GREEN);
		add(btnReset, new GridBagConstraints(0, 1, 1, 1, 1, 0, EAST, NONE, new Insets(5, 5, 5, 5), 0, 0));

		btnApply.setFont(FONT);
		btnApply.setBackground(Color.BLACK);
		btnApply.setForeground(Color.GREEN);
		add(btnApply, new GridBagConstraints(1, 1, 1, 1, 0, 0, EAST, NONE, new Insets(5, 0, 5, 5), 0, 0));

	}

	private void initActions(Button btnApply, Button btnReset) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		btnApply.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				try {
					// collect properties from panels
					Properties props = new Properties();
					props.putAll(matrixTypePanel.getProperties());
					props.putAll(voluntaryPanel.getProperties());
					props.putAll($199Panel.getProperties());
					props.putAll($2021Panel.getProperties());
					// dump properties to settings and file
					Settings.setAndSave(props);
					// reload matrix accordingly
					window.doRedraw();
				} catch (IOException e) {
					new MatrixException("Unable to reset to defaults", e);
				}
			}
		});
		btnReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				try {
					// load properties from defaults
					Settings.loadDefaults();					
					// reinit settings dialog
					removeAll();
					initComponents();
					revalidate();
					// reload matrix accordingly
					window.doRedraw();
				} catch (IOException e) {
					new MatrixException("Unable to reset to defaults", e);
				}
			}
		});
	}

	public static void main(String[] args) {
		new SettingsDlg(null);
	}

}