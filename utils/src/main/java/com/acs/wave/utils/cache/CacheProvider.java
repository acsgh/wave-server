package com.acs.wave.utils.cache;

@FunctionalInterface
public interface CacheProvider<Key, Value> {

    Value get(Key key);
}
