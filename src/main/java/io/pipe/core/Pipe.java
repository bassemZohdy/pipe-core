package io.pipe.core;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Pipe<I, O> extends Consumer<I>, Supplier<O> {
}
