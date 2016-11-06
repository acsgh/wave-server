package com.acs.waveserver.provider.common;

import com.acs.waveserver.core.HTTPRequest;
import com.acs.waveserver.core.HTTPResponse;
import com.acs.waveserver.core.Router;
import com.acs.waveserver.core.constants.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class WaveServerFilter implements Filter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final Router router;
    private String contextPath;

    public WaveServerFilter(Router router) {
        this.router = router;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        contextPath = filterConfig.getServletContext().getContextPath();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HTTPResponse waveResponse = router.process(toWaveRequest(request));

        if (waveResponse.responseStatus != ResponseStatus.NOT_FOUND) {
            transferParams(waveResponse, response);
        } else {
            chain.doFilter(request, response);
        }
    }


    @Override
    public void destroy() {
        contextPath = null;
    }


    private void transferParams(HTTPResponse waveResponse, ServletResponse response) {

    }

    private HTTPRequest toWaveRequest(ServletRequest request) {
        return null;
    }
}