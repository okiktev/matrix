package com.delfin.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Chars {

	private static List<Character> DIGITS;
	private static List<Character> JAPANESE;
	private static List<Character> LATIN;

	static List<Character> getDigits() {
		if (DIGITS == null) {
			DIGITS = IntStream.range(48, 58).mapToObj(i -> new Character((char) i)).collect(Collectors.toList());
		}
		return DIGITS;
	}

	static List<Character> getJapanese() {
		if (JAPANESE == null) {
			JAPANESE = Arrays.asList("30a", "30b", "30c", "30d", "30e", "30f").stream().flatMap(b -> {
				return IntStream.range(0, 16).mapToObj(i -> {
					return (char) Integer.parseInt(b + Integer.toHexString(i), 16);
				});
			}).collect(Collectors.toList());
		}
		return JAPANESE;
	}

	static List<Character> getLatin() {
		if (LATIN == null) {
			LATIN = IntStream.range(65, 91).mapToObj(i -> new Character((char) i)).collect(Collectors.toList());
			LATIN.addAll(IntStream.range(97, 123).mapToObj(i -> new Character((char) i)).collect(Collectors.toList()));
		}
		return LATIN;
	}

	static List<Character> getAll() {
		List<Character> res = new ArrayList<>(getDigits());
		res.addAll(getJapanese());
		res.addAll(getLatin());
		return res;
	}

}
