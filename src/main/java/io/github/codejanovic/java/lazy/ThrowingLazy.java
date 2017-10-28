package io.github.codejanovic.java.lazy;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public interface ThrowingLazy<T, E extends Exception> {
    T value() throws E;

    final class Value<T, E extends Exception> implements ThrowingLazy<T, E> {
        private final ThrowingLazy<T, E> lazy;

        public Value(final ThrowingLazy<T, E> supplier) {
            this.lazy = supplier;
        }

        @Override
        public T value() throws E {
            return lazy.value();
        }
    }

    final class Cached<T, E extends Exception> implements ThrowingLazy<T, E> {
        private final Queue<T> cache = new ArrayBlockingQueue<>(1);
        private final ThrowingLazy<T, E> lazy;

        public Cached(final ThrowingLazy<T, E> lazy) {
            this.lazy = lazy;
        }

        @Override
        public T value() throws E {
            if (cache.isEmpty())
                cache.add(lazy.value());
            return cache.peek();
        }
    }
}
