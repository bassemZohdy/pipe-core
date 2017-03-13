package org.pipe.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.function.Supplier;

import org.pipe.core.Pipe;
import org.pipe.core.Splitter;

public class BlockingQueuedSpitter<K, T> implements Splitter<K, T> {

	private final Map<K, Pipe<T, T>> map;
	private final Supplier<BlockingQueue<T>> queueSupplier;
	private final Function<T, K> getKeyFunction;
	private final Pipe<T, T> defualtPipe;

	private BlockingQueuedSpitter(Function<T, K> getKey, Supplier<BlockingQueue<T>> queueSupplier) {
		this.map = new HashMap<>();
		this.queueSupplier = queueSupplier;
		this.getKeyFunction = getKey;
		this.defualtPipe = BlockingQueuedPipe.with(this.queueSupplier.get());
	}

	public static <K, T> Splitter<K, T> with(final Function<T, K> getKey,
			final Supplier<BlockingQueue<T>> queueSupplier) {
		return new BlockingQueuedSpitter<>(getKey, queueSupplier);
	}

	public Supplier<T> out(K k) {
		return this.map.computeIfAbsent(k, key -> BlockingQueuedPipe.with(this.queueSupplier.get()));
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
