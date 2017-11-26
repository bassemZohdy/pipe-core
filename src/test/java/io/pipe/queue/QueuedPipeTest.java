package io.pipe.queue;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import io.pipe.core.Pipe;
import io.pipe.queue.QueuedPipe;

public class QueuedPipeTest {

	@Test
	public void test() {
		List<Integer> container = new ArrayList<>();
		AtomicInteger counter = new AtomicInteger();
		Queue<Integer> q = new LinkedBlockingQueue<>();
		Pipe<Integer,Integer> p = QueuedPipe.with(q, null, null);
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
	
	@Test
	public void testDefaultSupplier() {
		List<Integer> container = new ArrayList<>();
		AtomicInteger counter = new AtomicInteger();
		Queue<Integer> q = new LinkedBlockingQueue<>();
		Pipe<Integer,Integer> p = QueuedPipe.with(q, null, counter::getAndIncrement);
		for (int i = 0; i < 10; i++) {
			int j = p.get();
			assertEquals(i, j);
			container.add(j);
		}
		assertEquals(10, container.size());
	}
	
	@Test
	public void testDefaultConsumer() {
		List<Integer> container = new ArrayList<>();
		AtomicInteger counter = new AtomicInteger();
		Queue<Integer> q = new LinkedBlockingQueue<>(1);
		Pipe<Integer,Integer> p = QueuedPipe.with(q, container::add, null);
		for (int i = 0; i < 10; i++) {
			p.accept(counter.getAndIncrement());
		}
		assertEquals(9, container.size());
	}


}
