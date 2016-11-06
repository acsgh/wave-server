package com.acs.wave.router;

import com.acs.wave.router.constants.ProtocolVersion;

public class HTTPItem {
    public final ProtocolVersion protocolVersion;
    public final HTTPHeaders headers;

    public HTTPItem(ProtocolVersion protocolVersion, HTTPHeaders headers) {
        this.protocolVersion = protocolVersion;
        this.headers = headers.clone();
    }


    @Override
    public String toString() {
        return "HTTPItem{" +
                "protocolVersion=" + protocolVersion +
                ", headers=" + headers +
                '}';
    }
}
