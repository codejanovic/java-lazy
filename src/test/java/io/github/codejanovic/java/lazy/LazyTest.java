package io.github.codejanovic.java.lazy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LazyTest {
    @Test
    public void testSmartLazy() throws Exception {
        final List<String> list = new ArrayList<>();
        final Lazy<String> lazy = new Lazy.Smart<>(() -> { list.add("1"); return "1"; });
        assertThat(list).isEmpty();

        assertThat(lazy.value()).isEqualTo("1");
        assertThat(list).hasSize(1);
        assertThat(lazy.value()).isEqualTo("1");
        assertThat(list).hasSize(2);
    }

    @Test
    public void testCachedLazy() throws Exception {
        final List<String> list = new ArrayList<>();
        final Lazy<String> lazy = new Lazy.Cached<>(new Lazy.Smart<>(() -> { list.add("1"); return "1"; }));
        assertThat(list).isEmpty();

        assertThat(lazy.value()).isEqualTo("1");
        assertThat(list).hasSize(1);
        assertThat(lazy.value()).isEqualTo("1");
        assertThat(list).hasSize(1);
    }
}