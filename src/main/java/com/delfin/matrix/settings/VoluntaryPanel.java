package com.delfin.matrix.settings;

import java.util.HashSet;
import java.util.Set;

import com.delfin.matrix.Matrix.Type;

class VoluntaryPanel extends SettingsPanel {

	private static final long serialVersionUID = -7658481883445608427L;

	private static final Set<String> IGNORED_BY_EQUAL = new HashSet<>();
	static {
		IGNORED_BY_EQUAL.add("font.name");
		IGNORED_BY_EQUAL.add("font.size.range");
		IGNORED_BY_EQUAL.add("draw.bit");
		IGNORED_BY_EQUAL.add("move.length.range");
		IGNORED_BY_EQUAL.add("symbols.wait.speed.range");
		IGNORED_BY_EQUAL.add("wait.ticks.range");
		IGNORED_BY_EQUAL.add("pair.range");
	}

	private static final Set<String> IGNORED_BY_START_WITH = new HashSet<>();
	static {
		IGNORED_BY_START_WITH.add("app");
		IGNORED_BY_START_WITH.add(Type.$1999.toString());
		IGNORED_BY_START_WITH.add(Type.$2021.toString());
	}

	@Override
	boolean ignoredByEqual(String property) {
		return IGNORED_BY_EQUAL.contains(property);
	}

	@Override
	boolean ignoredByStartWith(String property) {
		return IGNORED_BY_START_WITH.stream().filter(p -> property.startsWith(p)).findFirst().isPresent();
	}

	@Override
	boolean isSpecialExist(Object key, Set<Object> keys) {
		String specialForKey = Type.VOLUNTARY.toString() + '.' + key;
		return keys.stream().filter(k -> specialForKey.equals(k)).findFirst().isPresent();
	}

	@Override
	String getPrefix() {
		return Type.VOLUNTARY.toString();
	}

}
