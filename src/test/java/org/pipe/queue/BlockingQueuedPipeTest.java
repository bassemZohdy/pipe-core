package org.pipe.queue;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.pipe.core.Pipe;

public class BlockingQueuedPipeTest {

	@Test
	public void test() {
		List<Integer> container = new ArrayList<>();
		AtomicInteger counter = new AtomicInteger();
		BlockingQueue<Integer> q = new LinkedBlockingQueue<>();
		Pipe<Integer, Integer> p = BlockingQueuedPipe.with(q);
		for (int i = 0; i < 10; i++) {
			p.accept(counter.getAndIncrement());
		}
		for (int i = 0; i < 10; i++) {
			int j = p.get();
			assertEquals(i, j);
			container.add(j);
		}
		assertEquals(10, container.size());
	}

}
