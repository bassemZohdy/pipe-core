package org.pipe.core;

import java.util.function.Supplier;

public interface Splitter<K, T> extends Pipe<T,T> {
	public Supplier<T> out(K k);
}
