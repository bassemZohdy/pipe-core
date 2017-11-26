package io.pipe.queue;

import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.pipe.core.Pipe;

public class QueuedPipe<T> implements Pipe<T, T> {
	private final Queue<T> q;
	private final Consumer<T> defualtConsumer;
	private final Supplier<T> defualtSupplier;

	private QueuedPipe(Queue<T> q, Consumer<T> defualtConsumer, Supplier<T> defualtSupplier) {
		this.q = q;
		this.defualtConsumer = defualtConsumer;
		this.defualtSupplier = defualtSupplier;
	}

	public static <T> Pipe<T, T> with(Queue<T> q, Consumer<T> defualtConsumer, Supplier<T> defualtSupplier) {
		return new QueuedPipe<>(q, defualtConsumer, defualtSupplier);
	}

	@Override
	public void accept(T t) {
		Optional.ofNullable(t).filter(i -> !q.offer(i)).filter(i -> this.defualtConsumer != null)
				.ifPresent(this.defualtConsumer);
	}

	@Override
	public T get() {
		return Optional.ofNullable(this.q.poll())
				.orElseGet(() -> (this.defualtSupplier != null) ? this.defualtSupplier.get() : null);
	}

}
