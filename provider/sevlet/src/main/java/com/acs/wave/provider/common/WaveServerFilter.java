package com.acs.wave.provider.common;

import com.acs.wave.router.HTTPResponse;
import com.acs.wave.router.HTTPRouter;
import com.acs.wave.router.constants.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WaveServerFilter implements Filter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final HTTPRouter httpRouter;

    public WaveServerFilter(HTTPRouter httpRouter) {
        this.httpRouter = httpRouter;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HTTPResponse waveResponse = httpRouter.process(ServletUtils.toWaveRequest((HttpServletRequest) request));

        if (waveResponse.responseStatus != ResponseStatus.NOT_FOUND) {
            ServletUtils.transferParams(waveResponse, (HttpServletResponse) response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}