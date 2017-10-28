package io.github.codejanovic.java.lazy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LazyTest {
    @Test
    public void testSmartLazy() throws Exception {
        final List<String> list = new ArrayList<>();
        final Lazy<String> lazy = new Lazy.Value<>(() -> { list.add("1"); return "1"; });
        assertThat(list).isEmpty();

        assertThat(lazy.value()).isEqualTo("1");
        assertThat(list).hasSize(1);
        assertThat(lazy.value()).isEqualTo("1");
        assertThat(list).hasSize(2);
    }

    @Test
    public void testCachedLazy() throws Exception {
        final List<String> list = new ArrayList<>();
        final Lazy<String> lazy = new Lazy.Cached<>(
                new Lazy.Value<>(() -> {
                    list.add("1");
                    return "1";
                }));
        assertThat(list).isEmpty();

        assertThat(lazy.value()).isEqualTo("1");
        assertThat(list).hasSize(1);
        assertThat(lazy.value()).isEqualTo("1");
        assertThat(list).hasSize(1);
    }

    @Test
    public void testThrowingLazy() throws Exception {
        final List<String> list = new ArrayList<>();
        final ThrowingLazy<String, ArrayIndexOutOfBoundsException> lazy =
                new ThrowingLazy.Value<>(() -> {
                    throw new ArrayIndexOutOfBoundsException("");
                });

        assertThat(list).isEmpty();
        assertThatThrownBy(lazy::value).isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testThrowingCachedLazyWillThrow() throws Exception {
        final List<String> list = new ArrayList<>();
        final ThrowingLazy<String, ArrayIndexOutOfBoundsException> lazy =
                new ThrowingLazy.Cached<>(
                        new ThrowingLazy.Value<>(() -> {
                            throw new ArrayIndexOutOfBoundsException("");
                        })
                );

        assertThat(list).isEmpty();
        assertThatThrownBy(lazy::value).isInstanceOf(ArrayIndexOutOfBoundsException.class);
        assertThatThrownBy(lazy::value).isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testThrowingCachedLazy() throws Exception {
        final List<String> list = new ArrayList<>();
        final ThrowingLazy<String, ArrayIndexOutOfBoundsException> lazy =
                new ThrowingLazy.Cached<>(
                        new ThrowingLazy.Value<>(() -> {
                            list.add("1");
                            return "1";
                        })
                );

        assertThat(list).isEmpty();
        assertThat(lazy.value()).isEqualTo("1");
        assertThat(list).hasSize(1);
        assertThat(lazy.value()).isEqualTo("1");
        assertThat(list).hasSize(1);
    }
}