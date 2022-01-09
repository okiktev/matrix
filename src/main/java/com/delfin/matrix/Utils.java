package com.delfin.matrix;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

class Utils {

	private static Random random = new Random();

	static void delay() {
		delay(1000);
	}

	static void delay(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	static Object time(Consumer<Long> logger, Supplier<?> executor) {
		long start = System.currentTimeMillis();
		try {
			return executor.get();
		} finally {
			logger.accept(System.currentTimeMillis() - start);
		}
	}

	static int getRandomFrom(int[] range) {
		if (range[0] == range[1]) {
			return range[0];
		} else {
			return range[0] + random.nextInt(range[1] - range[0]);
		}
	}

}
