package com.acs.wave.utils.cache;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CacheBuilder<Value> {

    private Long timeout;
    private TimeUnit units;
    private Supplier<Value> provider;


    public Cache<Value> build() {
        return new Cache<>(timeout, units, provider, System::currentTimeMillis);
    }

    public CacheBuilder<Value> withTimeout(Long timeout, TimeUnit units) {
        this.timeout = timeout;
        this.units = units;
        return this;
    }

    public CacheBuilder<Value> withProvider(Supplier<Value> provider) {
        this.provider = provider;
        return this;
    }
}
