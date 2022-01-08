package com.delfin.matrix;

class Utils {

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

}
