package com.acs.waveserver.core.constants;

public enum ProtocolVersion {
    HTTP_1_0(Protocol.HTTP, 1, 0, false, true),
    HTTP_1_1(Protocol.HTTP, 1, 1, true, true);

    public static ProtocolVersion fromString(String name) {
        for (ProtocolVersion protocolVersion : values()) {
            if (protocolVersion.toString().equals(name)) {
                return protocolVersion;
            }
        }
        throw new IllegalArgumentException("There is no ProtocolVersion with name: " + name);
    }

    public final Protocol protocol;
    public final int majorVersion;
    public final int minorVersion;
    public final boolean keepAliveDefault;
    public final boolean bytes;

    ProtocolVersion(Protocol protocol, int majorVersion, int minorVersion, boolean keepAliveDefault, boolean bytes) {
        this.protocol = protocol;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.keepAliveDefault = keepAliveDefault;
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return protocol + "/" + majorVersion + "." + minorVersion;
    }
}
