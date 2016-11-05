package com.acs.waveserver.utils.cache;

@FunctionalInterface
public interface CacheProvider<Key, Value> {

    Value get(Key key);
}
