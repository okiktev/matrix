package com.delfin.matrix;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Utils {

	private static Random random = new Random();

	public static void delay() {
		delay(1000);
	}

	public static void delay(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public static <T> T time(Consumer<Long> logger, Supplier<T> executor) {
		long start = System.currentTimeMillis();
		try {
			return executor.get();
		} finally {
			logger.accept(System.currentTimeMillis() - start);
		}
	}

	public static int getRandomFrom(int[] range) {
		if (range[0] == range[1]) {
			return range[0];
		} else {
			return range[0] + random.nextInt(range[1] - range[0]);
		}
	}

}
