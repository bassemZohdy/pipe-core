package org.pipe.queue;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.Test;
import org.pipe.core.Splitter;

public class QueuedSpitterTest {

	@Test
	public void test() {
		AtomicInteger counter = new AtomicInteger();
		Splitter<Integer, Integer> s = QueuedSpitter.with(i -> i % 3, () -> new LinkedBlockingQueue<>(), null, null);
		Supplier<Integer> s0 = s.out(0);
		Supplier<Integer> s1 = s.out(1);
		Supplier<Integer> s2 = s.out(2);
		for (int i = 0; i < 10; i++) {
			s.accept(counter.getAndIncrement());
		}
		// Assert s0
		assertEquals(0, (int) s0.get());
		assertEquals(3, (int) s0.get());
		assertEquals(6, (int) s0.get());
		assertEquals(9, (int) s0.get());

		// Assert s1
		assertEquals(1, (int) s1.get());
		assertEquals(4, (int) s1.get());
		assertEquals(7, (int) s1.get());

		// Assert s2
		assertEquals(2, (int) s2.get());
		assertEquals(5, (int) s2.get());
		assertEquals(8, (int) s2.get());

	}

	@Test
	public void testDefault() {
		AtomicInteger counter = new AtomicInteger();
		Splitter<Integer, Integer> s = QueuedSpitter.with(i -> i % 3, () -> new LinkedBlockingQueue<>(), null, null);
		Supplier<Integer> s0 = s.out(0);
		for (int i = 0; i < 10; i++) {
			s.accept(counter.getAndIncrement());
		}
		// Assert s0
		assertEquals(0, (int) s0.get());
		assertEquals(3, (int) s0.get());
		assertEquals(6, (int) s0.get());
		assertEquals(9, (int) s0.get());

		// Assert Default
		assertEquals(1, (int) s.get());
		assertEquals(2, (int) s.get());
		assertEquals(4, (int) s.get());
		assertEquals(5, (int) s.get());
		assertEquals(7, (int) s.get());
		assertEquals(8, (int) s.get());

	}
}