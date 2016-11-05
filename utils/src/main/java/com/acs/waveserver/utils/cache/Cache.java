package com.acs.waveserver.utils.cache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class Cache<Value> {

    private final Long timeout;
    private final TimeUnit units;
    private final Supplier<Value> provider;
    private final Supplier<Long> timeProvider;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private Long timestamp;
    private Value value = null;

    Cache(Long timeout, TimeUnit units, Supplier<Value> provider, Supplier<Long> timeProvider) {
        this.timeout = timeout;
        this.units = units;
        this.provider = provider;
        this.timeProvider = timeProvider;
    }

    public boolean isValid() {
        return secureRead(this::isValidInternal);
    }

    public Value get() {
        if (!isValid()) {
            secureWrite(() -> {
                if (!isValidInternal()) {
                    if (provider != null) {
                        setValueInternal(provider.get());
                    }
                }
            });
        }

        return secureRead(() -> isValidInternal() ? value : null);
    }

    public void set(Value value) {
        secureWrite(() -> setValueInternal(value));
    }

    public void clear() {
        secureWrite(() -> setValueInternal(null));
    }

    private void setValueInternal(Value newValue) {
        timestamp = timeProvider.get();
        value = newValue;
    }


    private boolean isValidInternal() {
        boolean result = (value != null);

        if (result) {
            if ((timeout != null) && (units != null)) {
                long maxTime = TimeUnit.MILLISECONDS.convert(timeout, units);
                result = timeProvider.get() < (timestamp + maxTime);
            }
        }

        return result;
    }

    private <T> T secureRead(Supplier<T> action) {
        lock.readLock().lock();
        try {
            return action.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    private void secureWrite(Runnable action) {
        lock.writeLock().lock();
        try {
            action.run();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
