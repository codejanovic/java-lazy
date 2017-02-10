package io.github.codejanovic.java.lazy;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


public interface Lazy<T> {
    T value();

    final class Smart<T> implements Lazy<T> {
        private final Lazy<T> lazy;

        public Smart(final Lazy<T> lazy) {
            this.lazy = lazy;
        }

        @Override
        public T value() {
            return lazy.value();
        }
    }

    final class Cached<T> implements Lazy<T> {
        private final Queue<T> cache = new ArrayBlockingQueue<>(1);
        private final Lazy<T> lazy;

        public Cached(final Lazy<T> lazy) {
            this.lazy = lazy;
        }

        @Override
        public T value() {
            if (cache.isEmpty())
                cache.add(lazy.value());
            return cache.peek();
        }
    }
}
