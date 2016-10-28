package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.RequestMethod;

class Route {
    final String uri;
    final RequestMethod method;

    Route(String uri, RequestMethod method) {
        this.uri = uri;
        this.method = method;
    }
}
