package com.acs.wave.router;


public class WebSocketRoute {

    public final String uri;
    public final String subprotocol;

    public WebSocketRoute(String uri, String subprotocol) {
        this.uri = uri;
        this.subprotocol = subprotocol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebSocketRoute that = (WebSocketRoute) o;

        if (!uri.equals(that.uri)) return false;
        return subprotocol != null ? subprotocol.equals(that.subprotocol) : that.subprotocol == null;

    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + (subprotocol != null ? subprotocol.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WebSocketRoute{" +
                "uri='" + uri + '\'' +
                ", subprotocols='" + subprotocol + '\'' +
                '}';
    }
}