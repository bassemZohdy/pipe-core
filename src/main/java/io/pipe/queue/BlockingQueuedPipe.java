package io.pipe.queue;

import java.util.concurrent.BlockingQueue;

import io.pipe.core.Pipe;

public class BlockingQueuedPipe<T> implements Pipe<T, T> {
	private final BlockingQueue<T> q;

	private BlockingQueuedPipe(BlockingQueue<T> q) {
		this.q = q;
	}

	public static <T> Pipe<T, T> with(BlockingQueue<T> q) {
		return new BlockingQueuedPipe<>(q);
	}

	@Override
	public void accept(T t) {
		try {
			this.q.put(t);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}

	}

	@Override
	public T get() {
		try {
			return this.q.take();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}
}
