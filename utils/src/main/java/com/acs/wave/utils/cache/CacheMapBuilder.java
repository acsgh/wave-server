package com.acs.wave.utils.cache;

import java.util.concurrent.TimeUnit;

public class CacheMapBuilder<Key, Value> {

    private Long timeout;
    private TimeUnit units;
    private CacheProvider<Key, Value> provider;


    public CacheMap<Key, Value> build() {
        return new CacheMap<>(timeout, units, provider, System::currentTimeMillis);
    }

    public CacheMapBuilder<Key, Value> withTimeout(Long timeout, TimeUnit units) {
        this.timeout = timeout;
        this.units = units;
        return this;
    }

    public CacheMapBuilder<Key, Value> withProvider(CacheProvider<Key, Value> provider) {
        this.provider = provider;
        return this;
    }
}
