package io.github.codejanovic.java.lazy;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;


public interface Lazy<T> {
    T value();

    final class Smart<T> implements Lazy<T> {
        private final Supplier<T> supplier;

        public Smart(final Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T value() {
            return supplier.get();
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
