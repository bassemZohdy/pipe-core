# pipe-core
If you have 2 Java 8 streams not in same context and what to connect them you can use this APIs

        Pipe<Integer,Integer> pipe= BlockingQueuedPipe.with(new LinkedBlockingQueue<>());
        IntStream.range(0,100).boxed().forEach(pipe);
        // in other context
        List list = Stream.generate(pipe).limit(100).collect(Collectors.toList());
