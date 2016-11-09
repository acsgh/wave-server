package com.acs.wave.router;

import com.acs.wave.router.constants.RequestMethod;
import com.acs.wave.utils.CheckUtils;

import java.util.Set;

class HTTPRoute<T> {
    final String uri;
    final Set<RequestMethod> methods;
    final T handler;

    HTTPRoute(String uri, Set<RequestMethod> methods, T handler) {
        CheckUtils.checkString("uri", uri);
        CheckUtils.checkNull("method", methods);
        CheckUtils.checkNull("handler", handler);

        this.uri = uri;
        this.methods = methods;
        this.handler = handler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HTTPRoute<?> httpRoute = (HTTPRoute<?>) o;

        if (!uri.equals(httpRoute.uri)) return false;
        return methods.equals(httpRoute.methods);

    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + methods.hashCode();
        return result;
    }

    boolean canApply(HTTPRequest httpRequest) {
        return validMethod(httpRequest) && httpRequest.address.matchUrl(this.uri);
    }

    private boolean validMethod(HTTPRequest httpRequest) {
        return (methods.isEmpty()) || (methods.contains(httpRequest.method));
    }

    @Override
    public String toString() {
        return "Route{" +
                "uri='" + uri + '\'' +
                ", methods=" + methods +
                '}';
    }
}
