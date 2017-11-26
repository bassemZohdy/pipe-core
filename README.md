# pipe-core
This library help you to link 2 streams not in same line of code, also help you to route items to varient streams based of `Function<T,K>` to be spicified when create the `Router`.

```java
        Pipe<Integer,Integer> pipe= BlockingQueuedPipe.with(new LinkedBlockingQueue<>());
        IntStream.range(0,100).boxed().forEach(pipe);
        // in other context
        List list = Stream.generate(pipe).limit(100).collect(Collectors.toList());
```

```java
        Router<Integer, Integer> s = BlockingQueuedRouter.with(i -> i % 3, () -> new LinkedBlockingQueue<>());
        Supplier<Integer> s0 = s.out(0);
        Supplier<Integer> s1 = s.out(1);
        Supplier<Integer> s2 = s.out(2);
        // then
        AtomicInteger counter = new AtomicInteger();
        s.accept(counter.getAndIncrement());
        Stream.generate(s0);
        Stream.generate(s1);
        Stream.generate(s2);
```