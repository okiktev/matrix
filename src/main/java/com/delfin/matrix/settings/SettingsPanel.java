package com.delfin.matrix.settings;

import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.NORTHWEST;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import com.delfin.matrix.voluntary.Matrix;

abstract class SettingsPanel extends Panel {

	private static final long serialVersionUID = 3023250814495878559L;

	private static final Insets INSETS = new Insets(0, 0, 0, 0);

	private Map<String, TextField> properties = new HashMap<>();

	SettingsPanel() {
		setFont(SettingsDlg.FONT);
		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());

		Properties props = Matrix.settings(null);
		Set<Entry<Object, Object>> filtered = props.entrySet().stream()
				.filter(e -> {
					String key = (String) e.getKey();
					return !ignoredByStartWith(key) && !ignoredByEqual(key);
				}).collect(Collectors.toSet());

		filtered = filtered.stream()
				.filter(e -> !isSpecialExist(e.getKey(), props.keySet()))
				.collect(Collectors.toSet());

		int filteredSize = filtered.size();
		int[] idx = { 0 };
		filtered.forEach(e -> {
			String propName = (String) e.getKey();
			Label label = new Label(propName);
			label.setForeground(Color.GREEN);
			add(label, new GridBagConstraints(0, idx[0], 1, 1, 1, 0, NORTHWEST, NORTH, INSETS, 0, 0));

			TextField textField = new TextField((String) e.getValue());
			textField.setForeground(Color.GREEN);
			textField.setBackground(Color.BLACK);
			int weighty = idx[0] == filteredSize - 1 ? 1 : 0;
			add(textField, new GridBagConstraints(1, idx[0], 1, 1, 1, weighty, NORTHWEST, HORIZONTAL, INSETS, 0, 0));

			properties.put(propName, textField);

			idx[0]++;
		});

	}

	Properties getProperties() {
		String pref = getPrefix() + '.';
		Properties ret = new Properties();
		ret.putAll(properties.entrySet().stream()
				.collect(Collectors.toMap(e -> {
					String key = e.getKey();
					if (!key.startsWith(pref)) {
						key = pref + key;
					}
					return key;
				}, e -> e.getValue().getText())));
		return ret;
	}

	abstract String getPrefix();
	abstract boolean isSpecialExist(Object key, Set<Object> keys);
	abstract boolean ignoredByEqual(String property);
	abstract boolean ignoredByStartWith(String property);
	
}
