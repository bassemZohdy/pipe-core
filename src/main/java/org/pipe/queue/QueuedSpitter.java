package org.pipe.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.pipe.core.Pipe;
import org.pipe.core.Splitter;

public class QueuedSpitter<K, T> implements Splitter<K, T> {

	private final Map<K, Pipe<T, T>> map;
	private final Supplier<Queue<T>> queueSupplier;
	private final Function<T, K> getKeyFunction;
	private final Pipe<T, T> defualtPipe;
	
	private final Consumer<T> defualtConsumer;
	private final Supplier<T> defualtSupplier;

	private QueuedSpitter(Function<T, K> getKey, Supplier<Queue<T>> queueSupplier, Consumer<T> defualtConsumer,
			Supplier<T> defualtSupplier) {
		this.map = new HashMap<>();
		this.queueSupplier = queueSupplier;
		this.defualtConsumer = defualtConsumer;
		this.defualtSupplier = defualtSupplier;
		this.defualtPipe = QueuedPipe.with(this.queueSupplier.get(), this.defualtConsumer, this.defualtSupplier);
		this.getKeyFunction = getKey;
	}

	public static <K, T> Splitter<K, T> with(final Function<T, K> getKey, final Supplier<Queue<T>> queueSupplier,
			final Consumer<T> c, final Supplier<T> s) {
		return new QueuedSpitter<>(getKey, queueSupplier, c, s);
	}

	public static <K, T> Splitter<K, T> with(Function<T, K> getKey, Supplier<Queue<T>> queueSupplier) {
		return QueuedSpitter.with(getKey, queueSupplier, null, null);
	}

	public Supplier<T> out(K k) {
		return this.map.computeIfAbsent(k,
				key -> QueuedPipe.with(this.queueSupplier.get(), this.defualtConsumer, this.defualtSupplier));
	}

	@Override
	public void accept(T t) {
		Optional.ofNullable(t).map(this.getKeyFunction).map(k -> this.map.getOrDefault(k, this.defualtPipe))
				.ifPresent(p -> p.accept(t));
	}

	@Override
	public T get() {
		return this.defualtPipe.get();
	}
}
