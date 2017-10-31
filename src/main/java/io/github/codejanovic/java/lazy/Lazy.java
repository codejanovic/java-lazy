package io.github.codejanovic.java.lazy;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;


public interface Lazy<T> extends Supplier<T> {
    default T value() {
        return get();
    }

    final class Value<T> implements Lazy<T> {
        private final Supplier<T> lazy;

        public Value(final Supplier<T> lazy) {
            this.lazy = lazy;
        }

        @Override
        public T get() {
            return lazy.get();
        }
    }

    final class Cached<T> implements Lazy<T> {
        private final Queue<T> cache = new ArrayBlockingQueue<>(1);
        private final Supplier<T> lazy;

        public Cached(final Supplier<T> lazy) {
            this.lazy = lazy;
        }

        @Override
        public T get() {
            if (cache.isEmpty())
                cache.add(lazy.get());
            return cache.peek();
        }
    }
}
