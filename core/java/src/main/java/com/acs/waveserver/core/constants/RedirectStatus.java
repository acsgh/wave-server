package com.acs.waveserver.core.constants;

public enum RedirectStatus {
    MOVED_PERMANENTLY(ResponseStatus.MOVED_PERMANENTLY),
    FOUND(ResponseStatus.FOUND),
    SEE_OTHER(ResponseStatus.SEE_OTHER),
    TEMPORARY_REDIRECT(ResponseStatus.TEMPORARY_REDIRECT),
    PERMANENT_REDIRECT(ResponseStatus.PERMANENT_REDIRECT);

    public final ResponseStatus status;

    RedirectStatus(ResponseStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status.toString();
    }
}
