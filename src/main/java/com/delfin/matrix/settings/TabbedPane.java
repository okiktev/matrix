package com.delfin.matrix.settings;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.NORTHWEST;
import static java.awt.Color.*;

import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class TabbedPane extends Panel {

	private static final long serialVersionUID = -1250686302181399103L;

	private static final Insets INSETS = new Insets(0, 0, 0, 0);

	private List<Button> buttons = new ArrayList<>();
	private List<Panel> panes = new ArrayList<>();
	private int selectedIdx = 0;

	TabbedPane() {
		setBackground(BLACK);
	}

	void addTab(String tabName, Panel tabPanel) {
		Button tabButton = new TabButton(tabName, buttons.size());
		tabButton.setFont(SettingsDlg.FONT);
		tabButton.setBackground(BLACK);
		tabButton.setForeground(buttons.isEmpty() ? WHITE : GREEN);
		tabButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttons.forEach(b -> b.setForeground(GREEN));

				TabButton button = (TabButton) e.getSource();
				selectedIdx = button.indx;
				button.setForeground(WHITE);

				redrawContent();
			}
		});

		buttons.add(tabButton);
		panes.add(tabPanel);

		removeAll();
		setLayout(new GridBagLayout());

		int idx[] = { 0 };
		buttons.forEach(b -> {
			add(b, new GridBagConstraints(idx[0], 0, 1, 1, 0, 0, NORTHWEST, NONE, INSETS, 0, 0));
			idx[0]++;
		});

		redrawContent();
	}

	private void redrawContent() {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.add(panes.get(selectedIdx));
		if (getComponentCount() == buttons.size() + 1) {
			remove(getComponentCount() - 1);
		}
		add(scrollPane, new GridBagConstraints(0, 1, panes.size(), 1, 1, 1, NORTHWEST, BOTH, INSETS, 0, 0));
		validate();
	}

	private static class TabButton extends Button {

		private static final long serialVersionUID = -4116830311878318582L;

		int indx;

		TabButton(String text, int indx) {
			super(text);
			this.indx = indx;
		}

	}

}
