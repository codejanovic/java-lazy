package io.github.codejanovic.java.lazy;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public interface ThrowingLazy<T> {
    T value() throws Exception;

    final class Smart<T> implements ThrowingLazy<T> {
        private final ThrowingLazy<T> lazy;

        public Smart(final ThrowingLazy<T> supplier) {
            this.lazy = supplier;
        }

        @Override
        public T value() throws Exception {
            return lazy.value();
        }
    }

    final class Cached<T> implements ThrowingLazy<T> {
        private final Queue<T> cache = new ArrayBlockingQueue<>(1);
        private final ThrowingLazy<T> lazy;

        public Cached(final ThrowingLazy<T> lazy) {
            this.lazy = lazy;
        }

        @Override
        public T value() throws Exception {
            if (cache.isEmpty())
                cache.add(lazy.value());
            return cache.peek();
        }
    }
}
