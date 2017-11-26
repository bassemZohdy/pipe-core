package io.pipe.core;

import java.util.function.Supplier;

public interface Router<K, T> extends Pipe<T,T> {
	public Supplier<T> out(K k);
}
