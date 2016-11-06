package com.acs.wave.utils.cache;

class CacheEntry<Value> {
    final Value value;
    final Long expirationTime;

    CacheEntry(Value value, Long expirationTime) {
        this.value = value;
        this.expirationTime = expirationTime;
    }

    public boolean canExpire() {
        return (expirationTime != null);
    }
}
