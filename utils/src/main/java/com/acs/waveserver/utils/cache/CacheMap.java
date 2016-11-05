package com.acs.waveserver.utils.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CacheMap<Key, Value> {

    private final Long timeout;
    private final TimeUnit units;
    private final CacheProvider<Key, Value> provider;
    private final Supplier<Long> timeProvider;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private Map<Key, CacheEntry<Value>> map = new HashMap<>();

    CacheMap(Long timeout, TimeUnit units, CacheProvider<Key, Value> provider, Supplier<Long> timeProvider) {
        this.timeout = timeout;
        this.units = units;
        this.provider = provider;
        this.timeProvider = timeProvider;
    }

    public Value get(Key key) {
        boolean mustLoad = secureRead(() -> ((provider != null) && (!isValid(key))));

        if (mustLoad) {
            secureWrite(() -> {
                if (!isValid(key)) {
                    Value value = provider.get(key);

                    if (value != null) {
                        map.put(key, new CacheEntry<>(value, getExpirationTime()));
                    }
                }
            });
        }
        return secureGet(key);
    }

    private Long getExpirationTime() {
        return entriesExpire() ? timeProvider.get() + TimeUnit.MILLISECONDS.convert(timeout, units) : null;
    }


    public void put(Key key, Value value) {
        secureWrite(() -> map.put(key, new CacheEntry<>(value, getExpirationTime())));
    }

    public void remove(Key key) {
        secureWrite(() -> map.remove(key));
    }


    public void clear() {
        secureWrite(() -> map.clear());
    }


    public void cleanup() {
        secureWrite(() -> {
            List<Key> keysToRemove = map.entrySet().stream()
                    .map(Map.Entry::getKey)
                    .filter(key -> !isValid(key))
                    .collect(Collectors.toList());

            keysToRemove.forEach(map::remove);
        });
    }

    private Value secureGet(Key key) {
        return secureRead(() -> isValid(key) ? map.get(key).value : null);
    }

    private boolean isValid(Key key) {
        CacheEntry<Value> entry = map.get(key);

        boolean validEntry = (entry != null) && (entry.value != null);

        if (validEntry && entry.canExpire() && entriesExpire()) {
            validEntry = timeProvider.get() <= entry.expirationTime;
        }

        return validEntry;
    }

    private boolean entriesExpire() {
        return (timeout != null) && (units != null);
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
