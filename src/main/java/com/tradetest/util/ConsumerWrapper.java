package com.tradetest.util;

import java.util.function.Consumer;

public class ConsumerWrapper {

	public static <T> Consumer<T> throwingConsumer(ThrowingConsumer<T, Exception> throwingConsumer) {

		return i -> {
			try {
				throwingConsumer.accept(i);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <T, E extends Exception> Consumer<T> handlingConsumer(ThrowingConsumer<T, E> throwingConsumer,
			Class<E> exceptionClass) {

		return i -> {
			try {
				throwingConsumer.accept(i);
			} catch (Exception ex) {
				try {
					E exCast = exceptionClass.cast(ex);
					System.err.println("Exception occured : " + exCast.getMessage());
				} catch (ClassCastException ccEx) {
					throw new RuntimeException(ex);
				}
			}
		};
	}
}
