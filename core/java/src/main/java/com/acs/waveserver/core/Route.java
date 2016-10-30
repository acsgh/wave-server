package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.RequestMethod;

class Route<T> {
    final String uri;
    final RequestMethod method;
    final T handler;

    Route(String uri, RequestMethod method, T handler) {
        this.uri = uri;
        this.method = method;
        this.handler = handler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route<?> route = (Route<?>) o;

        if (!uri.equals(route.uri)) return false;
        return method == route.method;

    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + method.hashCode();
        return result;
    }
}
