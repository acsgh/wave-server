package com.acs.wave.utils.cache;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class CacheMapTest {

    @Mock
    private CacheProvider<String, String> provider;

    @Mock
    private Supplier<Long> timeProvider;

    @Test
    public void normal_mapping(){
        CacheMap<String, String> cache = new CacheMapBuilder<String, String>().build();
        String expected = "World!";
        cache.put("Hello", expected);
        String result = cache.get("Hello");
        assertEquals(expected, result);
    }

    @Test
    public void no_entry_null(){
        CacheMap<String, String> cache = new CacheMapBuilder<String, String>().build();
        String result = cache.get("Hello");
        assertEquals(null, result);
    }

    @Test
    public void no_entry_but_provider(){
        String key = "Hello";
        String expected = "World!";
        when(provider.get(key)).thenReturn(expected);
        CacheMap<String, String> cache = new CacheMapBuilder<String, String>().withProvider(provider).build();
        String result = cache.get(key);
        assertEquals(expected, result);
    }

    @Test
    public void no_timeout_pass(){
        when(timeProvider.get()).thenReturn(1L, 2L);
        String key = "Hello";
        String expected = "World!";
        CacheMap<String, String> cache = new CacheMap<>(10L, TimeUnit.MILLISECONDS, null, timeProvider);
        cache.put(key, expected);
        String result = cache.get(key);
        assertEquals(expected, result);
    }

    @Test
    public void timeout_pass(){
        when(timeProvider.get()).thenReturn(1L, 1000L);
        String key = "Hello";
        String expected = "World!";
        CacheMap<String, String> cache = new CacheMap<>(10L, TimeUnit.MILLISECONDS, null, timeProvider);
        cache.put(key, expected);
        String result = cache.get(key);
        assertEquals(null, result);
    }
}
