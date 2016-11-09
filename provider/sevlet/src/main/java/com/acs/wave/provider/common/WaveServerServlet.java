package com.acs.wave.provider.common;

import com.acs.wave.router.HTTPResponse;
import com.acs.wave.router.HTTPRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WaveServerServlet extends HttpServlet {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final HTTPRouter httpRouter;

    public WaveServerServlet(HTTPRouter httpRouter) {
        this.httpRouter = httpRouter;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HTTPResponse waveResponse = httpRouter.process(ServletUtils.toWaveRequest(request));
        ServletUtils.transferParams(waveResponse, response);
    }

}