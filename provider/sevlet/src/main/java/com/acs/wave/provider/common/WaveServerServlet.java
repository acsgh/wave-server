package com.acs.wave.provider.common;

import com.acs.wave.router.HTTPResponse;
import com.acs.wave.router.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WaveServerServlet extends HttpServlet {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final Router router;

    public WaveServerServlet(Router router) {
        this.router = router;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HTTPResponse waveResponse = router.process(ServletUtils.toWaveRequest(request));
        ServletUtils.transferParams(waveResponse, response);
    }

}