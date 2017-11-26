package io.pipe.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import io.pipe.core.Pipe;
import io.pipe.core.Router;

public class QueuedRouter<K, T> implements Router<K, T> {

	private final Map<K, Pipe<T, T>> map;
	private final Supplier<Queue<T>> queueSupplier;
	private final Function<T, K> getKeyFunction;
	private final Pipe<T, T> defualtPipe;
	
	private final Consumer<T> defualtConsumer;
	private final Supplier<T> defualtSupplier;

	private QueuedRouter(Function<T, K> getKey, Supplier<Queue<T>> queueSupplier, Consumer<T> defualtConsumer,
			Supplier<T> defualtSupplier) {
		this.map = new HashMap<>();
		this.queueSupplier = queueSupplier;
		this.defualtConsumer = defualtConsumer;
		this.defualtSupplier = defualtSupplier;
		this.defualtPipe = QueuedPipe.with(this.queueSupplier.get(), this.defualtConsumer, this.defualtSupplier);
		this.getKeyFunction = getKey;
	}

	public static <K, T> Router<K, T> with(final Function<T, K> getKey, final Supplier<Queue<T>> queueSupplier,
			final Consumer<T> c, final Supplier<T> s) {
		return new QueuedRouter<>(getKey, queueSupplier, c, s);
	}

	public static <K, T> Router<K, T> with(Function<T, K> getKey, Supplier<Queue<T>> queueSupplier) {
		return QueuedRouter.with(getKey, queueSupplier, null, null);
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
